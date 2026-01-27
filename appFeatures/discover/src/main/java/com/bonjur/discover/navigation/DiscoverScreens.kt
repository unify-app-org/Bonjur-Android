package com.bonjur.discover.navigation

import kotlinx.serialization.Serializable

sealed interface DiscoverScreens {
    @Serializable
    data object Discover : DiscoverScreens

}