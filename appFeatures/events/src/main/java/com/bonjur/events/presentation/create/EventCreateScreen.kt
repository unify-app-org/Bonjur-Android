package com.bonjur.events.presentation.create

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.events.presentation.create.components.EventCreateView
import com.bonjur.events.presentation.create.models.EventCreateInputData
import com.bonjur.events.presentation.create.models.EventCreateSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun EventCreateScreen(
    inputData: EventCreateInputData,
    navigator: Navigator,
    viewModel: EventCreateViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is EventCreateSideEffect.Loading -> { /* Show/hide loading */ }
                is EventCreateSideEffect.Error -> AppSnackBar.show(
                    title = "Couldn't save event",
                    subtitle = effect.message,
                    style = AppSnackBar.Style.ERROR
                )
            }
        }
    ) { store ->
        EventCreateView(store = store)
    }
}
