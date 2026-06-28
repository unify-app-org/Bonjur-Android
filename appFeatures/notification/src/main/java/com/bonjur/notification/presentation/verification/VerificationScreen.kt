package com.bonjur.notification.presentation.verification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.designSystem.components.alert.AppAlert
import com.bonjur.designSystem.components.alert.AppAlertPresenter
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.navigation.Navigator
import com.bonjur.notification.presentation.verification.components.VerificationView
import com.bonjur.notification.presentation.verification.models.VerificationAction
import com.bonjur.notification.presentation.verification.models.VerificationSideEffect
import kotlinx.coroutines.launch

@Composable
fun VerificationScreen(
    navigator: Navigator,
    viewModel: VerificationViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.init(navigator) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            isScrolled = true,
            showTitle = true,
            title = "Verifications",
            onBack = { scope.launch { navigator.navigateUp() } }
        )
        FeatureScreen(
            viewModel = viewModel,
            handleEffect = { effect ->
                when (effect) {
                    is VerificationSideEffect.Error -> AppSnackBar.show(
                        title = effect.message ?: "Something went wrong",
                        style = AppSnackBar.Style.ERROR
                    )
                    is VerificationSideEffect.ConfirmReject -> AppAlertPresenter.present(
                        AppAlert(
                            config = AppAlert.Config(
                                title = "Reject verification?",
                                subtitle = "Are you sure you want to reject ${effect.item.clubName}'s verification request?"
                            ),
                            actions = listOf(
                                AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.SECONDARY),
                                AppAlert.Action(
                                    title = "Reject",
                                    style = AppAlert.Action.Style.DESTRUCTIVE,
                                    handler = { viewModel.store.send(VerificationAction.PerformReject(effect.item)) }
                                )
                            )
                        )
                    )
                }
            }
        ) { store ->
            VerificationView(store = store)
        }
    }
}
