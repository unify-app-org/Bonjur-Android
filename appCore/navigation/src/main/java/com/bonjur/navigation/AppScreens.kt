package com.bonjur.navigation

import kotlinx.serialization.Serializable

sealed interface AppScreens {
    @Serializable
    data object Splash : AppScreens
    
    @Serializable
    data object Auth : AppScreens
    
    @Serializable
    data object Main : AppScreens
}

sealed interface TabScreens {
    @Serializable
    data object Events : TabScreens
    
    @Serializable
    data object Clubs : TabScreens
    
    @Serializable
    data object Hangouts : TabScreens
    
    @Serializable
    data object Profile : TabScreens
}

val Any.route: String
    get() = this::class.qualifiedName!!