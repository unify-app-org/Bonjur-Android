package com.bonjur.clubs.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.clubs.presentation.ClubDetailsScreen
import com.bonjur.clubs.presentation.list.ClubsListScreen
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.navigation.NavArgs
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.clubsNavGraph(
    navigator: Navigator
) {
    composable<ClubsScreens.Details> {
        val inputData = remember { NavArgs.get<ClubDetailsInputData>() ?: ClubDetailsInputData(clubId = 1) }
        ClubDetailsScreen(
            inputData = inputData,
            navigator = navigator
        )
    }

    composable<ClubsScreens.List> {
        ClubsListScreen(
            navigator = navigator
        )
    }
}