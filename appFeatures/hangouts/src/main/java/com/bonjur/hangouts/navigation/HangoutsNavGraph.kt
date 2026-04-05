package com.bonjur.hangouts.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.hangouts.presentation.detail.HangoutDetailsScreen
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.hangouts.presentation.list.HangoutsListScreen
import com.bonjur.hangouts.presentation.list.model.HangoutsListInputData
import com.bonjur.navigation.MainScreen
import com.bonjur.navigation.NavArgs
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

        composable<HangoutsScreens.Details> {
            val inputData = remember { NavArgs.get<HangoutDetailsInputData>() ?: HangoutDetailsInputData(hangoutId = "") }
            HangoutDetailsScreen(
                inputData = inputData,
                navigator
            )
        }
    }
}