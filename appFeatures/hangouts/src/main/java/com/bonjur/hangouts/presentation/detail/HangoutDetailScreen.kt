package com.bonjur.hangouts.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.hangouts.presentation.detail.components.HangoutDetailsView
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun HangoutDetailsScreen(
    inputData: HangoutDetailsInputData,
    navigator: Navigator,
    viewModel: HangoutDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is HangoutDetailsSideEffect.Loading -> {
                    // Show/hide loading
                }
            }
        }
    ) { store ->
        HangoutDetailsView(store = store)
    }
}