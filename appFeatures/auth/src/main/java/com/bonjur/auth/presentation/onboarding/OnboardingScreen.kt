package com.bonjur.auth.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            OnboardingView(store = store)
        }
    }
}
