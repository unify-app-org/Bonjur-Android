package com.bonjur.network.manager

import com.bonjur.storage.securePreference.SecureStorage
import com.bonjur.storage.securePreference.SecureStorageKey
import javax.inject.Singleton

interface TokenManager {
    fun saveAccessToken(token: String)
    fun getAccessToken(): String?

    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?

    fun clearTokens()
}

@Singleton
class TokenManagerImpl(
    val securePreference: SecureStorage
): TokenManager {

    override fun saveAccessToken(token: String) {
        securePreference.saveString(
            SecureStorageKey.AUTH_TOKEN,
            token
        )
    }

    override fun getAccessToken(): String? {
        val token = securePreference.getString(
            SecureStorageKey.AUTH_TOKEN,
            ""
        )
        return token
    }

    override fun saveRefreshToken(token: String) {
        securePreference.saveString(
            SecureStorageKey.REFRESH_TOKEN,
            token
        )
    }

    override fun getRefreshToken(): String? {
        val refreshToken = securePreference.getString(
            SecureStorageKey.REFRESH_TOKEN,
            ""
        )
        return refreshToken
    }

    override fun clearTokens() {
        securePreference.remove(SecureStorageKey.REFRESH_TOKEN)
        securePreference.remove(SecureStorageKey.AUTH_TOKEN)
    }
}