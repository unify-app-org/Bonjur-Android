package com.bonjur.clubs.presentation.create

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.clubs.presentation.create.components.ClubCreateView
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun ClubCreateScreen(
    inputData: ClubCreateInputData,
    navigator: Navigator,
    viewModel: ClubCreateViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ClubCreateSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is ClubCreateSideEffect.Error -> com.bonjur.designSystem.components.snackbar.AppSnackBar.show(
                    title = effect.message,
                    style = com.bonjur.designSystem.components.snackbar.AppSnackBar.Style.ERROR
                )
            }
        }
    ) { store ->
        ClubCreateView(store = store)
    }
}
