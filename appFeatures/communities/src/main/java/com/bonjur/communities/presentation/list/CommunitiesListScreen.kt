package com.bonjur.communities.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.list.components.CommunitiesListView
import com.bonjur.communities.presentation.list.model.CommunitiesListInputData
import com.bonjur.communities.presentation.list.model.CommunitiesListSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun CommunitiesListScreen(
    inputData: CommunitiesListInputData = CommunitiesListInputData(),
    navigator: Navigator,
    viewModel: CommunitiesListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is CommunitiesListSideEffect.Loading -> {
                    // Handle loading effect
                }
                is CommunitiesListSideEffect.Error -> {
                    // Handle error effect
                }
            }
        }
    ) { store ->
        CommunitiesListView(store = store)
    }
}
