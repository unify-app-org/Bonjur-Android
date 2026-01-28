package com.bonjur.groups.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class GroupsDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
): NetworkService(apiClient), GroupsDataSource {

}