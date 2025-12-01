package com.bonjur.network

import com.bonjur.network.BuildConfig
import dagger.hilt.InstallIn

@InstallIn
class AppConfig {
    val apiBaseUrl: String = BuildConfig.API_BASE_URL
    val environment: String = BuildConfig.ENVIRONMENT
    val enableLogging: Boolean = BuildConfig.ENABLE_LOGGING
    val isDebug: Boolean = BuildConfig.DEBUG

    val isProduction: Boolean
        get() = environment == "RELEASE"

    val isTest: Boolean
        get() = environment == "TEST"
}