package com.bonjur.clubs.presentation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.clubs.presentation.components.ClubDetailsView
import com.bonjur.clubs.presentation.model.*
import com.bonjur.navigation.Navigator

@Composable
fun ClubDetailsScreen(
    inputData: ClubDetailsInputData,
    navigator: Navigator,
    viewModel: ClubDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ClubDetailsSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
            }
        }
    ) { store ->
        ClubDetailsView(
            store = store
        )
    }
}