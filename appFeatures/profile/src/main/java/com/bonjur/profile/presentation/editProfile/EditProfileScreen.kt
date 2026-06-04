package com.bonjur.profile.presentation.editProfile

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.editProfile.components.EditProfileView
import com.bonjur.profile.presentation.editProfile.models.EditProfileInputData
import com.bonjur.profile.presentation.editProfile.models.EditProfileSideEffect

@Composable
fun EditProfileScreen(
    inputData: EditProfileInputData,
    navigator: Navigator,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is EditProfileSideEffect.Loading -> { /* Show/hide loading */ }
                is EditProfileSideEffect.Error -> { /* Show error snackbar */ }
            }
        }
    ) { store ->
        EditProfileView(store = store)
    }
}
