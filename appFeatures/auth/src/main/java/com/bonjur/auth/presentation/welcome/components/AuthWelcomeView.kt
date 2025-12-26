package com.bonjur.auth.presentation.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeAction
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeSideEffect
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeViewState
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun AuthWelcomeView(
    store: FeatureStore<AuthWelcomeViewState, AuthWelcomeAction, AuthWelcomeSideEffect>
) {
    val state = store.state

    LaunchedEffect(Unit) {
        store.send(AuthWelcomeAction.FetchData)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = Images.Icons.xmark(),
                contentDescription = null,
                modifier = Modifier
                    .clickable{
                        store.send(AuthWelcomeAction.Dismiss)
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = state.uiModel.title,
            style = AppTypography.TitleXL.extraBold
        )

        Text(
            text = state.uiModel.subtitle,
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Image(
            painter = state.uiModel.image(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                title = "Skip",
                model = AppButtonModel(type = ButtonType.Tertiary),
                onClick = {}
            )
            AppButton(
                title = "Continue",
                model = AppButtonModel(contentSize = ContentSize.Fill),
                onClick = {
                    store.send(AuthWelcomeAction.ContinueTapped)
                }
            )
        }
    }
}
