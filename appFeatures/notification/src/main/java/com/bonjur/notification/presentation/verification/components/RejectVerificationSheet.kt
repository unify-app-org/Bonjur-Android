package com.bonjur.notification.presentation.verification.components

import com.bonjur.designsystem.R as DesignR
import androidx.compose.ui.res.stringResource
import com.bonjur.notification.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.textView.TextView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.notification.domain.models.VerificationItem

private const val NOTE_LIMIT = 300

/**
 * Confirmation sheet for rejecting a club's verification request. Compose port
 * of iOS `RejectVerificationSheet` — replaces the old confirm alert and doubles
 * as the input for the optional note (`rejectionReason`) the club organiser reads.
 */
@Composable
fun RejectVerificationSheet(
    item: VerificationItem,
    onDismiss: () -> Unit,
    /** Called with the trimmed note, or null when the field was left blank. */
    onReject: (String?) -> Unit
) {
    var note by remember { mutableStateOf("") }

    AppBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.notif_reject_verification),
                style = AppTypography.HeadingMd.bold,
                color = Palette.black
            )

            Text(
                text = "You're rejecting ${item.clubName}'s verification request.",
                style = AppTypography.TextL.regular,
                color = Palette.graySecondary
            )

            Text(
                text = stringResource(R.string.notif_reject_note_label),
                style = AppTypography.BodyTextMd.medium,
                color = Palette.black
            )

            TextView(
                text = note,
                onTextChange = { note = it },
                characterLimit = NOTE_LIMIT,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            )

            Text(
                text = stringResource(R.string.notif_reject_note_hint),
                style = AppTypography.TextMd.regular,
                color = Palette.graySecondary
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppButton(
                    title = stringResource(DesignR.string.common_cancel),
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    model = AppButtonModel(
                        type = ButtonType.Secondary,
                        contentSize = ContentSize.Fill,
                        size = AppButtonSize.Medium
                    )
                )

                AppButton(
                    title = stringResource(R.string.notif_reject),
                    onClick = {
                        onReject(note.trim().ifEmpty { null })
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    model = AppButtonModel(
                        type = ButtonType.Destructive,
                        contentSize = ContentSize.Fill,
                        size = AppButtonSize.Medium
                    )
                )
            }
        }
    }
}
