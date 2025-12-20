package com.bonjur.network.di

import com.bonjur.network.manager.TokenManager
import com.bonjur.network.manager.TokenManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {

    @Binds
    @Singleton
    abstract fun bindTokenManager(
        impl: TokenManagerImpl
    ): TokenManager
}