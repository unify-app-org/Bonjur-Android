package com.bonjur.groups.navigation

import kotlinx.serialization.Serializable

sealed interface GroupsScreens {
    @Serializable
    data object List : GroupsScreens

}