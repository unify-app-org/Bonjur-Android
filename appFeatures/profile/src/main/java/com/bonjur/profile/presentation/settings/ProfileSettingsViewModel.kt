package com.bonjur.profile.presentation.settings

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.manager.TokenManager
import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import com.bonjur.profile.presentation.settings.models.ProfileSettingsAction
import com.bonjur.profile.presentation.settings.models.ProfileSettingsInputData
import com.bonjur.profile.presentation.settings.models.ProfileSettingsSideEffect
import com.bonjur.profile.presentation.settings.models.ProfileSettingsViewState
import com.bonjur.profile.presentation.settings.models.SettingsItemModel
import com.bonjur.profile.presentation.settings.models.SettingsSectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ProfileSettingsViewState, ProfileSettingsAction, ProfileSettingsSideEffect>(
    ProfileSettingsViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ProfileUseCase,
        val tokenManager: TokenManager,
        val defaultStorage: DefaultStorage
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
            ProfileSettingsAction.LanguageTapped -> { /* navigate to language */ }
            ProfileSettingsAction.HelpCenterTapped -> { /* navigate to help center */ }
            ProfileSettingsAction.TermsTapped -> { /* navigate to terms */ }
            ProfileSettingsAction.DeleteAccountTapped -> deleteAccount()
            ProfileSettingsAction.LogOutTapped -> logOut()
            is ProfileSettingsAction.NotificationToggled ->
                updateState(state.copy(notificationsEnabled = action.isOn))
        }
    }

    private fun fetchData() {
        val sections = buildSections()
        updateState(state.copy(sections = sections))
    }

    private fun buildSections(): List<SettingsSectionModel> = listOf(
        SettingsSectionModel(
            title = null,
            items = listOf(
                SettingsItemModel(
                    id = "notifications",
                    title = "Notifications",
                    isSwitch = true,
                    action = null
                ),
                SettingsItemModel(
                    id = "language",
                    title = "Language",
                    action = ProfileSettingsAction.LanguageTapped
                )
            )
        ),
        SettingsSectionModel(
            title = "Support",
            items = listOf(
                SettingsItemModel(
                    id = "help",
                    title = "Help Center",
                    action = ProfileSettingsAction.HelpCenterTapped
                ),
                SettingsItemModel(
                    id = "terms",
                    title = "Terms & Conditions",
                    action = ProfileSettingsAction.TermsTapped
                )
            )
        ),
        SettingsSectionModel(
            title = null,
            items = listOf(
                SettingsItemModel(
                    id = "logout",
                    title = "Log out",
                    isDestructive = false,
                    action = ProfileSettingsAction.LogOutTapped
                ),
                SettingsItemModel(
                    id = "delete",
                    title = "Delete account",
                    isDestructive = true,
                    action = ProfileSettingsAction.DeleteAccountTapped
                )
            )
        )
    )

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            postEffect(ProfileSettingsSideEffect.Loading(true))
            try {
                dependencies.useCase.deleteAccount()
                navigator.navigateUp()
            } catch (e: Exception) {
                postEffect(ProfileSettingsSideEffect.Error(e.message ?: "Error", null))
            } finally {
                postEffect(ProfileSettingsSideEffect.Loading(false))
            }
        }
    }

    private fun logOut() {
        viewModelScope.launch {
            dependencies.tokenManager.clearTokens()
            dependencies.defaultStorage.saveBoolean(DefaultStorageKey.IS_AUTHENTICATED, false)
            navigator.navigateAndClearStack(AppScreens.Auth.route)
        }
    }
}
