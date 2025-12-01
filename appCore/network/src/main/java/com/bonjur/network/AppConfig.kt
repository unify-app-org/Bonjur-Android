package com.bonjur.network

import javax.inject.Singleton

@Singleton
class AppConfig {
    val apiBaseUrl: String = BuildConfig.API_BASE_URL
    val enableLogging: Boolean = BuildConfig.ENABLE_LOGGING
}