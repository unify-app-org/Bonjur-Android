//
//  HangoutsListScreen.kt
//  Hangouts
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.hangouts.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.hangouts.presentation.list.components.HangoutsListView
import com.bonjur.hangouts.presentation.list.model.HangoutsListInputData
import com.bonjur.hangouts.presentation.list.model.HangoutsListSideEffect
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
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is HangoutsListSideEffect.Error -> com.bonjur.designSystem.components.snackbar.AppSnackBar.show(
                    title = effect.error.message ?: "Something went wrong",
                    style = com.bonjur.designSystem.components.snackbar.AppSnackBar.Style.ERROR
                )
            }
        }
    ) { store ->
        HangoutsListView(store = store)
    }
}