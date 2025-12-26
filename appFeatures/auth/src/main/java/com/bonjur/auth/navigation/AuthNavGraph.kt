package com.bonjur.auth.navigation

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bonjur.auth.ForgotPass
import com.bonjur.auth.RegistrationGreeting
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityInputData
import com.bonjur.auth.presentation.chooseUniversity.ChooseUniversityScreen
import com.bonjur.auth.presentation.example.model.ExampleInputData
import com.bonjur.auth.presentation.example.ExampleScreen
import com.bonjur.auth.presentation.onboarding.model.OnboardingInputData
import com.bonjur.auth.presentation.onboarding.OnboardingScreen
import com.bonjur.auth.presentation.optional.AuthOptionalInfoScreen
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoInputData
import com.bonjur.auth.presentation.welcome.AuthWelcomeScreen
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.route
import kotlinx.serialization.json.Json

fun NavGraphBuilder.authNavGraph() {
    navigation<AppScreens.Auth>(
        startDestination = AuthScreens.Onboarding
    ) {
        composable<AuthScreens.Test> {
            ExampleScreen(inputData = ExampleInputData())
        }

        composable<AuthScreens.Onboarding> {
            OnboardingScreen(
                OnboardingInputData()
            )
        }

        composable<AuthScreens.ChooseUniversity> {
            ChooseUniversityScreen(
                ChooseUniversityInputData()
            )
        }

        composable<AuthScreens.Optionals> {
            AuthOptionalInfoScreen(
                AuthOptionalInfoInputData()
            )
        }

        composable(
            route = AuthScreens.Welcome.ROUTE,
            arguments = listOf(navArgument("welcomeJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("welcomeJson") ?: ""
            val welcome = Json.decodeFromString(
                AuthWelcomeInputData.serializer(),
                Uri.decode(json)
            )
            AuthWelcomeScreen(
                welcome
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