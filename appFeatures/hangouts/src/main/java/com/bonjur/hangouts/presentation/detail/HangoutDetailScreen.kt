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
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsAction

@Composable
fun HangoutDetailsScreen(
    inputData: HangoutDetailsInputData,
    navigator: Navigator,
    viewModel: HangoutDetailsViewModel = hiltViewModel()
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
        viewModel.store.send(HangoutDetailsAction.FetchData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is HangoutDetailsSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
            }
        }
    ) { store ->
        HangoutDetailsView(store = store)
    }
}