package com.bonjur.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.components.ProfileDetailView
import com.bonjur.profile.presentation.models.ProfileDetailInputData
import com.bonjur.profile.presentation.models.ProfileDetailSideEffect

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
                    // Show/hide loading
                }
            }
        }
    ) { store ->
        ProfileDetailView(store = store)
    }
}