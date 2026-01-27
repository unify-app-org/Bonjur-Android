//
//  DiscoverScreen.kt
//  Discover
//
//  Created by Huseyn Hasanov on 11.01.26
//

package com.bonjur.discover.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen

@Preview(showBackground = true)
@Composable
fun DiscoverScreen(
    inputData: DiscoverInputData = DiscoverInputData(),
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is DiscoverSideEffect.Loading -> {
                    // Handle loading effect
                }
                is DiscoverSideEffect.Error -> {
                    // Handle error effect
                }
            }
        }
    ) { store ->
        DiscoverView(store = store)
    }
}