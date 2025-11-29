package com.bonjur.storage.securePreference

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SecureStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): SecureStorage {

    override fun saveString(key: SecureStorageKey, value: String?) {
        sharedPreferences.edit { putString(key.key, value) }
    }

    override fun getString(key: SecureStorageKey, default: String?): String? {
        return sharedPreferences.getString(key.key, default)
    }

    override fun saveInt(key: SecureStorageKey, value: Int) {
        sharedPreferences.edit { putInt(key.key, value) }
    }

    override fun getInt(key: SecureStorageKey, default: Int): Int {
        return sharedPreferences.getInt(key.key, default)
    }

    override fun saveFloat(key: SecureStorageKey, value: Float) {
        sharedPreferences.edit { putFloat(key.key, value) }
    }

    override fun getFloat(key: SecureStorageKey, default: Float): Float {
        return sharedPreferences.getFloat(key.key, default)
    }

    override fun saveBoolean(key: SecureStorageKey, value: Boolean) {
        sharedPreferences.edit { putBoolean(key.key, value) }
    }

    override fun getBoolean(key: SecureStorageKey, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key.key, default)
    }

    override fun remove(key: SecureStorageKey) {
        sharedPreferences.edit { remove(key.key) }
    }
}
