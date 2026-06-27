package com.bonjur.auth.presentation.signIn

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.auth.presentation.signIn.model.SignInAction
import com.bonjur.auth.presentation.signIn.model.SignInInputData
import com.bonjur.auth.presentation.signIn.model.SignInSideEffect
import com.bonjur.auth.presentation.signIn.model.SignInViewState
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val useCase: AuthUseCase,
    private val navigator: Navigator
) : FeatureViewModel<SignInViewState, SignInAction, SignInSideEffect>(
    SignInViewState()
) {

    private lateinit var inputData: SignInInputData

    fun init(inputData: SignInInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
    }

    override fun handle(action: SignInAction) {
        when (action) {
            SignInAction.FetchData -> { /* no-op */ }
            is SignInAction.EmailChanged -> updateState(state.copy(email = action.email))
            is SignInAction.PasswordChanged -> updateState(state.copy(password = action.password))
            SignInAction.SignIn -> signIn()
            SignInAction.Dismiss -> viewModelScope.launch { navigator.navigateUp() }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            postEffect(SignInSideEffect.Loading(true))
            try {
                val isFirstLogin = useCase.login(
                    communityId = inputData.communityId,
                    email = state.email,
                    password = state.password,
                    idToken = null
                )
                if (isFirstLogin) {
                    navigator.navigateTo(
                        AuthScreens.Welcome.route,
                        AuthWelcomeInputData(state.email.substringBefore("@"))
                    )
                } else {
                    navigator.navigateAndClearStack(AppScreens.Main.route)
                }
            } catch (e: Exception) {
                postEffect(SignInSideEffect.Error(e.message ?: "Login failed"))
            } finally {
                postEffect(SignInSideEffect.Loading(false))
            }
        }
    }
}
