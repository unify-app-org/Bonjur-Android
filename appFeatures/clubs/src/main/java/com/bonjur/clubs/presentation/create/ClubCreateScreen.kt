package com.bonjur.clubs.presentation.create

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.clubs.presentation.create.components.ClubCreateView
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun ClubCreateScreen(
    inputData: ClubCreateInputData,
    navigator: Navigator,
    viewModel: ClubCreateViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ClubCreateSideEffect.Loading -> { /* Show/hide loading */ }
                is ClubCreateSideEffect.Error -> { /* Show error */ }
            }
        }
    ) { store ->
        ClubCreateView(store = store)
    }
}
