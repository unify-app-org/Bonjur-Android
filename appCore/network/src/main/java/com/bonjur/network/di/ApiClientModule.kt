package com.bonjur.network.di

import com.bonjur.network.APIClient.ApiClient
import com.bonjur.network.APIClient.ApiClientProtocol
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiClientModule {

    @Binds
    @Singleton
    abstract fun bindApiClient(
        impl: ApiClient
    ): ApiClientProtocol
}