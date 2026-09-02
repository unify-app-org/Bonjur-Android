package com.bonjur.designSystem.components.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.localization.AppLocalizationProvider
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * Reusable modal bottom sheet. Compose equivalent of iOS `AppBottomSheet` / `appSheet`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    showDragHandle: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Palette.white,
        scrimColor = Color.Black.copy(alpha = 0.3f),
        dragHandle = if (showDragHandle) {
            { androidx.compose.material3.BottomSheetDefaults.DragHandle() }
        } else null
    ) {
        // `ModalBottomSheet` renders in its own dialog window, whose ComposeView
        // re-provides `LocalContext` from that window — which throws away the
        // localized context `AppLocalizationProvider` installed at the app root, so
        // every `stringResource` inside a sheet fell back to the device language
        // (the club verify sheet came up English under a Russian app). Re-provide it
        // here so sheet content localizes like everything else; call sites keep using
        // plain `stringResource`.
        AppLocalizationProvider {
            Column(modifier = modifier, content = content)
        }
    }
}
