package com.bonjur.member.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R
import com.bonjur.member.policy.ActivityReportReason
import com.bonjur.member.policy.ReportReason
import com.bonjur.network.model.userMessage
import com.bonjur.network.report.ReportService
import com.bonjur.network.report.ReportTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sends reports through the shared [ReportService] and shows the outcome.
 * Every report sheet (member / club / event / hangout) goes through here, so
 * the snackbars are identical everywhere. Mirrors iOS `ReportSubmitter`.
 *
 * Runs in [viewModelScope], not the sheet's scope: the request must survive the
 * sheet being dismissed while it is in flight.
 */
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val service: ReportService
) : ViewModel() {

    /** [onResult] gets `true` on success — the sheet closes only then. */
    fun reportUser(userId: String, reason: ReportReason, onResult: (Boolean) -> Unit) =
        submit(ReportTarget.User(userId), reason.code, reason.displayTitle, onResult)

    fun reportActivity(
        target: ReportTarget,
        reason: ActivityReportReason,
        onResult: (Boolean) -> Unit
    ) = submit(target, reason.code, reason.displayTitle, onResult)

    private fun submit(
        target: ReportTarget,
        code: String,
        details: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val ok = try {
                service.report(target, reason = code, details = details)
                true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                AppSnackBar.showError(e.userMessage())
                false
            }
            if (ok) {
                AppSnackBar.show(
                    title = LanguageManager.string(R.string.common_report_submitted),
                    style = AppSnackBar.Style.SUCCESS
                )
            }
            onResult(ok)
        }
    }
}
