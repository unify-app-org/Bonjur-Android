package com.bonjur.hangouts.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.hangouts.presentation.create.HangoutCreateScreen
import com.bonjur.hangouts.presentation.create.models.HangoutCreateInputData
import com.bonjur.hangouts.presentation.detail.HangoutDetailsScreen
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.hangouts.presentation.list.HangoutsListScreen
import com.bonjur.hangouts.presentation.list.model.HangoutsListInputData
import com.bonjur.navigation.HangoutDetailsNavArgs
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
            // Accept the hangouts-local payload, or the neutral cross-feature
            // payload (e.g. from the notification feed, which can't depend on
            // the hangouts module).
            val inputData = remember {
                NavArgs.get<HangoutDetailsInputData>()
                    ?: NavArgs.get<HangoutDetailsNavArgs>()?.let { HangoutDetailsInputData(hangoutId = it.hangoutId) }
                    ?: HangoutDetailsInputData(hangoutId = "")
            }
            HangoutDetailsScreen(
                inputData = inputData,
                navigator
            )
        }

        composable<HangoutsScreens.Create> {
            HangoutCreateScreen(
                inputData = HangoutCreateInputData(),
                navigator = navigator
            )
        }

        composable<HangoutsScreens.Edit> {
            val inputData = remember {
                NavArgs.get<HangoutCreateInputData>() ?: HangoutCreateInputData()
            }
            HangoutCreateScreen(
                inputData = inputData,
                navigator = navigator
            )
        }
    }
}