package com.bonjur.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bonjur.app.tabBar.navigation.mainNavGraph
import com.bonjur.auth.navigation.authNavGraph
import com.bonjur.discover.navigation.discoverNavGraph
import com.bonjur.events.navigation.eventsNavGraph
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.NavigationEffect
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey

@Composable
fun AppNavigation(
    navigator: Navigator = hiltViewModel<AppNavigationViewModel>().navigator,
    defaultStorage: DefaultStorage = hiltViewModel<AppNavigationViewModel>().defaultStorage
) {
    val navController = rememberNavController()

    val isAuthenticated = defaultStorage.getBoolean(
        DefaultStorageKey.IS_AUTHENTICATED, default = false
    )

    LaunchedEffect(UInt) {
//        defaultStorage.saveBoolean(DefaultStorageKey.IS_AUTHENTICATED, false)
    }

    NavigationEffect(
        navController = navController,
        navigationFlow = navigator.navigationCommands
    )

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) AppScreens.Main else AppScreens.Auth
    ) {
        authNavGraph()
        mainNavGraph()
        eventsNavGraph()
        discoverNavGraph()
    }
}