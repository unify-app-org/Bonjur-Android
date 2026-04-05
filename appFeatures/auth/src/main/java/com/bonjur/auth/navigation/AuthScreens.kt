package com.bonjur.auth.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreens {
    @Serializable
    data object Test : AuthScreens

    @Serializable
    data object Onboarding : AuthScreens

    @Serializable
    data object ChooseUniversity : AuthScreens

    @Serializable
    data object Optionals : AuthScreens

    @Serializable
    data object SignIn : AuthScreens

    @Serializable
    data object Welcome : AuthScreens

    @Serializable
    data object SignUp : AuthScreens

    @Serializable
    data object ForgotPassword : AuthScreens
}
