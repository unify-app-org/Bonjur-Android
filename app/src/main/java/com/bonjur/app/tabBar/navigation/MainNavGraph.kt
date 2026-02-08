package com.bonjur.app.tabBar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.navigation.AppScreens
import androidx.navigation.navigation
import com.bonjur.app.tabBar.AppTabBar
import com.bonjur.navigation.MainScreen
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.mainNavGraph(navigator: Navigator) {
    navigation<AppScreens.Main>(
        startDestination = MainScreen.TabBar
    ) {
        composable<MainScreen.TabBar> {
            AppTabBar(navigator)
        }
    }
}