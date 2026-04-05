package com.bonjur.auth.presentation.signIn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.auth.presentation.chooseUniversity.components.ChooseUniversityView
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityAction
import com.bonjur.auth.presentation.signIn.components.SignInView
import com.bonjur.auth.presentation.signIn.model.SignInAction
import com.bonjur.auth.presentation.signIn.model.SignInInputData
import com.bonjur.auth.presentation.signIn.model.SignInSideEffect
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun SignInScreen(
    inputData: SignInInputData,
    viewModel: SignInViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is SignInSideEffect.Loading -> {
                    // show / hide loader
                }
            }
        }
    ) { store ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(vertical = 16.dp)
        ) {
            Icon(
                painter = Images.Icons.arrowLeft01(),
                contentDescription = "Back",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable{
                        store.send(SignInAction.Dismiss)
                    }
            )
            SignInView(store = store)
        }
    }
}
