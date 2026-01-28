package com.bonjur.groups.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.navigation.MainScreen


fun NavGraphBuilder.groupsNavGraph() {
    navigation<MainScreen.Groups>(
        startDestination = GroupsScreens.List
    ) {
        composable<GroupsScreens.List> {

        }
    }
}