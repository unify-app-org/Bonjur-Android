package com.bonjur.auth.presentation.chooseUniversity.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityAction
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversitySideEffect
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityViewState
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel.Style
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.selectableList.SelectableListItem
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun ChooseUniversityView(
    store: FeatureStore<ChooseUniversityViewState, ChooseUniversityAction, ChooseUniversitySideEffect>
) {
    val state = store.state

    LaunchedEffect(Unit) {
        store.send(ChooseUniversityAction.FetchData)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // Top View
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Choose \nUniversity",
                style = AppTypography.TitleXL.extraBold,
                color = Palette.blackHigh
            )
            Text(
                text = "In which university are you studying?",
                style = AppTypography.BodyTextMd.regular,
                color = Palette.grayPrimary
            )
        }

        // List View
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(state.uiModel) { index, university ->
                SelectableListItem(
                    model = university,
                    onClick = {
                        store.send(ChooseUniversityAction.SelectedCell(index))
                    }
                )
            }
        }

        AppButton(
            title = "Next",
            model = AppButtonModel(
                contentSize = ContentSize.Fill
            ),
            onClick = {
                store.send(ChooseUniversityAction.NextTapped)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.enabled
        )
    }
}
