package com.bonjur.auth.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
import com.bonjur.auth.presentation.signIn.model.SignInInputData
import com.bonjur.auth.presentation.signIn.SignInScreen
import com.bonjur.auth.presentation.welcome.AuthWelcomeScreen
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.NavArgs

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

        composable<AuthScreens.SignIn> {
            SignInScreen(
                SignInInputData()
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

        composable<AuthScreens.Welcome> {
            val inputData = remember { NavArgs.get<AuthWelcomeInputData>() ?: AuthWelcomeInputData() }
            AuthWelcomeScreen(inputData)
        }

        composable<AuthScreens.SignUp> {
            RegistrationGreeting(
                name = "SignUp"
            )
        }

        composable<AuthScreens.ForgotPassword> {
            val forgotPass = remember { NavArgs.get<ForgotPass>() ?: ForgotPass(email = "") }
            RegistrationGreeting(name = forgotPass.email)
        }
    }
}
