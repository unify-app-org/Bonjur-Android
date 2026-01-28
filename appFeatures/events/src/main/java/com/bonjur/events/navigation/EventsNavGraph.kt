package com.bonjur.events.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.events.presentation.EventsListScreen
import com.bonjur.navigation.MainScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.eventsNavGraph() {
    navigation<MainScreen.Events>(
        startDestination = EventsScreens.List
    ) {
        composable<EventsScreens.List> {
            EventsListScreen()
        }
    }
}