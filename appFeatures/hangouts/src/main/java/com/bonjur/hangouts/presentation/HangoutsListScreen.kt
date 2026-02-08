//
//  HangoutsListScreen.kt
//  Hangouts
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.hangouts.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.hangouts.presentation.components.HangoutsListView
import com.bonjur.hangouts.presentation.model.HangoutsListInputData
import com.bonjur.hangouts.presentation.model.HangoutsListSideEffect
import com.bonjur.navigation.Navigator


@Composable
fun HangoutsListScreen(
    inputData: HangoutsListInputData = HangoutsListInputData(),
    navigator: Navigator,
    viewModel: HangoutsListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is HangoutsListSideEffect.Loading -> {
                    // Handle loading effect
                }
                is HangoutsListSideEffect.Error -> {
                    // Handle error effect
                }
            }
        }
    ) { store ->
        HangoutsListView(store = store)
    }
}