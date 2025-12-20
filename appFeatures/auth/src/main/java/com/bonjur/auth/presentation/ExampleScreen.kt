package com.bonjur.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.auth.presentation.components.ExampleView
import com.bonjur.auth.presentation.model.ExampleInputData
import com.bonjur.auth.presentation.model.ExampleSideEffect

@Composable
fun ExampleScreen(
    inputData: ExampleInputData,
    viewModel: ExampleViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ExampleSideEffect.Loading -> {

                }
            }
        }
    ) { store ->
        ExampleView(store = store)
    }
}