package com.bonjur.auth.presentation.chooseUniversity

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.helper.MicrosoftAuthManager
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityAction
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityInputData
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversitySideEffect
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityViewState
import com.bonjur.auth.presentation.signIn.model.SignInInputData
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    /** Community ids that authenticate via Microsoft SSO instead of credentials. Mirrors iOS. */
    private val msalCommunityIds: Set<Int> = setOf(1)
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
            val selectedUniversity = state.uiModel.first { item -> item.selected }
            selectedCommunityId = selectedUniversity.id
            if (msalCommunityIds.contains(selectedUniversity.id)) {
                postEffect(ChooseUniversitySideEffect.LaunchMicrosoftSignIn)
            } else {
                navigator.navigateTo(
                    AuthScreens.SignIn.route,
                    SignInInputData(communityId = selectedUniversity.id)
                )
            }
        }
    }

    /** Triggered by the screen (it owns the Activity MSAL needs). Mirrors iOS `startMSALFlow`. */
    fun signInWithMicrosoft(activity: Activity) {
        viewModelScope.launch {
            postEffect(ChooseUniversitySideEffect.Loading(true))
            try {
                val result = dependencies.microsoftAuthManager.signIn(activity)
                val email = result.email
                if (result.error != null || email.isNullOrBlank()) {
                    postEffect(ChooseUniversitySideEffect.Error("Microsoft Sign In Failed"))
                    return@launch
                }
                val isFirstLogin = dependencies.useCase.login(
                    communityId = selectedCommunityId,
                    email = email,
                    password = null
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
                postEffect(ChooseUniversitySideEffect.Error(e.message ?: "Microsoft Sign In Failed"))
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
            try {
                val universities = dependencies.useCase.chooseUniversity()
                updateState ( state.copy(uiModel = universities) )
            } catch (e: Exception) {

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
