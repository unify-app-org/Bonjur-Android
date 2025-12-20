package com.bonjur.storage.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageImpl
import com.bonjur.storage.securePreference.SecureStorage
import com.bonjur.storage.securePreference.SecureStorageImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    private const val SECURE_PREF_NAME = "secure_prefs"
    private const val PREF_NAME = "app_prefs"

    @Provides
    @Singleton
    @PlainPrefs
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    @EncryptedPrefs
    fun provideEncryptedSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            SECURE_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideDefaultStorage(
        @PlainPrefs sharedPreferences: SharedPreferences
    ): DefaultStorage =
        DefaultStorageImpl(sharedPreferences)

    @Provides
    @Singleton
    fun provideSecureStorage(
        @EncryptedPrefs sharedPreferences: SharedPreferences
    ): SecureStorage =
        SecureStorageImpl(sharedPreferences)
}
