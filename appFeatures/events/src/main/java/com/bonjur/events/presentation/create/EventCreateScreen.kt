package com.bonjur.events.presentation.create

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.events.presentation.create.components.EventCreateView
import com.bonjur.events.presentation.create.models.EventCreateInputData
import com.bonjur.events.presentation.create.models.EventCreateSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun EventCreateScreen(
    inputData: EventCreateInputData,
    navigator: Navigator,
    viewModel: EventCreateViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is EventCreateSideEffect.Loading -> { /* Show/hide loading */ }
                is EventCreateSideEffect.Error -> { /* Show error */ }
            }
        }
    ) { store ->
        EventCreateView(store = store)
    }
}
