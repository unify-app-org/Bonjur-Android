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
                    // Only the filter-apply path posts loading (initial/reappear load inline).
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is DiscoverSideEffect.Error -> com.bonjur.designSystem.components.snackbar.AppSnackBar.show(
                    title = effect.error.message ?: "Something went wrong",
                    style = com.bonjur.designSystem.components.snackbar.AppSnackBar.Style.ERROR
                )
            }
        }
    ) { store ->
        DiscoverView(store = store)
    }
}