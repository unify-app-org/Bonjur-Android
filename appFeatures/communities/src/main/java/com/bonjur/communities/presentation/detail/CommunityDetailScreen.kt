package com.bonjur.communities.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.detail.components.CommunityDetailView
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.communities.presentation.detail.model.CommunityDetailSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun CommunityDetailScreen(
    inputData: CommunityDetailInputData = CommunityDetailInputData(communityId = 1),
    navigator: Navigator,
    viewModel: CommunityDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is CommunityDetailSideEffect.Loading -> {
                    // Handle loading effect
                }
            }
        }
    ) { store ->
        CommunityDetailView(store = store)
    }
}
