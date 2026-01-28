package com.bonjur.events.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class EventsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), EventsDataSource {

}