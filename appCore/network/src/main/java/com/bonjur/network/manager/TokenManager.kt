package com.bonjur.network.manager

import com.bonjur.storage.securePreference.SecureStorage
import com.bonjur.storage.securePreference.SecureStorageKey
import javax.inject.Inject
import javax.inject.Singleton

interface TokenManager {
    fun saveAccessToken(token: String)
    fun getAccessToken(): String?

    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?

    fun saveUserId(userId: String)
    fun getUserId(): String?

    fun saveUserEmail(email: String)
    fun getUserEmail(): String?

    fun clearTokens()
}

@Singleton
class TokenManagerImpl @Inject constructor(
    val securePreference: SecureStorage
): TokenManager {

    override fun saveAccessToken(token: String) {
        securePreference.saveString(SecureStorageKey.AUTH_TOKEN, token)
    }

    override fun getAccessToken(): String? {
        return securePreference.getString(SecureStorageKey.AUTH_TOKEN, "")?.ifBlank { null }
    }

    override fun saveRefreshToken(token: String) {
        securePreference.saveString(SecureStorageKey.REFRESH_TOKEN, token)
    }

    override fun getRefreshToken(): String? {
        return securePreference.getString(SecureStorageKey.REFRESH_TOKEN, "")?.ifBlank { null }
    }

    override fun saveUserId(userId: String) {
        securePreference.saveString(SecureStorageKey.USER_ID, userId)
    }

    override fun getUserId(): String? {
        return securePreference.getString(SecureStorageKey.USER_ID, "")?.ifBlank { null }
    }

    /** Stashed at sign-in so the create forms can prefill the owner-contact field. */
    override fun saveUserEmail(email: String) {
        securePreference.saveString(SecureStorageKey.USER_EMAIL, email)
    }

    override fun getUserEmail(): String? {
        return securePreference.getString(SecureStorageKey.USER_EMAIL, "")?.ifBlank { null }
    }

    override fun clearTokens() {
        securePreference.remove(SecureStorageKey.REFRESH_TOKEN)
        securePreference.remove(SecureStorageKey.AUTH_TOKEN)
        securePreference.remove(SecureStorageKey.USER_ID)
        securePreference.remove(SecureStorageKey.USER_EMAIL)
    }
}