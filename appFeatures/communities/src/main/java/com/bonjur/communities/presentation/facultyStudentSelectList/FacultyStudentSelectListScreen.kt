package com.bonjur.communities.presentation.facultyStudentSelectList

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.communities.presentation.facultyStudentSelectList.components.FacultyStudentSelectListView
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListInputData
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun FacultyStudentSelectListScreen(
    inputData: FacultyStudentSelectListInputData,
    navigator: Navigator,
    viewModel: FacultyStudentSelectListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is FacultyStudentSelectListSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
            }
        }
    ) { store ->
        FacultyStudentSelectListView(store = store)
    }
}
