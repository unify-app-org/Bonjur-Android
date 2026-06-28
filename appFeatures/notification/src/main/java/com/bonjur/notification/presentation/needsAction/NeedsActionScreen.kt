package com.bonjur.notification.presentation.needsAction

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
import com.bonjur.notification.presentation.needsAction.components.NeedsActionView
import com.bonjur.notification.presentation.needsAction.models.NeedsActionAction
import com.bonjur.notification.presentation.needsAction.models.NeedsActionSideEffect
import kotlinx.coroutines.launch

@Composable
fun NeedsActionScreen(
    navigator: Navigator,
    viewModel: NeedsActionViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.init(navigator) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            isScrolled = true,
            showTitle = true,
            title = "Needs your action",
            onBack = { scope.launch { navigator.navigateUp() } }
        )
        FeatureScreen(
            viewModel = viewModel,
            handleEffect = { effect ->
                when (effect) {
                    is NeedsActionSideEffect.Error -> AppSnackBar.show(
                        title = effect.message ?: "Something went wrong",
                        style = AppSnackBar.Style.ERROR
                    )
                    is NeedsActionSideEffect.ConfirmReject -> AppAlertPresenter.present(
                        AppAlert(
                            config = AppAlert.Config(
                                title = "Reject request?",
                                subtitle = "Are you sure you want to reject ${effect.item.requesterName}'s request to join ${effect.item.targetName}?"
                            ),
                            actions = listOf(
                                AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.SECONDARY),
                                AppAlert.Action(
                                    title = "Reject",
                                    style = AppAlert.Action.Style.DESTRUCTIVE,
                                    handler = { viewModel.store.send(NeedsActionAction.PerformReject(effect.item)) }
                                )
                            )
                        )
                    )
                }
            }
        ) { store ->
            NeedsActionView(store = store)
        }
    }
}
