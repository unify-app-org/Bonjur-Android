package com.bonjur.app.tabBar.navigation

import kotlinx.serialization.Serializable

sealed interface MainScreen {
    @Serializable
    object TabBar : MainScreen
}