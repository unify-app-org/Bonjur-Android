package com.bonjur.discover.data.dataSources

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class DiscoverDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), DiscoverDataSource {

}