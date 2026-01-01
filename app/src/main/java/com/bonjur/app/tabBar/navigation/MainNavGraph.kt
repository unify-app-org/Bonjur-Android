package com.bonjur.app.tabBar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.navigation.AppScreens
import androidx.navigation.navigation
import com.bonjur.app.tabBar.AppTabBar

fun NavGraphBuilder.mainNavGraph() {
    navigation<AppScreens.Main>(
        startDestination = MainScreen.TabBar
    ) {
        composable<MainScreen.TabBar> {
            AppTabBar()
        }
    }
}