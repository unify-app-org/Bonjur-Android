package com.bonjur.designSystem.components.alert

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ButtonStyle
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun AppAlertView(
    alert: AppAlert,
    onDismiss: (handler: (() -> Unit)?) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Palette.white,
        border = BorderStroke(0.5.dp, Palette.grayTeritary.copy(alpha = 0.4f)),
        modifier = modifier.widthIn(max = 360.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TextSection(config = alert.config)
            ActionsSection(
                actions = alert.actions,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun TextSection(config: AppAlert.Config) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = config.title,
            style = AppTypography.TitleSm.bold,
            color = Palette.black,
            textAlign = TextAlign.Start
        )

        if (!config.subtitle.isNullOrEmpty()) {
            Text(
                text = config.subtitle,
                style = AppTypography.BodyTextMd.regular,
                color = Palette.blackMedium,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun ActionsSection(
    actions: List<AppAlert.Action>,
    onDismiss: (handler: (() -> Unit)?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        actions.forEach { action ->
            AppButton(
                title = action.title,
                model = buttonModel(action.style),
                onClick = { onDismiss(action.handler) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun AppAlertPreview() {
    AppAlertView(
        alert = AppAlert(
            config = AppAlert.Config(
                title = "Delete this item?",
                subtitle = "This action cannot be undone. Are you sure you want to continue?"
            ),
            actions = listOf(
                AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.SECONDARY),
                AppAlert.Action(title = "Delete", style = AppAlert.Action.Style.DESTRUCTIVE)
            )
        ),
        onDismiss = {},
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun AppAlertPrimaryPreview() {
    AppAlertView(
        alert = AppAlert(
            config = AppAlert.Config(
                title = "Save changes?",
                subtitle = "You have unsaved changes that will be lost."
            ),
            actions = listOf(
                AppAlert.Action(title = "Discard", style = AppAlert.Action.Style.SECONDARY),
                AppAlert.Action(title = "Save", style = AppAlert.Action.Style.PRIMARY)
            )
        ),
        onDismiss = {},
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF888888)
@Composable
private fun AppAlertNoSubtitlePreview() {
    AppAlertView(
        alert = AppAlert(
            config = AppAlert.Config(title = "Are you sure?"),
            actions = listOf(
                AppAlert.Action(title = "No", style = AppAlert.Action.Style.SECONDARY),
                AppAlert.Action(title = "Yes", style = AppAlert.Action.Style.PRIMARY)
            )
        ),
        onDismiss = {},
        modifier = Modifier.padding(16.dp)
    )
}

private fun buttonModel(style: AppAlert.Action.Style): AppButtonModel {
    return when (style) {
        AppAlert.Action.Style.PRIMARY -> AppButtonModel(
            type = ButtonType.Primary,
            style = ButtonStyle.Hover,
            contentSize = ContentSize.Fill,
            size = AppButtonSize.Large
        )
        AppAlert.Action.Style.SECONDARY -> AppButtonModel(
            type = ButtonType.Tertiary,
            contentSize = ContentSize.Fill,
            size = AppButtonSize.Large
        )
        AppAlert.Action.Style.DESTRUCTIVE -> AppButtonModel(
            type = ButtonType.Destructive,
            contentSize = ContentSize.Fill,
            size = AppButtonSize.Large
        )
    }
}
