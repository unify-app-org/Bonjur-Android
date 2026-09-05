package com.bonjur.auth.presentation.signIn.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.utils.isValidEmail

// -------- Input Data --------
data class SignInInputData(
    val communityId: Int = 0,
    /** Display name of the chosen community. Mirrors iOS `SignInInputData.communityName`. */
    val communityName: String = "",
    val initialValue: String = ""
)

// -------- Side Effect --------
sealed class SignInSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : SignInSideEffect()
    data class Error(val message: String?) : SignInSideEffect()
}

// -------- View State --------
data class SignInViewState(
    val email: String = "",
    val password: String = ""
) : FeatureState {
    /** Gates the Sign in button. Android had no gate at all: the button sat enabled
     *  over two empty fields and the tap went to the API.
     *
     *  Stricter than iOS on purpose (decided 2026-09-06): iOS only checks both fields
     *  are non-empty, Android also format-checks the address. So an address Android
     *  refuses can still be submitted on iOS — a deliberate divergence, not drift. */
    val isValid: Boolean
        get() = email.isValidEmail() && password.isNotBlank()

    /** Only once there is something to be wrong about — an empty field is not an error,
     *  it is an unfinished one, and flagging it as the user starts typing is noise. */
    val showEmailError: Boolean
        get() = email.isNotBlank() && !email.isValidEmail()
}

// -------- Actions --------
sealed class SignInAction : FeatureAction {
    object SignIn : SignInAction()
    object Dismiss : SignInAction()
    object FetchData : SignInAction()
    data class EmailChanged(val email: String) : SignInAction()
    data class PasswordChanged(val password: String) : SignInAction()
}
