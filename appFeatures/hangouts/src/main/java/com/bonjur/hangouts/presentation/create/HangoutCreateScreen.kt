package com.bonjur.hangouts.presentation.create

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.hangouts.presentation.create.components.HangoutCreateView
import com.bonjur.hangouts.presentation.create.models.HangoutCreateInputData
import com.bonjur.hangouts.presentation.create.models.HangoutCreateSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun HangoutCreateScreen(
    inputData: HangoutCreateInputData,
    navigator: Navigator,
    viewModel: HangoutCreateViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is HangoutCreateSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is HangoutCreateSideEffect.Error -> AppSnackBar.show(
                    title = "Couldn't save hangout",
                    subtitle = effect.message,
                    style = AppSnackBar.Style.ERROR
                )
            }
        }
    ) { store ->
        HangoutCreateView(store = store)
    }
}
