package com.bonjur.clubs.presentation.create.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.create.models.ClubCreateAction
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.clubs.presentation.create.models.ClubCreateViewState
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.textField.AppTextField
import com.bonjur.designSystem.components.textField.AppTextFieldModel
import com.bonjur.designSystem.components.textView.TextView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun ClubCreateView(
    store: FeatureStore<ClubCreateViewState, ClubCreateAction, ClubCreateSideEffect>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = store.state.topTitle,
                style = AppTypography.TitleL.extraBold
            )

            Text(
                text = "Fields marked with * are required.",
                style = AppTypography.BodyTextMd.regular,
                color = Palette.appBlue
            )

            AppTextField(
                text = store.state.name,
                onTextChange = { store.send(ClubCreateAction.NameChanged(it)) },
                placeHolder = "Enter club name",
                model = AppTextFieldModel(title = "Club name *")
            )

            AppTextField(
                text = store.state.ownerContact,
                onTextChange = { store.send(ClubCreateAction.OwnerContactChanged(it)) },
                placeHolder = "Enter owner contact",
                model = AppTextFieldModel(title = "Owner contact")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "About", style = AppTypography.HeadingMd.medium)
                TextView(
                    text = store.state.about,
                    onTextChange = { store.send(ClubCreateAction.AboutChanged(it)) },
                    characterLimit = 500,
                    placeholder = "About",
                    modifier = Modifier.height(120.dp)
                )
            }

            AppTextField(
                text = store.state.location,
                onTextChange = { store.send(ClubCreateAction.LocationChanged(it)) },
                placeHolder = "Enter location",
                model = AppTextFieldModel(title = "Location")
            )

            AppTextField(
                text = store.state.capacity,
                onTextChange = { store.send(ClubCreateAction.CapacityChanged(it)) },
                placeHolder = "Enter capacity",
                model = AppTextFieldModel(title = "Capacity")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Rules", style = AppTypography.HeadingMd.medium)
                TextView(
                    text = store.state.rules,
                    onTextChange = { store.send(ClubCreateAction.RulesChanged(it)) },
                    characterLimit = 500,
                    placeholder = "Rules",
                    modifier = Modifier.height(120.dp)
                )
            }

            VisibilityPicker(
                isPublic = store.state.isPublic,
                onChanged = { store.send(ClubCreateAction.VisibilityChanged(it)) }
            )
        }

        AppButton(
            title = "Continue",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = { store.send(ClubCreateAction.ContinueTapped) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            enabled = store.state.isValid
        )
    }
}

@Composable
private fun VisibilityPicker(
    isPublic: Boolean,
    onChanged: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Visibility",
            style = AppTypography.HeadingMd.medium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(
                    selected = isPublic,
                    onClick = { onChanged(true) }
                )
                Text("Public", style = AppTypography.TextMd.regular)
            }
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(
                    selected = !isPublic,
                    onClick = { onChanged(false) }
                )
                Text("Private", style = AppTypography.TextMd.regular)
            }
        }
    }
}
