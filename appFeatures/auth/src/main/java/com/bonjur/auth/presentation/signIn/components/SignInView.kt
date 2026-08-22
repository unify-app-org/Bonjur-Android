package com.bonjur.auth.presentation.signIn.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bonjur.auth.R
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
                onTextChange = { store.send(SignInAction.EmailChanged(it)) },
                placeHolder = stringResource(R.string.auth_email_placeholder),
                model = AppTextFieldModel(
                    title = stringResource(R.string.auth_email)
                )
            )

            AppTextField(
                text = state.password,
                onTextChange = { store.send(SignInAction.PasswordChanged(it)) },
                placeHolder = stringResource(R.string.auth_password_placeholder),
                model = AppTextFieldModel(
                    title = stringResource(R.string.auth_password),
                    type = FieldType.Secure
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            title = stringResource(R.string.auth_sign_in_button),
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = { store.send(SignInAction.SignIn) }
        )
    }
}

@Composable
private fun TopView() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(R.string.auth_sign_in_title),
            style = AppTypography.TitleXL.extraBold,
            color = Palette.black
        )

        Text(
            text = stringResource(R.string.auth_sign_in_subtitle),
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary
        )
    }
}
