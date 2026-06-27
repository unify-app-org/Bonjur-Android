package com.bonjur.profile.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.detail.components.ProfileDetailView
import com.bonjur.profile.presentation.detail.models.ProfileDetailInputData
import com.bonjur.profile.presentation.detail.models.ProfileDetailSideEffect

@Composable
fun ProfileDetailScreen(
    inputData: ProfileDetailInputData,
    navigator: Navigator,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ProfileDetailSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is ProfileDetailSideEffect.Error -> {
                    AppSnackBar.show(
                        title = effect.title,
                        subtitle = effect.message ?: "",
                        style = AppSnackBar.Style.ERROR
                    )
                }
            }
        }
    ) { store ->
        ProfileDetailView(store = store)
    }
}