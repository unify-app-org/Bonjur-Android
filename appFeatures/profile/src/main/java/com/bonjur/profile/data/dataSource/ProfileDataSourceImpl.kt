package com.bonjur.profile.data.dataSource

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.NetworkService
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    apiClient: ApiClientProtocol
) : NetworkService(apiClient), ProfileDataSource {

}