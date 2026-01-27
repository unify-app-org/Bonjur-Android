package com.bonjur.discover.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.navigation.MainScreen

fun NavGraphBuilder.discoverNavGraph() {
    navigation<MainScreen.Discover>(
        startDestination = DiscoverScreens.Discover
    ) {
        composable<DiscoverScreens.Discover> {

        }
    }
}