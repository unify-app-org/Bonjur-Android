package com.bonjur.hangouts.presentation.create.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.categorieChips.SelectCategoryView
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.FieldSchemaRouter
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.hangouts.presentation.create.models.HangoutCreateAction
import com.bonjur.hangouts.presentation.create.models.HangoutCreateSideEffect
import com.bonjur.hangouts.presentation.create.models.HangoutCreateViewState

@Composable
fun HangoutCreateView(
    store: FeatureStore<HangoutCreateViewState, HangoutCreateAction, HangoutCreateSideEffect>
) {
    val state = store.state
    val scrollState = rememberScrollState()
    val isScrolled by remember { derivedStateOf { scrollState.value > 30 } }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
                    // Clear the floating AppTopBar (back button) — no cover header
                    // pushes the title down here (unlike Events).
                    .padding(top = 64.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = state.topTitle, style = AppTypography.TitleL.extraBold)

                Text(
                    text = "Fields marked with * are required.",
                    style = AppTypography.BodyTextMd.regular,
                    color = Palette.appBlue
                )

                state.schema.forEach { field ->
                    // Hangout name is immutable once the hangout exists (mirrors iOS).
                    val isLocked = state.isEdit && field.id == AppFieldSchema.FieldId.HANGOUT_NAME
                    FieldSchemaRouter(
                        field = field,
                        values = state.values,
                        disabled = isLocked,
                        onChange = { id, value ->
                            if (!isLocked) store.send(HangoutCreateAction.FieldChanged(id, value))
                        },
                        onAddCategory = { store.send(HangoutCreateAction.AddCategoryTapped) },
                        onRemoveCategory = { id -> store.send(HangoutCreateAction.RemoveCategory(id)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (isLocked) 0.5f else 1f)
                    )
                }
            }

            AppButton(
                title = "Continue",
                model = AppButtonModel(contentSize = ContentSize.Fill),
                onClick = { store.send(HangoutCreateAction.ContinueTapped) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                enabled = state.isValid
            )
        }

        AppTopBar(
            isScrolled = isScrolled,
            onBack = { store.send(HangoutCreateAction.BackTapped) }
        )
    }

    if (state.showCategoryPicker) {
        AppBottomSheet(
            onDismiss = { store.send(HangoutCreateAction.DismissCategoryPicker) },
            showDragHandle = false,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            SelectCategoryView(
                sections = state.categorySections,
                onToggle = { id -> store.send(HangoutCreateAction.CategoryToggled(id)) },
                onDone = { store.send(HangoutCreateAction.CategoryPickerDone) },
                onClose = { store.send(HangoutCreateAction.DismissCategoryPicker) }
            )
        }
    }
}
