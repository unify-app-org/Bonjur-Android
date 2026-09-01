package com.bonjur.events.presentation.details

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.events.presentation.details.components.EventDetailsView
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.events.presentation.details.model.EventDetailsSideEffect
import com.bonjur.navigation.Navigator
import com.bonjur.events.presentation.details.model.EventDetailsAction

@Composable
fun EventDetailsScreen(
    inputData: EventDetailsInputData,
    navigator: Navigator,
    viewModel: EventDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    // Refetch on every entry, not just the first. Compose disposes this screen when Edit
    // is pushed and recomposes it on the way back, but `init` is guarded by
    // `if (::inputData.isInitialized) return`, so nothing reloaded and a saved edit looked
    // like it hadn't been applied. `init` now only wires up input/navigator; the fetch lives
    // here, mirroring iOS `.onAppear { store.send(.fetchData) }`.
    LaunchedEffect(Unit) {
        viewModel.store.send(EventDetailsAction.FetchData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is EventDetailsSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
            }
        }
    ) { store ->
        EventDetailsView(store = store)
    }
}