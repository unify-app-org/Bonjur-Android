package com.bonjur.communities.presentation.facultyBrowse

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.facultyBrowse.components.FacultyBrowseView
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseInputData
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun FacultyBrowseScreen(
    inputData: FacultyBrowseInputData,
    navigator: Navigator,
    viewModel: FacultyBrowseViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is FacultyBrowseSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
            }
        }
    ) { store ->
        FacultyBrowseView(store = store)
    }
}
