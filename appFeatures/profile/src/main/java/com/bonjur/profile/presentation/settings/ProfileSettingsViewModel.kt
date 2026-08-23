package com.bonjur.profile.presentation.settings

import com.bonjur.profile.R
import com.bonjur.designsystem.R as DesignR
import androidx.lifecycle.viewModelScope
import com.bonjur.designSystem.components.alert.AppAlert
import com.bonjur.designSystem.components.alert.AppAlertPresenter
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.manager.TokenManager
import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import com.bonjur.storage.notification.NotificationPreferences
import com.bonjur.profile.presentation.settings.models.ProfileSettingsAction
import com.bonjur.profile.presentation.settings.models.ProfileSettingsInputData
import com.bonjur.profile.presentation.settings.models.ProfileSettingsSideEffect
import com.bonjur.profile.presentation.settings.models.ProfileSettingsViewState
import com.bonjur.profile.presentation.settings.models.SettingsItemModel
import com.bonjur.profile.presentation.settings.models.SettingsSectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bonjur.network.model.userMessage

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ProfileSettingsViewState, ProfileSettingsAction, ProfileSettingsSideEffect>(
    ProfileSettingsViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ProfileUseCase,
        val tokenManager: TokenManager,
        val defaultStorage: DefaultStorage,
        val notificationPreferences: NotificationPreferences,
        /**
         * The Hilt @Singleton [Navigator] that drives the ROOT NavHost (the one
         * holding authNavGraph). Logout is an app-level transition to
         * [AppScreens.Auth] — a route the per-tab navigator (passed via [init])
         * can't reach, so dispatching it there crashes ("destination ... cannot
         * be found"). App-level nav must go through the root navigator.
         */
        val rootNavigator: Navigator
    )

    private lateinit var inputData: ProfileSettingsInputData
    private lateinit var navigator: Navigator

    fun init(inputData: ProfileSettingsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        handle(ProfileSettingsAction.FetchData)
    }

    override fun handle(action: ProfileSettingsAction) {
        when (action) {
            ProfileSettingsAction.FetchData -> fetchData()
            ProfileSettingsAction.BackTapped -> navigateBack()
            ProfileSettingsAction.LanguageTapped ->
                updateState(state.copy(showLanguagePicker = true))
            ProfileSettingsAction.DismissLanguagePicker ->
                updateState(state.copy(showLanguagePicker = false))
            is ProfileSettingsAction.LanguageSelected -> {
                // Switches live — every screen re-reads its strings, no restart.
                LanguageManager.select(action.language)
                updateState(state.copy(showLanguagePicker = false, sections = buildSections()))
            }
            ProfileSettingsAction.HelpCenterTapped -> { /* no destination yet — same on iOS */ }
            ProfileSettingsAction.TermsTapped -> { /* no destination yet — same on iOS */ }
            ProfileSettingsAction.DeleteAccountTapped -> confirmDeleteAccount()
            ProfileSettingsAction.LogOutTapped -> confirmLogOut()
            is ProfileSettingsAction.NotificationToggled -> toggleNotifications(action.isOn)
        }
    }

    /**
     * Also re-run on every ON_RESUME: the OS notification grant can change while the
     * user is away in system settings, and the switch has to come back agreeing with it.
     */
    private fun fetchData() {
        updateState(
            state.copy(
                sections = buildSections(),
                notificationsEnabled = dependencies.notificationPreferences.isEnabled
            )
        )
    }

    /**
     * iOS calls `register/unregisterForRemoteNotifications()` here. Android apps can't
     * grant or revoke their own notification permission, so:
     * - the in-app mute is always honoured (the FCM service drops messages while it's off);
     * - turning the switch ON while the OS still blocks notifications sends the user to the
     *   system page, and the switch stays off until they actually allow it (re-read on resume).
     */
    private fun toggleNotifications(isOn: Boolean) {
        val preferences = dependencies.notificationPreferences
        preferences.isEnabledInApp = isOn
        if (isOn && !preferences.isEnabledInSystem) {
            preferences.openSystemSettings()
        }
        updateState(state.copy(notificationsEnabled = preferences.isEnabled))
    }

    // Mirrors iOS ProfileDataSource.fetchSections: two untitled sections.
    // Log out is the destructive (red) row, Delete account is normal — matches iOS.
    private fun buildSections(): List<SettingsSectionModel> = listOf(
        SettingsSectionModel(
            title = null,
            items = listOf(
                SettingsItemModel(
                    id = "notifications",
                    title = LanguageManager.string(R.string.settings_notification),
                    iconRes = DesignR.drawable.ic_bell,
                    isSwitch = true,
                    action = null
                ),
                SettingsItemModel(
                    id = "language",
                    title = LanguageManager.string(R.string.settings_language),
                    iconRes = DesignR.drawable.ic_globe,
                    action = ProfileSettingsAction.LanguageTapped
                ),
                SettingsItemModel(
                    id = "help",
                    title = LanguageManager.string(R.string.settings_help_center),
                    iconRes = DesignR.drawable.ic_help_circle,
                    action = ProfileSettingsAction.HelpCenterTapped
                ),
                SettingsItemModel(
                    id = "terms",
                    title = LanguageManager.string(R.string.settings_terms),
                    iconRes = DesignR.drawable.ic_clipboard_list,
                    action = ProfileSettingsAction.TermsTapped
                ),
                SettingsItemModel(
                    id = "version",
                    title = LanguageManager.string(R.string.settings_app_version),
                    iconRes = DesignR.drawable.ic_phone,
                    versionText = APP_VERSION,
                    action = null
                )
            )
        ),
        SettingsSectionModel(
            title = null,
            items = listOf(
                SettingsItemModel(
                    id = "delete",
                    title = LanguageManager.string(R.string.settings_delete_account),
                    iconRes = DesignR.drawable.ic_trash,
                    isDestructive = false,
                    action = ProfileSettingsAction.DeleteAccountTapped
                ),
                SettingsItemModel(
                    id = "logout",
                    title = LanguageManager.string(R.string.settings_logout),
                    iconRes = DesignR.drawable.ic_logout,
                    isDestructive = true,
                    action = ProfileSettingsAction.LogOutTapped
                )
            )
        )
    )

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }

    /** Both destructive rows go through a confirm alert, as on iOS (`showConfirmationAlert`). */
    private fun confirmDeleteAccount() = presentConfirmation(
        title = LanguageManager.string(R.string.settings_delete_title),
        subtitle = LanguageManager.string(R.string.settings_delete_subtitle),
        confirmTitle = LanguageManager.string(R.string.settings_delete_confirm),
        onConfirm = ::deleteAccount
    )

    private fun confirmLogOut() = presentConfirmation(
        title = LanguageManager.string(R.string.settings_logout_title),
        subtitle = LanguageManager.string(R.string.settings_logout_subtitle),
        confirmTitle = LanguageManager.string(R.string.settings_logout_confirm),
        onConfirm = ::logOut
    )

    private fun presentConfirmation(
        title: String,
        subtitle: String,
        confirmTitle: String,
        onConfirm: () -> Unit
    ) {
        AppAlertPresenter.present(
            AppAlert(
                config = AppAlert.Config(title = title, subtitle = subtitle),
                actions = listOf(
                    AppAlert.Action(
                        title = LanguageManager.string(DesignR.string.common_cancel),
                        style = AppAlert.Action.Style.PRIMARY
                    ),
                    AppAlert.Action(
                        title = confirmTitle,
                        style = AppAlert.Action.Style.DESTRUCTIVE,
                        handler = onConfirm
                    )
                )
            )
        )
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            postEffect(ProfileSettingsSideEffect.Loading(true))
            try {
                dependencies.useCase.deleteAccount()
                finishSession()
            } catch (e: Exception) {
                postEffect(ProfileSettingsSideEffect.Error(e.userMessage()))
            } finally {
                postEffect(ProfileSettingsSideEffect.Loading(false))
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch { finishSession() }
    }

    /**
     * iOS routes both delete and logout to `.finishSession` → `delegate.logout()`. Delete
     * used to only `navigateUp()` on Android, leaving the deleted user signed in on the
     * profile tab.
     */
    private suspend fun finishSession() {
        dependencies.tokenManager.clearTokens()
        dependencies.defaultStorage.saveBoolean(DefaultStorageKey.IS_AUTHENTICATED, false)
        // Root navigator, not the per-tab one — Auth lives in the root graph.
        dependencies.rootNavigator.navigateAndClearStack(AppScreens.Auth.route)
    }

    private companion object {
        const val APP_VERSION = "1.24.0"
    }
}
