package com.bonjur.auth.presentation.optional.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.components.textView.TextView

@Composable
fun AuthOptionalBioView(
    store: FeatureStore<AuthOptionalInfoViewState, AuthOptionalInfoAction, AuthOptionalInfoSideEffect>
) {
    val state = store.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopView()
        TextView(
            text = state.biography ?: "",
            onTextChange = { text ->
                store.send(AuthOptionalInfoAction.BioChange(text))
            },
            modifier = Modifier.height(200.dp)
        )
    }
}

@Composable
private fun TopView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Which language do you know?",
            style = AppTypography.TitleXL.extraBold,
            color = Palette.black,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Write something about yourself",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}