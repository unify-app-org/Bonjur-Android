package com.bonjur.clubs.presentation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.clubs.presentation.components.ClubDetailsView
import com.bonjur.clubs.presentation.model.*

@Composable
fun ClubDetailsScreen(
    inputData: ClubDetailsInputData,
    viewModel: ClubDetailsViewModel = hiltViewModel(),
    onEventCardClick: (String) -> Unit = {},
    onEventButtonClick: (String) -> Unit = {}
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ClubDetailsSideEffect.Loading -> {
                    // Show/hide loading
                }
            }
        }
    ) { store ->
        ClubDetailsView(
            store = store
        )
    }
}