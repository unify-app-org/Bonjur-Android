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
import com.bonjur.discover.presentation.components.DiscoverView
import com.bonjur.discover.presentation.models.DiscoverInputData
import com.bonjur.discover.presentation.models.DiscoverSideEffect
import com.bonjur.navigation.Navigator
import com.bonjur.network.model.userMessage

@Composable
fun DiscoverScreen(
    inputData: DiscoverInputData = DiscoverInputData(),
    navigator: Navigator,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is DiscoverSideEffect.Loading -> {
                    // Posted by the first load and the filter-apply path. Pull-to-refresh
                    // and the reappear refresh stay inline (no overlay).
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is DiscoverSideEffect.Error -> com.bonjur.designSystem.components.snackbar.AppSnackBar.showError(effect.error.userMessage())
            }
        }
    ) { store ->
        DiscoverView(store = store)
    }
}