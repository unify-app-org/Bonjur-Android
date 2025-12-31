package com.bonjur.auth.presentation.optional.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import com.bonjur.designSystem.components.selectableList.SelectableListItem
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.designSystem.ui.theme.colors.Palette
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import com.bonjur.designSystem.components.serach.SearchView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography

@Composable
fun AuthOptionalSelectLanguagesView(
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
        SearchView(
            text = state.languageSearchText ?: "",
            onTextChange = { value ->
                store.send(AuthOptionalInfoAction.LanguageTextChange(value))
            }
        )
        LanguagesView(
            languages = state.languages,
            onGenderSelected = { id ->
                store.send(AuthOptionalInfoAction.SelectedLanguage(id))
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
            text = "Which language do you know?",
            style = AppTypography.TitleXL.extraBold,
            color = Palette.black,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Select languages you know",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LanguagesView(
    languages: List<SelectableListItemModel>,
    onGenderSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        languages.forEach { language ->
            SelectableListItem(
                model = language,
                onClick = {
                    onGenderSelected(language.id)
                }
            )
        }
    }
}