package com.bonjur.auth.presentation.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            AuthWelcomeView(store)
        }
    }
}
