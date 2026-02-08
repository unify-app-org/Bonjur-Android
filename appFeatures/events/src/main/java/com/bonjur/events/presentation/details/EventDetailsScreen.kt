package com.bonjur.events.presentation.details

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.events.presentation.details.components.EventDetailsView
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.events.presentation.details.model.EventDetailsSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun EventDetailsScreen(
    inputData: EventDetailsInputData,
    navigator: Navigator,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is EventDetailsSideEffect.Loading -> {
                    // Show/hide loading
                }
            }
        }
    ) { store ->
        EventDetailsView(store = store)
    }
}