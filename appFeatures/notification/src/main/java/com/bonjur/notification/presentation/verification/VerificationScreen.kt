package com.bonjur.notification.presentation.verification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.navigation.Navigator
import com.bonjur.notification.presentation.verification.components.VerificationView
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
                }
            }
        ) { store ->
            VerificationView(store = store)
        }
    }
}
