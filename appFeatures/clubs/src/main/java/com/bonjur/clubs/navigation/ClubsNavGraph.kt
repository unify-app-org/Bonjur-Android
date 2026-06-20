package com.bonjur.clubs.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.clubs.presentation.ClubDetailsScreen
import com.bonjur.clubs.presentation.create.ClubCreateScreen
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.list.ClubsListScreen
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.navigation.ClubDetailsNavArgs
import com.bonjur.navigation.NavArgs
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.clubsNavGraph(
    navigator: Navigator
) {
    composable<ClubsScreens.Details> {
        // Accept the clubs-local payload, or the neutral cross-feature payload
        // (e.g. from events, which can't depend on the clubs module).
        val inputData = remember {
            NavArgs.get<ClubDetailsInputData>()
                ?: NavArgs.get<ClubDetailsNavArgs>()?.let { ClubDetailsInputData(clubId = it.clubId) }
                ?: ClubDetailsInputData(clubId = 1)
        }
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

    composable<ClubsScreens.Create> {
        ClubCreateScreen(
            inputData = ClubCreateInputData(),
            navigator = navigator
        )
    }

    composable<ClubsScreens.Edit> {
        val inputData = remember {
            NavArgs.get<ClubCreateInputData>() ?: ClubCreateInputData()
        }
        ClubCreateScreen(
            inputData = inputData,
            navigator = navigator
        )
    }
}