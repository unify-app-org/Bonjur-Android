package com.bonjur.clubs.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.clubs.presentation.ClubDetailsScreen
import com.bonjur.clubs.presentation.list.ClubsListScreen
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.navigation.MainScreen

fun NavGraphBuilder.clubsNavGraph() {
    navigation<MainScreen.Clubs>(
        startDestination = ClubsScreens.List
    ) {
        composable<ClubsScreens.Details> {
            ClubDetailsScreen(inputData = ClubDetailsInputData(1))
        }

        composable<ClubsScreens.List> {
            ClubsListScreen()
        }
    }
}