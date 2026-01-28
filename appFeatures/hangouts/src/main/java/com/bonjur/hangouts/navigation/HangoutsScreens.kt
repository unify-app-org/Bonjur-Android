package com.bonjur.hangouts.navigation

import kotlinx.serialization.Serializable


sealed interface HangoutsScreens {
    @Serializable
    data object List : HangoutsScreens
}