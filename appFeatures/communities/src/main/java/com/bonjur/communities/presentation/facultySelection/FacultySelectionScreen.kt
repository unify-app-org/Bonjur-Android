package com.bonjur.communities.presentation.facultySelection

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.facultySelection.components.FacultySelectionView
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionInputData
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun FacultySelectionScreen(
    inputData: FacultySelectionInputData,
    navigator: Navigator,
    viewModel: FacultySelectionViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is FacultySelectionSideEffect.Loading -> { /* Show/hide loading */ }
            }
        }
    ) { store ->
        FacultySelectionView(store = store)
    }
}
