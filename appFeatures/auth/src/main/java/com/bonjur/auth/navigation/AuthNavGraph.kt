package com.bonjur.auth.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bonjur.auth.ForgotPass
import com.bonjur.auth.RegistrationGreeting
import com.bonjur.auth.presentation.model.ExampleInputData
import com.bonjur.auth.presentation.ExampleScreen
import com.bonjur.navigation.AppScreens
import kotlinx.serialization.json.Json

fun NavGraphBuilder.authNavGraph() {
    navigation<AppScreens.Auth>(
        startDestination = AuthScreens.Login
    ) {
        composable<AuthScreens.Test> {
            ExampleScreen(inputData = ExampleInputData())
        }

        composable<AuthScreens.Login> {
            RegistrationGreeting(
                name = "Login"
            )
        }

        composable<AuthScreens.SignUp> {
            RegistrationGreeting(
                name = "SignUp"
            )
        }

        composable(
            route = AuthScreens.ForgotPassword.ROUTE,
            arguments = listOf(navArgument("forgotPassJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("forgotPassJson") ?: ""
            val forgotPass = Json.decodeFromString(
                ForgotPass.serializer(),
                Uri.decode(json)
            )
            RegistrationGreeting(name = forgotPass.email)
        }
    }
}