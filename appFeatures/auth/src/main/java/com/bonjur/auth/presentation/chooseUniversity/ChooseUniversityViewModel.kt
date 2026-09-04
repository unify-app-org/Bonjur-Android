package com.bonjur.auth.presentation.chooseUniversity

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.bonjur.auth.R
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.helper.MicrosoftAuthManager
import com.bonjur.auth.helper.MsalSignInCancelled
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityAction
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityInputData
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversitySideEffect
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityViewState
import com.bonjur.auth.presentation.chooseUniversity.model.CommunitiesPhase
import com.bonjur.auth.presentation.signIn.model.SignInInputData
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bonjur.network.model.userMessage

@HiltViewModel
class ChooseUniversityViewModel @Inject constructor(
    private val dependencies: Dependencies,
    private val navigator: Navigator
) : FeatureViewModel<ChooseUniversityViewState, ChooseUniversityAction, ChooseUniversitySideEffect>(
    ChooseUniversityViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: AuthUseCase,
        val microsoftAuthManager: MicrosoftAuthManager
    )

    /**
     * Communities that authenticate via Microsoft SSO instead of credentials, keyed by
     * community NAME — the ids are assigned by the backend and are not stable across
     * environments, so matching on them routed the wrong community into the MSAL flow.
     * Mirrors iOS `SignInFlowCoordinator.msalCommunityIds`.
     */
    private val msalCommunityNames: Set<String> = setOf("UFAZ")
    private var selectedCommunityId: Int = 0

    private lateinit var inputData: ChooseUniversityInputData

    fun init(inputData: ChooseUniversityInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        fetchData()
    }

    override fun handle(action: ChooseUniversityAction) {
        when (action) {
            is ChooseUniversityAction.FetchData -> fetchData()
            is ChooseUniversityAction.SelectedCell -> selectCell(action.index)
            is ChooseUniversityAction.Dismiss -> dismiss()
            is ChooseUniversityAction.NextTapped -> nextTapped()
        }
    }

    private fun nextTapped() {
        viewModelScope.launch {
            val selectedUniversity = state.uiModel.firstOrNull { item -> item.selected } ?: return@launch
            selectedCommunityId = selectedUniversity.id
            if (usesMicrosoftSignIn(selectedUniversity.title)) {
                postEffect(ChooseUniversitySideEffect.LaunchMicrosoftSignIn)
            } else {
                navigator.navigateTo(
                    AuthScreens.SignIn.route,
                    SignInInputData(
                        communityId = selectedUniversity.id,
                        communityName = selectedUniversity.title
                    )
                )
            }
        }
    }

    private fun usesMicrosoftSignIn(communityName: String): Boolean =
        msalCommunityNames.any { it.equals(communityName.trim(), ignoreCase = true) }

    fun signInWithMicrosoft(activity: Activity) {
        viewModelScope.launch {
            // Held only until the Microsoft UI takes over the screen — the overlay is
            // invisible behind it and a count that spans another activity can be left
            // stranded. The return leg re-shows it below.
            postEffect(ChooseUniversitySideEffect.Loading(true))
            val result = try {
                dependencies.microsoftAuthManager.signIn(activity)
            } finally {
                postEffect(ChooseUniversitySideEffect.Loading(false))
            }

            if (result.error is MsalSignInCancelled) return@launch

            val email = result.email
            if (result.error != null || email.isNullOrBlank()) {
                postEffect(ChooseUniversitySideEffect.Error(LanguageManager.string(R.string.auth_microsoft_failed)))
                return@launch
            }

            // Microsoft has redirected back into Unify and the token exchange starts
            // now: keep the app under a loading overlay the whole way to the dashboard
            // instead of flashing the community list.
            postEffect(ChooseUniversitySideEffect.Loading(true))
            try {
                val isFirstLogin = dependencies.useCase.login(
                    communityId = selectedCommunityId,
                    email = email,
                    password = null,
                    idToken = result.idToken
                )
                if (isFirstLogin) {
                    navigator.navigateTo(
                        AuthScreens.Welcome.route,
                        AuthWelcomeInputData(email.substringBefore("@"))
                    )
                } else {
                    navigator.navigateAndClearStack(AppScreens.Main.route)
                }
            } catch (e: Exception) {
                postEffect(ChooseUniversitySideEffect.Error(e.userMessage()))
            } finally {
                postEffect(ChooseUniversitySideEffect.Loading(false))
            }
        }
    }

    private fun dismiss() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateState(state.copy(phase = CommunitiesPhase.LOADING))
            try {
                val universities = dependencies.useCase.chooseUniversity()
                updateState(
                    state.copy(uiModel = universities, phase = CommunitiesPhase.LOADED)
                )
            } catch (e: Exception) {
                // No hardcoded fallback list — an empty screen with a retry beats
                // signing the user into a community that isn't theirs.
                updateState(state.copy(uiModel = emptyList(), phase = CommunitiesPhase.FAILED))
            }
        }
    }

    private fun selectCell(index: Int) {
        updateState (
            state.copy(
                uiModel = this.state.uiModel.mapIndexed { i, item ->
                    item.copy(selected = i == index)
                },
                enabled = true
            )
        )
    }
}
