package com.bonjur.navigation

import kotlinx.serialization.Serializable

sealed interface AppScreens {
    @Serializable
    data object Auth : AppScreens
    @Serializable
    data object Main : AppScreens
}

sealed interface MainScreen {
    @Serializable
    object TabBar : MainScreen

    @Serializable
    object Discover : MainScreen

    @Serializable
    object Clubs : MainScreen

    @Serializable
    object Events : MainScreen

    @Serializable
    object Hangouts : MainScreen
}

val Any.route: String
    get() = this::class.qualifiedName!!