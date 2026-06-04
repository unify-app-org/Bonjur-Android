package com.bonjur.hangouts.presentation.create

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
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
                is HangoutCreateSideEffect.Loading -> { /* Show/hide loading */ }
                is HangoutCreateSideEffect.Error -> { /* Show error */ }
            }
        }
    ) { store ->
        HangoutCreateView(store = store)
    }
}
