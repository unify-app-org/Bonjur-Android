package com.bonjur.notification.navigation

import kotlinx.serialization.Serializable

sealed interface NotificationScreens {
    @Serializable
    data object Feed : NotificationScreens

    @Serializable
    data object NeedsAction : NotificationScreens

    @Serializable
    data object Verification : NotificationScreens
}
