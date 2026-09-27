package com.bonjur.events.presentation.details.components

import com.bonjur.designsystem.R as DesignR
import androidx.compose.ui.res.stringResource
import com.bonjur.events.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.member.components.ReportReasonsScreen
import com.bonjur.member.policy.ActivityReportReason
import com.bonjur.member.policy.MemberOptionsPolicy

/**
 * Event 3-dot options sheet: Report event / Leave event / Share.
 * Compose port of iOS `EventOptionsSheet`. Pure UI — visibility is decided from
 * [viewerRole]; the exit confirmation lives in `EventDetailsViewModel` (events
 * have no owner-transfer gate). This sheet only renders rows and delegates out.
 *
 * Report opens the shared reason screen in place; the sheet closes once the
 * report is accepted. Share is still "Coming soon".
 */
private val DestructiveRed = Color(0xFFE5484D)

@Composable
fun EventOptionsSheet(
    viewerRole: AppUIEntities.UserActivityRole,
    onExit: () -> Unit,
    onReport: (ActivityReportReason, onResult: (Boolean) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    // Leave shows for joined members; Report shows for everyone but the creator
    // (you can't report your own event).
    val showExit = viewerRole != AppUIEntities.UserActivityRole.NOT_JOINED
    val showReport = MemberOptionsPolicy.canReportActivity(viewerRole)

    var showReportScreen by remember { mutableStateOf(false) }
    var isReporting by remember { mutableStateOf(false) }

    AppBottomSheet(onDismiss = onDismiss) {
        if (showReportScreen) {
            ReportReasonsScreen(
                title = stringResource(DesignR.string.common_report),
                reasons = ActivityReportReason.entries,
                reasonTitle = { it.displayTitle },
                isSubmitting = isReporting,
                onBack = { showReportScreen = false },
                onSubmit = { reason ->
                    isReporting = true
                    onReport(reason) { ok ->
                        isReporting = false
                        if (ok) onDismiss()
                    }
                }
            )
        } else Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            if (showReport) {
                EventOptionRow(
                    title = stringResource(R.string.events_report),
                    tint = DestructiveRed
                ) { showReportScreen = true }
                RowDivider()
            }

            if (showExit) {
                EventOptionRow(
                    title = stringResource(R.string.events_leave_confirm),
                    tint = DestructiveRed
                ) {
                    onDismiss()
                    onExit()
                }
                RowDivider()
            }

            EventOptionRow(
                title = stringResource(DesignR.string.common_share),
                tint = Palette.blackMedium,
                trailing = stringResource(DesignR.string.common_coming_soon),
                enabled = false
            ) {}
        }
    }
}

@Composable
private fun EventOptionRow(
    title: String,
    tint: Color,
    trailing: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (enabled) it.clickable(onClick = onClick) else it }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = AppTypography.TextL.medium,
            color = tint
        )
        if (trailing != null) {
            Text(
                text = trailing,
                style = AppTypography.TextMd.regular,
                color = Palette.blackMedium
            )
        }
    }
}

@Composable
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Palette.grayTeritary.copy(alpha = 0.6f))
    )
}
