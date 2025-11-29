package com.bonjur.storage.defaultPreference

import android.content.SharedPreferences
import javax.inject.Inject

class DefaultStorageImpl @Inject constructor(
    private val prefs: SharedPreferences
): DefaultStorage {

    override fun saveString(key: DefaultStorageKey, value: String?) {
        prefs.edit().putString(key.key, value).apply()
    }

    override fun getString(key: DefaultStorageKey, default: String?): String? {
        return prefs.getString(key.key, default)
    }

    override fun saveInt(key: DefaultStorageKey, value: Int) {
        prefs.edit().putInt(key.key, value).apply()
    }

    override fun getInt(key: DefaultStorageKey, default: Int): Int {
        return prefs.getInt(key.key, default)
    }

    override fun saveFloat(key: DefaultStorageKey, value: Float) {
        prefs.edit().putFloat(key.key, value).apply()
    }

    override fun getFloat(key: DefaultStorageKey, default: Float): Float {
        return prefs.getFloat(key.key, default)
    }

    override fun saveBoolean(key: DefaultStorageKey, value: Boolean) {
        prefs.edit().putBoolean(key.key, value).apply()
    }

    override fun getBoolean(key: DefaultStorageKey, default: Boolean): Boolean {
        return prefs.getBoolean(key.key, default)
    }

    override fun remove(key: DefaultStorageKey) {
        prefs.edit().remove(key.key).apply()
    }
}
