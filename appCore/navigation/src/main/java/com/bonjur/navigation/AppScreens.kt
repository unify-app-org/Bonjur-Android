package com.bonjur.navigation

import kotlinx.serialization.Serializable

sealed interface AppScreens {
    @Serializable
    data object Auth : AppScreens
    @Serializable
    data object Main : AppScreens
}

val Any.route: String
    get() = this::class.qualifiedName!!