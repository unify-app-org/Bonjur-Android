package com.bonjur.clubs.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.navigation.MainScreen

fun NavGraphBuilder.clubsNavGraph() {
    navigation<MainScreen.Clubs>(
        startDestination = ClubsScreens.Details
    ) {
        composable<ClubsScreens.Details> {

        }
    }
}