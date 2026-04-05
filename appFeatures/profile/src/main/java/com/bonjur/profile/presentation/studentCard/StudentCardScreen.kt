package com.bonjur.profile.presentation.studentCard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.studentCard.components.StudentCardView
import com.bonjur.profile.presentation.studentCard.models.StudentCardInputData
import com.bonjur.profile.presentation.studentCard.models.StudentCardSideEffect

@Composable
fun StudentCardScreen(
    inputData: StudentCardInputData,
    navigator: Navigator,
    viewModel: StudentCardViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is StudentCardSideEffect.Loading -> {
                    // Show/hide loading
                }
            }
        }
    ) { store ->
        StudentCardView(store = store)
    }
}
