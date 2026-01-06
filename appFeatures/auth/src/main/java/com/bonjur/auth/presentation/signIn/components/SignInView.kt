package com.bonjur.auth.presentation.signIn.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.signIn.model.SignInAction
import com.bonjur.auth.presentation.signIn.model.SignInSideEffect
import com.bonjur.auth.presentation.signIn.model.SignInViewState
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.textField.AppTextField
import com.bonjur.designSystem.components.textField.AppTextFieldModel
import com.bonjur.designSystem.components.textField.FieldType
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun SignInView(
    store: FeatureStore<SignInViewState, SignInAction, SignInSideEffect>
) {
    val state = store.state

    LaunchedEffect(Unit) {
        store.send(SignInAction.FetchData)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        TopView()

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AppTextField(
                text = state.email,
                onTextChange = { /* update via action if needed */ },
                placeHolder = "Enter your email",
                model = AppTextFieldModel(
                    title = "Email"
                )
            )

            AppTextField(
                text = state.password,
                onTextChange = { /* update via action if needed */ },
                placeHolder = "Enter your password",
                model = AppTextFieldModel(
                    title = "Password",
                    type = FieldType.Secure
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            title = "Sign in",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = { store.send(SignInAction.SignIn) }
        )
    }
}

@Composable
private fun TopView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Sign In",
            style = AppTypography.TitleXL.extraBold,
            color = Palette.black
        )

        Text(
            text = "Only continue if you downloaded the app from a store or website that you trust.",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary
        )
    }
}
