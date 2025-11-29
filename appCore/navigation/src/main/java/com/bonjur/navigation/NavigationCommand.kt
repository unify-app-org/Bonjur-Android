package com.bonjur.navigation

sealed interface NavigationCommand {
    data class NavigateTo(
        val route: String
    ) : NavigationCommand

    data class NavigateAndClearStack(
        val route: String
    ) : NavigationCommand

    data object NavigateUp : NavigationCommand

    data class PopToRoute(
        val route: String,
        val inclusive: Boolean = false
    ) : NavigationCommand

}