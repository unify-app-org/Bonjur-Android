package com.bonjur.auth.navigation

import android.net.Uri
import com.bonjur.auth.ForgotPass
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed interface AuthScreens {
    @Serializable
    data object Test : AuthScreens

    @Serializable
    data object Login : AuthScreens

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