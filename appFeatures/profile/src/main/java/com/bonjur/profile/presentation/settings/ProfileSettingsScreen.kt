package com.bonjur.profile.presentation.settings

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.settings.components.ProfileSettingsView
import com.bonjur.profile.presentation.settings.models.ProfileSettingsInputData
import com.bonjur.profile.presentation.settings.models.ProfileSettingsSideEffect

@Composable
fun ProfileSettingsScreen(
    inputData: ProfileSettingsInputData,
    navigator: Navigator,
    viewModel: ProfileSettingsViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ProfileSettingsSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is ProfileSettingsSideEffect.Error -> com.bonjur.designSystem.components.snackbar.AppSnackBar.showError(effect.message)
            }
        }
    ) { store ->
        ProfileSettingsView(store = store)
    }
}
