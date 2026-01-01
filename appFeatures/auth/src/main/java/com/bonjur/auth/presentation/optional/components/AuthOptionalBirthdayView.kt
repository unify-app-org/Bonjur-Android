package com.bonjur.auth.presentation.optional.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import java.time.LocalDate

@Composable
fun AuthOptionalBirthdayView(
    store: FeatureStore<AuthOptionalInfoViewState, AuthOptionalInfoAction, AuthOptionalInfoSideEffect>
) {
    val state = store.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        TopView()
        InputField(
            date = state.birthDate,
            onTap = {
                store.send(AuthOptionalInfoAction.OpenDatePicker)
            }
        )
    }
}

@Composable
private fun InputField(
    date: LocalDate?,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = Palette.grayPrimary,
                    shape = androidx.compose.foundation.shape.CircleShape
                )
                .padding(horizontal = 24.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (date != null) {
                Text(
                    text = date.format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")),
                    modifier = Modifier.weight(1f),
                    color = Palette.blackHigh
                )
            } else {
                Text(
                    text = "Date of birth",
                    modifier = Modifier.weight(1f),
                    color = Palette.blackDisabled
                )
            }

            Icon(
                painter = Images.Icons.calendar(),
                contentDescription = "Calendar",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun TopView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Your Birthday",
            style = AppTypography.TitleXL.extraBold,
            color = Palette.blackHigh,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "What's your date of birth?",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}