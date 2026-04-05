package com.bonjur.communities.navigation

import kotlinx.serialization.Serializable

sealed interface CommunitiesScreens {
    @Serializable
    data object List : CommunitiesScreens

    @Serializable
    data object Details : CommunitiesScreens
}
