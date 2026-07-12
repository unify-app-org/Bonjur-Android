package com.bonjur.app.fcm.data

import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod

sealed class DeviceEndpoints : AppEndpoint {

    // PUT api/as/v1/device/{deviceId} — register/refresh this device's FCM token.
    // Mirrors iOS `UserEnpoints.updateUser` (PUT api/as/v1/device/{id}).
    data class UpdateDevice(
        val deviceId: String,
        val payload: UpdateDeviceRequest,
    ) : DeviceEndpoints() {
        override val path = "api/as/v1/device/$deviceId"
        override val method = NetworkMethod.PUT
        override val body = payload
    }
}
