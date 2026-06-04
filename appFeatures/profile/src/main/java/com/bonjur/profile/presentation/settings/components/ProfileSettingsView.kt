package com.bonjur.profile.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.profile.presentation.settings.models.ProfileSettingsAction
import com.bonjur.profile.presentation.settings.models.ProfileSettingsSideEffect
import com.bonjur.profile.presentation.settings.models.ProfileSettingsViewState
import com.bonjur.profile.presentation.settings.models.SettingsItemModel

@Composable
fun ProfileSettingsView(
    store: FeatureStore<ProfileSettingsViewState, ProfileSettingsAction, ProfileSettingsSideEffect>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary.copy(alpha = 0.3f)),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        store.state.sections.forEach { section ->
            if (section.title != null) {
                item {
                    Text(
                        text = section.title,
                        style = AppTypography.TextSm.medium,
                        color = Palette.blackMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Column {
                        section.items.forEachIndexed { index, item ->
                            SettingsRow(
                                item = item,
                                isNotificationsOn = store.state.notificationsEnabled,
                                onTap = { action ->
                                    if (action != null) store.send(action)
                                },
                                onToggle = { isOn ->
                                    store.send(ProfileSettingsAction.NotificationToggled(isOn))
                                }
                            )
                            if (index < section.items.lastIndex) {
                                HorizontalDivider(
                                    color = Palette.grayTeritary.copy(alpha = 0.3f),
                                    modifier = Modifier.padding(start = 56.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    item: SettingsItemModel,
    isNotificationsOn: Boolean,
    onTap: (ProfileSettingsAction?) -> Unit,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !item.isSwitch && item.versionText == null) {
                onTap(item.action)
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (item.isDestructive) Color.Red.copy(alpha = 0.1f)
                    else Palette.grayQuaternary,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = when (item.id) {
                    "notifications" -> Images.Icons.notification()
                    "language" -> Images.Icons.globe()
                    "help" -> Images.Icons.helpCircle()
                    "terms" -> Images.Icons.fileText()
                    "logout" -> Images.Icons.logOut()
                    "delete" -> Images.Icons.delete()
                    else -> Images.Icons.settings()
                },
                contentDescription = item.title,
                tint = if (item.isDestructive) Color.Red else Palette.blackHigh,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.title,
            style = AppTypography.TextMd.medium,
            color = if (item.isDestructive) Color.Red else Palette.blackHigh,
            modifier = Modifier.weight(1f)
        )

        when {
            item.isSwitch -> {
                Switch(
                    checked = isNotificationsOn,
                    onCheckedChange = onToggle
                )
            }
            item.versionText != null -> {
                Text(
                    text = item.versionText,
                    style = AppTypography.TextSm.regular,
                    color = Palette.blackMedium
                )
            }
            !item.isDestructive -> {
                Icon(
                    painter = Images.Icons.chevronRight(),
                    contentDescription = null,
                    tint = Palette.blackMedium,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
