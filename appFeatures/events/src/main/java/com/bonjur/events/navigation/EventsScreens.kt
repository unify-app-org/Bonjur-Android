package com.bonjur.events.navigation

import kotlinx.serialization.Serializable

sealed interface EventsScreens {
    @Serializable
    data object List : EventsScreens

    @Serializable
    data object Details : EventsScreens

    @Serializable
    data object Create : EventsScreens

    @Serializable
    data class Edit(val eventId: String) : EventsScreens
}