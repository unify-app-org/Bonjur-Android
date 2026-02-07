package com.bonjur.clubs.navigation

import kotlinx.serialization.Serializable

sealed interface ClubsScreens {
    @Serializable
    data object List : ClubsScreens
    @Serializable
    data object Details : ClubsScreens
}