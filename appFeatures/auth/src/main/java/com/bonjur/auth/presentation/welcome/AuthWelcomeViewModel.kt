package com.bonjur.auth.presentation.welcome

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeAction
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeSideEffect
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeViewState
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthWelcomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val dependencies: Dependencies
) : FeatureViewModel<AuthWelcomeViewState, AuthWelcomeAction, AuthWelcomeSideEffect>(
    AuthWelcomeViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: AuthUseCase,
        val storage: DefaultStorage
    )

    private lateinit var inputData: AuthWelcomeInputData

    fun init(inputData: AuthWelcomeInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        handle(AuthWelcomeAction.FetchData)
    }

    override fun handle(action: AuthWelcomeAction) {
        when (action) {
            AuthWelcomeAction.FetchData -> fetchData()
            AuthWelcomeAction.ContinueTapped -> continueTapped()
            AuthWelcomeAction.SkipTapped -> skipTapped()
        }
    }

    private fun fetchData() {
        val model = dependencies.useCase.welcome(name = inputData.name)
        updateState(state.copy(
            uiModel = model
        ))
    }

    private fun skipTapped() {
        dependencies.storage.saveBoolean(DefaultStorageKey.IS_AUTHENTICATED, true)
        viewModelScope.launch {
            navigator.navigateAndClearStack(AppScreens.Main.route)
        }
    }

    private fun continueTapped() {
        viewModelScope.launch {
            navigator.navigateAndClearStack(AuthScreens.Optionals.route)
        }
    }

    private fun dismiss() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }
}
