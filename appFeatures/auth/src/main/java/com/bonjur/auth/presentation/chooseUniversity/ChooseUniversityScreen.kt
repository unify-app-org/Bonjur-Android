package com.bonjur.auth.presentation.chooseUniversity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.auth.presentation.chooseUniversity.components.ChooseUniversityView
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityAction
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityInputData
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversitySideEffect
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun ChooseUniversityScreen(
    inputData: ChooseUniversityInputData,
    viewModel: ChooseUniversityViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ChooseUniversitySideEffect.Loading -> {

                }
            }
        }
    ) { store ->
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Icon(
                painter = Images.Icons.arrowLeft01(),
                contentDescription = "Back",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable{
                        store.send(ChooseUniversityAction.Dismiss)
                    }
            )
            ChooseUniversityView(store = store)
        }
    }
}
