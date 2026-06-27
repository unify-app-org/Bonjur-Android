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
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is CommunitiesListSideEffect.Error -> com.bonjur.designSystem.components.snackbar.AppSnackBar.show(
                    title = effect.error.message ?: "Something went wrong",
                    style = com.bonjur.designSystem.components.snackbar.AppSnackBar.Style.ERROR
                )
            }
        }
    ) { store ->
        CommunitiesListView(store = store)
    }
}
