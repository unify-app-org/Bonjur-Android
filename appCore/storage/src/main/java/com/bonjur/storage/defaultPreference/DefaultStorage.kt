package com.bonjur.storage.defaultPreference

interface DefaultStorage {
    fun saveString(key: DefaultStorageKey, value: String?)
    fun getString(key: DefaultStorageKey, default: String?): String?

    fun saveInt(key: DefaultStorageKey, value: Int)
    fun getInt(key: DefaultStorageKey, default: Int): Int

    fun saveFloat(key: DefaultStorageKey, value: Float)
    fun getFloat(key: DefaultStorageKey, default: Float): Float

    fun saveBoolean(key: DefaultStorageKey, value: Boolean)
    fun getBoolean(key: DefaultStorageKey, default: Boolean): Boolean

    fun remove(key: DefaultStorageKey)
}
