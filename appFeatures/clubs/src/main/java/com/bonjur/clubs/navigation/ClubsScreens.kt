package com.bonjur.clubs.navigation

import kotlinx.serialization.Serializable

sealed interface ClubsScreens {
    @Serializable
    data object List : ClubsScreens
    @Serializable
    data object Details : ClubsScreens
    @Serializable
    data object Create : ClubsScreens
    // Payload (clubId) is passed via NavArgs, matching Details — the string-based
    // Navigator routes by qualified name, so parameterized routes aren't reachable.
    @Serializable
    data object Edit : ClubsScreens
}