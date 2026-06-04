package com.bonjur.events.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bonjur.events.presentation.create.EventCreateScreen
import com.bonjur.events.presentation.create.models.EventCreateInputData
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.events.presentation.details.EventDetailsScreen
import com.bonjur.events.presentation.list.EventsListScreen
import com.bonjur.navigation.MainScreen
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.eventsNavGraph(navigator: Navigator) {
    navigation<MainScreen.Events>(
        startDestination = EventsScreens.List
    ) {
        composable<EventsScreens.List> {
            EventsListScreen(
                navigator = navigator
            )
        }

        composable<EventsScreens.Details> {
            EventDetailsScreen(
                inputData = EventDetailsInputData(eventId = ""),
                navigator = navigator
            )
        }

        composable<EventsScreens.Create> {
            EventCreateScreen(
                inputData = EventCreateInputData(),
                navigator = navigator
            )
        }

        composable<EventsScreens.Edit> { backStackEntry ->
            val route = backStackEntry.toRoute<EventsScreens.Edit>()
            EventCreateScreen(
                inputData = EventCreateInputData(eventId = route.eventId),
                navigator = navigator
            )
        }
    }
}