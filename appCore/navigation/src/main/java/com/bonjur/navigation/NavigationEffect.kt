package com.bonjur.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import kotlinx.coroutines.flow.Flow

@Composable
fun NavigationEffect(
    navController: NavHostController,
    navigationFlow: Flow<NavigationCommand>
) {
    LaunchedEffect(navController) {
        navigationFlow.collect { command ->
            when (command) {
                is NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                is NavigationCommand.NavigateAndClearStack -> {
                    navController.navigate(command.route) {
                        popUpTo(AppScreens.Main.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                is NavigationCommand.NavigateUp -> {
                    navController.navigateUp()
                }

                is NavigationCommand.PopToRoute -> {
                    navController.popBackStack(
                        route = command.route,
                        inclusive = command.inclusive
                    )
                }
            }
        }
    }
}