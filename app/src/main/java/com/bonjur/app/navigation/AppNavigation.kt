package com.bonjur.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bonjur.auth.navigation.authNavGraph
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.NavigationEffect
import com.bonjur.navigation.Navigator

@Composable
fun AppNavigation(
    navigator: Navigator = hiltViewModel<AppNavigationViewModel>().navigator,
    padding: PaddingValues
) {
    val navController = rememberNavController()
    
    NavigationEffect(
        navController = navController,
        navigationFlow = navigator.navigationCommands
    )
    
    NavHost(
        navController = navController,
        startDestination = AppScreens.Auth,
        modifier = Modifier.padding(padding)
    ) {
        authNavGraph()
    }
}
