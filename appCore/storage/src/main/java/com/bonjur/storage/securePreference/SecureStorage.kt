package com.bonjur.storage.securePreference

interface SecureStorage {
    fun saveString(key: SecureStorageKey, value: String?)
    fun getString(key: SecureStorageKey, default: String?): String?

    fun saveInt(key: SecureStorageKey, value: Int)
    fun getInt(key: SecureStorageKey, default: Int): Int

    fun saveFloat(key: SecureStorageKey, value: Float)
    fun getFloat(key: SecureStorageKey, default: Float): Float

    fun saveBoolean(key: SecureStorageKey, value: Boolean)
    fun getBoolean(key: SecureStorageKey, default: Boolean): Boolean

    fun remove(key: SecureStorageKey)
}