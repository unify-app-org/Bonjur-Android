package com.bonjur.auth.presentation.optional.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import com.bonjur.designSystem.components.selectableList.SelectableListItem
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import kotlin.collections.forEachIndexed

@Composable
fun AuthOptionalSelectGenderView(
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
        GendersView(
            genders = state.genders,
            onGenderSelected = { index ->
                store.send(AuthOptionalInfoAction.SelectedGender(index))
            }
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
            text = "Your Gender",
            style = AppTypography.TitleXL.extraBold,
            color = Palette.black,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Select your gender",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GendersView(
    genders: List<SelectableListItemModel>,
    onGenderSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        genders.forEachIndexed { index, gender ->
            SelectableListItem (
                model = gender,
                onClick = {
                    onGenderSelected(index)

                }
            )
        }
    }
}