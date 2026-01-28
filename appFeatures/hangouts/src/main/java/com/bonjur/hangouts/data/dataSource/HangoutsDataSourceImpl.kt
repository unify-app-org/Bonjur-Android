package com.bonjur.hangouts.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class HangoutsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
): NetworkService(apiClient), HangoutsDataSource {

}