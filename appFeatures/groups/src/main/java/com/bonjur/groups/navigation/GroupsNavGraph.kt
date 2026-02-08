package com.bonjur.groups.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bonjur.groups.presentation.GroupsListScreen
import com.bonjur.navigation.MainScreen
import com.bonjur.navigation.Navigator


fun NavGraphBuilder.groupsNavGraph(
    navigator: Navigator
) {
    composable<GroupsScreens.List> {
        GroupsListScreen()
    }
}