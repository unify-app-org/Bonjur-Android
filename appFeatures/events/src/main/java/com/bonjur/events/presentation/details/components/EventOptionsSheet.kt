package com.bonjur.events.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * Event 3-dot options sheet: Report event / Leave event / Share.
 * Compose port of iOS `EventOptionsSheet`. Pure UI — visibility is decided from
 * [viewerRole]; the exit confirmation lives in `EventDetailsViewModel` (events
 * have no owner-transfer gate). This sheet only renders rows and delegates out.
 *
 * NOTE: Report and Share are "Coming soon" — the report flow (iOS
 * `ActivityReportScreen`) has no Android screen yet. Mirrors the Clubs port.
 */
private val DestructiveRed = Color(0xFFE5484D)

@Composable
fun EventOptionsSheet(
    viewerRole: AppUIEntities.UserActivityRole,
    onExit: () -> Unit,
    onDismiss: () -> Unit
) {
    // Leave shows for joined members; Report shows for everyone but the creator
    // (you can't report your own event).
    val showExit = viewerRole != AppUIEntities.UserActivityRole.NOT_JOINED
    val showReport = viewerRole != AppUIEntities.UserActivityRole.PRESIDENT &&
        viewerRole != AppUIEntities.UserActivityRole.EVENT_CREATOR

    AppBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            if (showReport) {
                EventOptionRow(
                    title = "Report event",
                    tint = DestructiveRed,
                    trailing = "Coming soon",
                    enabled = false
                ) {}
                RowDivider()
            }

            if (showExit) {
                EventOptionRow(
                    title = "Leave event",
                    tint = DestructiveRed
                ) {
                    onDismiss()
                    onExit()
                }
                RowDivider()
            }

            EventOptionRow(
                title = "Share",
                tint = Palette.blackMedium,
                trailing = "Coming soon",
                enabled = false
            ) {}
        }
    }
}

@Composable
private fun EventOptionRow(
    title: String,
    tint: Color,
    trailing: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (enabled) it.clickable(onClick = onClick) else it }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = AppTypography.TextL.medium,
            color = tint
        )
        if (trailing != null) {
            Text(
                text = trailing,
                style = AppTypography.TextMd.regular,
                color = Palette.blackMedium
            )
        }
    }
}

@Composable
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Palette.grayTeritary.copy(alpha = 0.6f))
    )
}
