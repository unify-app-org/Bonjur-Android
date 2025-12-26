package com.bonjur.auth.presentation.welcome

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.auth.presentation.welcome.components.AuthWelcomeView
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeSideEffect

@Composable
fun AuthWelcomeScreen(
    inputData: AuthWelcomeInputData,
    viewModel: AuthWelcomeViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is AuthWelcomeSideEffect.Loading -> {}
            }
        }
    ) { store ->
        AuthWelcomeView(store)
    }
}
