package com.bonjur.storage.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlainPrefs

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EncryptedPrefs