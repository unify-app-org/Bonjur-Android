package com.bonjur.communities.presentation.facultyStudentList

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.facultyStudentList.components.FacultyStudentListView
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListInputData
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun FacultyStudentListScreen(
    inputData: FacultyStudentListInputData,
    navigator: Navigator,
    viewModel: FacultyStudentListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is FacultyStudentListSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
            }
        }
    ) { store ->
        FacultyStudentListView(store = store)
    }
}
