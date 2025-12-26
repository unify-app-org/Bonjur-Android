package com.bonjur.auth.navigation

import android.net.Uri
import com.bonjur.auth.ForgotPass
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.navigation.AppScreens
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

sealed interface AuthScreens {
    @Serializable
    data object Test : AuthScreens

    @Serializable
    data object Login : AuthScreens

    @Serializable
    data object Onboarding : AuthScreens

    @Serializable
    data object ChooseUniversity : AuthScreens

    @Serializable
    data object Optionals : AuthScreens

    @Serializable
    data class Welcome(val welcome: AuthWelcomeInputData): AuthScreens {
        companion object {
            const val ROUTE = "welcome/{welcomeJson}"
            fun createRoute(welcomeInputData: AuthWelcomeInputData): String {
                val json = Json.encodeToString(
                    AuthWelcomeInputData.serializer(),
                    welcomeInputData
                )
                val encoded = Uri.encode(json)
                return "welcome/$encoded"
            }
        }
    }
    @Serializable
    data object SignUp : AuthScreens

    @Serializable
    data class ForgotPassword(val forgotPass: ForgotPass) : AuthScreens {
        companion object {
            const val ROUTE = "forgot_password/{forgotPassJson}"
            fun createRoute(forgotPass: ForgotPass): String {
                val json = Json.encodeToString(
                    ForgotPass.serializer(),
                    forgotPass
                )
                val encoded = Uri.encode(json)
                return "forgot_password/$encoded"
            }
        }
    }
}