package com.bonjur.clubs.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.clubs.presentation.ClubDetailsScreen
import com.bonjur.clubs.presentation.list.ClubsListScreen
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.clubsNavGraph(
    navigator: Navigator
) {
    composable<ClubsScreens.Details> {
        ClubDetailsScreen(
            inputData = ClubDetailsInputData(1),
            navigator = navigator
        )
    }

    composable<ClubsScreens.List> {
        ClubsListScreen(
            navigator = navigator
        )
    }
}