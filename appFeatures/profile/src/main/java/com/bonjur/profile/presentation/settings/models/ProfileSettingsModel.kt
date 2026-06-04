package com.bonjur.profile.presentation.settings.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// MARK: - ProfileSettings input
data class ProfileSettingsInputData(
    val dummy: Unit = Unit
)

// MARK: - Side effects
sealed class ProfileSettingsSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ProfileSettingsSideEffect()
    data class Error(val title: String, val message: String?) : ProfileSettingsSideEffect()
}

data class SettingsItemModel(
    val id: String,
    val title: String,
    val iconRes: Int? = null,
    val isDestructive: Boolean = false,
    val isSwitch: Boolean = false,
    val versionText: String? = null,
    val action: ProfileSettingsAction? = null
)

data class SettingsSectionModel(
    val title: String?,
    val items: List<SettingsItemModel>
)

// MARK: - View State
data class ProfileSettingsViewState(
    val sections: List<SettingsSectionModel> = emptyList(),
    val notificationsEnabled: Boolean = true
) : FeatureState

// MARK: - Feature Action
sealed class ProfileSettingsAction : FeatureAction {
    object FetchData : ProfileSettingsAction()
    object BackTapped : ProfileSettingsAction()
    object LanguageTapped : ProfileSettingsAction()
    object HelpCenterTapped : ProfileSettingsAction()
    object TermsTapped : ProfileSettingsAction()
    object DeleteAccountTapped : ProfileSettingsAction()
    object LogOutTapped : ProfileSettingsAction()
    data class NotificationToggled(val isOn: Boolean) : ProfileSettingsAction()
}
