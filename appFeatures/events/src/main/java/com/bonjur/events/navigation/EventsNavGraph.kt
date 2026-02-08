package com.bonjur.events.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
    }
}