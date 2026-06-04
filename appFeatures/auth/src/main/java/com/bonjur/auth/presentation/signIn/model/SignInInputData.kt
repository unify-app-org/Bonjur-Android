package com.bonjur.auth.presentation.signIn.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// -------- Input Data --------
data class SignInInputData(
    val communityId: Int = 0,
    val initialValue: String = ""
)

// -------- Side Effect --------
sealed class SignInSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : SignInSideEffect()
    data class Error(val message: String) : SignInSideEffect()
}

// -------- View State --------
data class SignInViewState(
    val email: String = "",
    val password: String = ""
) : FeatureState

// -------- Actions --------
sealed class SignInAction : FeatureAction {
    object SignIn : SignInAction()
    object Dismiss : SignInAction()
    object FetchData : SignInAction()
    data class EmailChanged(val email: String) : SignInAction()
    data class PasswordChanged(val password: String) : SignInAction()
}
