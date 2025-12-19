package com.bonjur.auth.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen

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