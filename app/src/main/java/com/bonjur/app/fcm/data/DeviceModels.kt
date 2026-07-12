package com.bonjur.app.fcm.data

import kotlinx.serialization.Serializable

/** Body for PUT api/as/v1/device/{deviceId}. Mirrors iOS `UserUpdate(fcmToken:)`. */
@Serializable
data class UpdateDeviceRequest(
    val fcmToken: String,
)
