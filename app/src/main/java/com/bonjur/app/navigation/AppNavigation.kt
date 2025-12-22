package com.bonjur.app.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bonjur.auth.navigation.authNavGraph
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.NavigationEffect
import com.bonjur.navigation.Navigator
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.bonjur.app.R
import kotlinx.coroutines.delay

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