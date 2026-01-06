package com.bonjur.auth.presentation.signIn.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// -------- Input Data --------
data class SignInInputData(
    val initialValue: String = ""
)

// -------- Side Effect --------
sealed class SignInSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : SignInSideEffect()
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
}
