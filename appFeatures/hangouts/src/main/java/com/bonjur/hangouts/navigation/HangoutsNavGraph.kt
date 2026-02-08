package com.bonjur.hangouts.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.hangouts.presentation.HangoutsListScreen
import com.bonjur.hangouts.presentation.model.HangoutsListInputData
import com.bonjur.navigation.MainScreen
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.hangoutsNavGraph(navigator: Navigator) {
    navigation<MainScreen.Hangouts>(
        startDestination = HangoutsScreens.List
    ) {
        composable<HangoutsScreens.List> {
            HangoutsListScreen(
                inputData = HangoutsListInputData(),
                navigator
            )
        }
    }
}