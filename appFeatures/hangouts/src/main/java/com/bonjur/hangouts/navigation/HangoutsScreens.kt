package com.bonjur.hangouts.navigation

import kotlinx.serialization.Serializable


sealed interface HangoutsScreens {
    @Serializable
    data object List : HangoutsScreens

    @Serializable
    data object Details : HangoutsScreens

    @Serializable
    data object Create : HangoutsScreens

    @Serializable
    data class Edit(val hangoutId: String) : HangoutsScreens
}