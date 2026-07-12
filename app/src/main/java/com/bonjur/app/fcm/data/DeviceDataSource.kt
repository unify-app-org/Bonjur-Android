package com.bonjur.app.fcm.data

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

interface DeviceDataSource {
    /** PUT the FCM token for [deviceId]. Requires auth; no-op response body. */
    suspend fun updateFcmToken(deviceId: String, fcmToken: String)
}

class DeviceDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol,
) : NetworkService(apiClient), DeviceDataSource {

    override suspend fun updateFcmToken(deviceId: String, fcmToken: String) {
        fetchRawData(
            DeviceEndpoints.UpdateDevice(
                deviceId = deviceId,
                payload = UpdateDeviceRequest(fcmToken = fcmToken),
            )
        )
    }
}
