package com.bonjur.communities.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.detail.components.CommunityDetailView
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.communities.presentation.detail.model.CommunityDetailSideEffect
import com.bonjur.navigation.Navigator
import com.bonjur.communities.presentation.detail.model.CommunityDetailAction

@Composable
fun CommunityDetailScreen(
    inputData: CommunityDetailInputData = CommunityDetailInputData(communityId = 1),
    navigator: Navigator,
    viewModel: CommunityDetailViewModel = hiltViewModel()
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
        viewModel.store.send(CommunityDetailAction.FetchData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is CommunityDetailSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
            }
        }
    ) { store ->
        CommunityDetailView(store = store)
    }
}
