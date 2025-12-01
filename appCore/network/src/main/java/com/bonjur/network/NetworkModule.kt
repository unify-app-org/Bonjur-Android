package com.bonjur.network

import com.bonjur.network.APIClient.ApiClient
import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.logger.NetworkLogger
import com.bonjur.network.manager.TokenManager
import com.bonjur.network.manager.TokenManagerImpl
import com.bonjur.storage.securePreference.SecureStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient {
        return HttpClient(Android) {

            install(ContentNegotiation.Plugin) {
                json(json)
            }

            install(HttpTimeout.Plugin) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }

            defaultRequest {
                header("Content-Type", "application/json")
            }
        }
    }

    @Provides
    @Singleton
    fun provideNetworkLogger(): NetworkLogger {
        return NetworkLogger()
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        storage: SecureStorage
    ): TokenManager {
        return TokenManagerImpl(
            storage
        )
    }

    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig {
        return AppConfig()
    }

    @Provides
    @Singleton
    fun provideApiClient(
        client: HttpClient,
        json: Json,
        tokenManager: TokenManager,
        logger: NetworkLogger,
        appConfig: AppConfig
    ): ApiClientProtocol {
        return ApiClient(client, json, tokenManager, logger, appConfig)
    }
}