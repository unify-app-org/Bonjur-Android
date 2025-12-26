package com.bonjur.auth.presentation.onboarding

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.auth.presentation.onboarding.components.OnboardingView
import com.bonjur.auth.presentation.onboarding.model.OnboardingInputData
import com.bonjur.auth.presentation.onboarding.model.OnboardingSideEffect

@Composable
fun OnboardingScreen(
    inputData: OnboardingInputData,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is OnboardingSideEffect.Loading -> {
                    // show/hide loading
                }
            }
        }
    ) { store ->
        OnboardingView(store = store)
    }
}
