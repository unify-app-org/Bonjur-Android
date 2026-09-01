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

    // Refetch on every entry, not just the first. Compose disposes this screen when Edit
    // is pushed and recomposes it on the way back, but `init` is guarded by
    // `if (::inputData.isInitialized) return`, so nothing reloaded and a saved edit looked
    // like it hadn't been applied. `init` now only wires up input/navigator; the fetch lives
    // here, mirroring iOS `.onAppear { store.send(.fetchData) }`.
    LaunchedEffect(Unit) {
        viewModel.store.send(ClubDetailsAction.FetchData)
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