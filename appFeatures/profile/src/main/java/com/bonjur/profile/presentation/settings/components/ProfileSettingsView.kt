package com.bonjur.profile.presentation.settings.components

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.bonjur.profile.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.designSystem.localization.LanguageSelectionView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.profile.presentation.settings.models.ProfileSettingsAction
import com.bonjur.profile.presentation.settings.models.ProfileSettingsSideEffect
import com.bonjur.profile.presentation.settings.models.ProfileSettingsViewState
import com.bonjur.profile.presentation.settings.models.SettingsItemModel

private val SectionSpacing = 16.dp
private val ScreenPadding = 16.dp
private val RowIconSize = 40.dp

@Composable
fun ProfileSettingsView(
    store: FeatureStore<ProfileSettingsViewState, ProfileSettingsAction, ProfileSettingsSideEffect>
) {
    // The notification switch mirrors an OS-level grant the user can change from the system
    // settings page we send them to, so re-read it every time the screen comes back.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                store.send(ProfileSettingsAction.FetchData)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Solid grouped-list grey. This used to be `grayQuaternary.copy(alpha = 0.3f)`,
            // which composites to ~#FBFBFB over the white host — 4/255 away from the white
            // cards sitting on it, so the cards had no visible edge at all.
            .background(Palette.grayQuaternary)
    ) {
        // No hero/cover header here, so the bar is always in its solid state (same as the
        // notification feed). Left transparent it drew a `whiteMedium` back button onto a
        // near-white page, which is what made the button all but disappear.
        AppTopBar(
            isScrolled = true,
            showTitle = true,
            title = stringResource(R.string.settings_title),
            onBack = { store.send(ProfileSettingsAction.BackTapped) }
        )
        SettingsList(store)
    }

    if (store.state.showLanguagePicker) {
        AppBottomSheet(onDismiss = { store.send(ProfileSettingsAction.DismissLanguagePicker) }) {
            LanguageSelectionView { language ->
                store.send(ProfileSettingsAction.LanguageSelected(language))
            }
        }
    }
}

@Composable
private fun SettingsList(
    store: FeatureStore<ProfileSettingsViewState, ProfileSettingsAction, ProfileSettingsSideEffect>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = SectionSpacing),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing)
    ) {
        store.state.sections.forEach { section ->
            if (section.title != null) {
                item {
                    Text(
                        text = section.title,
                        style = AppTypography.TextSm.medium,
                        color = Palette.blackMedium,
                        modifier = Modifier.padding(horizontal = ScreenPadding)
                    )
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Palette.white),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.padding(horizontal = ScreenPadding)
                ) {
                    Column {
                        section.items.forEachIndexed { index, item ->
                            SettingsRow(
                                item = item,
                                isNotificationsOn = store.state.notificationsEnabled,
                                onTap = { action -> action?.let(store::send) },
                                onToggle = { isOn ->
                                    store.send(ProfileSettingsAction.NotificationToggled(isOn))
                                }
                            )
                            if (index < section.items.lastIndex) {
                                HorizontalDivider(
                                    color = Palette.grayTeritary.copy(alpha = 0.3f),
                                    modifier = Modifier.padding(start = 72.dp)
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
    val contentColor = if (item.isDestructive) Palette.destructiveRed else Palette.blackHigh

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.action != null) { onTap(item.action) }
            .padding(horizontal = ScreenPadding, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(RowIconSize)
                .background(
                    color = if (item.isDestructive) Palette.destructiveRed.copy(alpha = 0.1f)
                    else Palette.grayQuaternary,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(ScreenPadding))

        Text(
            text = item.title,
            style = AppTypography.TextMd.medium,
            color = contentColor,
            modifier = Modifier.weight(1f)
        )

        when {
            item.isSwitch -> Switch(
                checked = isNotificationsOn,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Palette.white,
                    checkedTrackColor = Palette.secondary,
                    checkedBorderColor = Palette.secondary
                )
            )

            item.versionText != null -> Text(
                text = item.versionText,
                style = AppTypography.TextSm.regular,
                color = Palette.blackMedium
            )

            else -> Icon(
                painter = Images.Icons.chevronRight(),
                contentDescription = null,
                tint = Palette.blackMedium,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
