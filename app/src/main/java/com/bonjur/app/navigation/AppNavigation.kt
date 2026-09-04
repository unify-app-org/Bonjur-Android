package com.bonjur.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bonjur.app.tabBar.navigation.mainNavGraph
import com.bonjur.auth.navigation.authNavGraph
import com.bonjur.designSystem.components.alert.AppAlertOverlay
import com.bonjur.designSystem.components.loading.AppLoadingOverlay
import com.bonjur.designSystem.components.loading.AppLoadingUI
import com.bonjur.designSystem.components.snackbar.AppSnackBarOverlay
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.NavigationEffect
import com.bonjur.navigation.Navigator
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

    // The widget's card is normally written when the user opens their own profile.
    // A widget added right after signing in would otherwise sit there telling an
    // already-signed-in user to sign in.
    val appViewModel = hiltViewModel<AppNavigationViewModel>()
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) appViewModel.publishWidgetCardIfNeeded()
    }

    LaunchedEffect(Unit) {
//        defaultStorage.saveBoolean(DefaultStorageKey.IS_AUTHENTICATED, false)
    }

    NavigationEffect(
        navController = navController,
        navigationFlow = navigator.navigationCommands
    )

    // Clear any stuck global loading whenever the destination changes. A screen that
    // posts Loading(true) then navigates away disposes before its Loading(false) is
    // delivered, so the routed dismiss would otherwise never fire.
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect {
            AppLoadingUI.reset()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Global bottom safe area, the equivalent of iOS's safe-area inset. The app
        // is edge-to-edge (mandatory from Android 15), so without this every screen
        // has to remember `navigationBarsPadding()` on its own bottom content — and
        // the ones that forgot ran under the system bar (worst with 3-button nav,
        // ~48dp, where a fixed 16dp only clears a gesture pill).
        //
        // Only the bottom edge is consumed: hero covers still bleed under the status
        // bar, matching iOS. `windowInsetsPadding` consumes what it applies, so a
        // nested `navigationBarsPadding()` resolves to zero instead of double-padding.
        //
        // The overlays stay outside it — they are full-screen surfaces that do their
        // own inset handling.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
                )
        ) {
            NavHost(
                navController = navController,
                startDestination = if (isAuthenticated) AppScreens.Main else AppScreens.Auth
            ) {
                authNavGraph()
                mainNavGraph(navigator)
            }
        }

        AppAlertOverlay()
        AppSnackBarOverlay()
        AppLoadingOverlay()
    }
}