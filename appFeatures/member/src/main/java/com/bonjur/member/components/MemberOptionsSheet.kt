package com.bonjur.member.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.member.policy.ReportReason
import com.bonjur.member.policy.assignSubtitle
import com.bonjur.member.policy.assignTitle

/**
 * Input for the shared member 3-dot options sheet. Built by each activity's
 * detail layer from `MemberOptionsPolicy`. Mirrors iOS `MemberOptionsInput`.
 */
data class MemberOptionsInput(
    val memberName: String,
    val currentRole: AppUIEntities.UserActivityRole,
    val assignableRoles: List<AppUIEntities.UserActivityRole>,
    val showChangeRole: Boolean,
    val showReport: Boolean,
    val onAssignRole: (AppUIEntities.UserActivityRole) -> Unit,
    val onReport: (ReportReason) -> Unit
)

private enum class SheetScreen { MENU, ASSIGN_ROLE, REPORT }

/**
 * Shared member options sheet: Change role / Report user / Share.
 * Compose port of iOS `MemberOptionsSheet` — menu root, an assign-role screen
 * (radio cards + Cancel/Confirm) and a report screen (radio rows + Report submit).
 */
@Composable
fun MemberOptionsSheet(
    input: MemberOptionsInput,
    onDismiss: () -> Unit
) {
    var screen by remember { mutableStateOf(SheetScreen.MENU) }

    AppBottomSheet(onDismiss = onDismiss) {
        when (screen) {
            SheetScreen.MENU -> MenuScreen(
                input = input,
                onChangeRole = { screen = SheetScreen.ASSIGN_ROLE },
                onReport = { screen = SheetScreen.REPORT }
            )

            SheetScreen.ASSIGN_ROLE -> AssignRoleScreen(
                input = input,
                onBack = { screen = SheetScreen.MENU },
                onConfirm = { role ->
                    input.onAssignRole(role)
                    onDismiss()
                }
            )

            SheetScreen.REPORT -> ReportScreen(
                onBack = { screen = SheetScreen.MENU },
                onSubmit = { reason ->
                    input.onReport(reason)
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun MenuScreen(
    input: MemberOptionsInput,
    onChangeRole: () -> Unit,
    onReport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = input.memberName,
            style = AppTypography.HeadingMd.bold,
            color = Palette.black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (input.showChangeRole) {
            MenuRow(title = "Change role", tint = Palette.black, onClick = onChangeRole)
            MenuDivider()
        }
        if (input.showReport) {
            MenuRow(title = "Report user", tint = Palette.appBlue, onClick = onReport)
            MenuDivider()
        }
        MenuRow(
            title = "Share",
            tint = Palette.graySecondary,
            trailing = "Coming soon",
            enabled = false,
            onClick = {}
        )
    }
}

@Composable
private fun MenuRow(
    title: String,
    tint: androidx.compose.ui.graphics.Color,
    trailing: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (enabled) it.clickable(onClick = onClick) else it }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = AppTypography.BodyTextMd.medium, color = tint)
        Spacer(modifier = Modifier.weight(1f))
        if (trailing != null) {
            Text(
                text = trailing,
                style = AppTypography.TextMd.regular,
                color = Palette.graySecondary
            )
        }
    }
}

@Composable
private fun MenuDivider() {
    Divider(
        color = Palette.grayTeritary.copy(alpha = 0.6f),
        thickness = 0.5.dp
    )
}

@Composable
private fun AssignRoleScreen(
    input: MemberOptionsInput,
    onBack: () -> Unit,
    onConfirm: (AppUIEntities.UserActivityRole) -> Unit
) {
    var selected by remember { mutableStateOf(input.currentRole) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SheetHeader(title = "Assign role", onBack = onBack)

        Text(
            text = "Choose a role for this member",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.graySecondary
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            input.assignableRoles.forEach { role ->
                RoleCard(
                    title = role.assignTitle,
                    subtitle = role.assignSubtitle,
                    isSelected = role == selected,
                    onClick = { selected = role }
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                title = "Cancel",
                onClick = onBack,
                modifier = Modifier.weight(1f),
                model = AppButtonModel(
                    type = ButtonType.Secondary,
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Medium
                )
            )
            AppButton(
                title = "Confirm",
                onClick = { onConfirm(selected) },
                modifier = Modifier.weight(1f),
                model = AppButtonModel(
                    type = ButtonType.Primary,
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Medium
                ),
                enabled = selected != input.currentRole
            )
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Palette.white, RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 1.dp else 0.5.dp,
                color = if (isSelected) Palette.black else Palette.grayTeritary.copy(alpha = 0.7f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioCircle(isSelected = isSelected)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, style = AppTypography.BodyTextMd.bold, color = Palette.black)
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = AppTypography.TextMd.regular,
                    color = Palette.graySecondary
                )
            }
        }
    }
}

@Composable
private fun ReportScreen(
    onBack: () -> Unit,
    onSubmit: (ReportReason) -> Unit
) {
    var selected by remember { mutableStateOf(ReportReason.FAKE_PROFILE) }
    val reasons = ReportReason.entries

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        SheetHeader(title = "Report user", onBack = onBack)

        Column(
            modifier = Modifier
                .heightIn(max = 420.dp)
                .verticalScroll(rememberScrollState())
        ) {
            reasons.forEachIndexed { index, reason ->
                ReportReasonRow(
                    title = reason.displayTitle,
                    isSelected = reason == selected,
                    showsDivider = index != reasons.lastIndex,
                    onClick = { selected = reason }
                )
            }
        }

        AppButton(
            title = "Report",
            onClick = { onSubmit(selected) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            model = AppButtonModel(
                type = ButtonType.Destructive,
                contentSize = ContentSize.Fill,
                size = AppButtonSize.Medium
            )
        )
    }
}

@Composable
private fun ReportReasonRow(
    title: String,
    isSelected: Boolean,
    showsDivider: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioCircle(isSelected = isSelected)
            Text(text = title, style = AppTypography.BodyTextMd.regular, color = Palette.black)
        }
        if (showsDivider) {
            Divider(color = Palette.grayTeritary.copy(alpha = 0.5f), thickness = 0.5.dp)
        }
    }
}

@Composable
private fun RadioCircle(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 1.5.dp,
                color = if (isSelected) Palette.black else Palette.graySecondary,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Palette.black, CircleShape)
            )
        }
    }
}

@Composable
private fun SheetHeader(title: String, onBack: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                painter = Images.Icons.xmark(),
                contentDescription = "Back",
                tint = Palette.black,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(text = title, style = AppTypography.HeadingMd.bold, color = Palette.black)
    }
}
