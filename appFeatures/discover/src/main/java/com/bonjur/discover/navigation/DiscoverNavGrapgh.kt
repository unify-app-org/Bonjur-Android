package com.bonjur.discover.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.discover.presentation.DiscoverScreen
import com.bonjur.discover.presentation.models.DiscoverInputData
import com.bonjur.events.presentation.list.EventsListScreen
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.discoverNavGraph(
    navigator: Navigator,
    seeAllClubs: (() -> Unit)?
) {
    composable<DiscoverScreens.Discover> {
        DiscoverScreen(
            inputData = DiscoverInputData(onTabChange = seeAllClubs),
            navigator = navigator
        )
    }

    composable<DiscoverScreens.EventsList> {
        EventsListScreen(
            navigator = navigator
        )
    }
}