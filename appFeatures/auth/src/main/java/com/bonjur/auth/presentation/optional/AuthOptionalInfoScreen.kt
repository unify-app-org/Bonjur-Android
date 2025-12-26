package com.bonjur.auth.presentation.optional

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.auth.presentation.optional.components.AuthOptionalInfoView
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoInputData

@Composable
fun AuthOptionalInfoScreen(
    inputData: AuthOptionalInfoInputData,
    viewModel: AuthOptionalInfoViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { /* loading when added later */ }
    ) { store ->
        AuthOptionalInfoView(store = store)
    }
}
