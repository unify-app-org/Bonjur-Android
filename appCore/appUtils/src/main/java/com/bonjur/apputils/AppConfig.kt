package com.bonjur.apputils

import javax.inject.Singleton

@Singleton
class AppConfig {
    private val environment: String = BuildConfig.ENVIRONMENT

    val isDebug: Boolean = BuildConfig.DEBUG

    val isProduction: Boolean
        get() = environment == "RELEASE"

    val isStaging: Boolean
        get() = environment == "STAGING"
}