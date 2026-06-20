package com.bonjur.communities.presentation.membersList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.membersList.components.MembersListView
import com.bonjur.communities.presentation.membersList.models.MembersListInputData
import com.bonjur.communities.presentation.membersList.models.MembersListSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun MembersListScreen(
    inputData: MembersListInputData,
    navigator: Navigator,
    viewModel: MembersListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is MembersListSideEffect.Loading -> { /* Show/hide loading */ }
            }
        }
    ) { store ->
        MembersListView(store = store)
    }
}
