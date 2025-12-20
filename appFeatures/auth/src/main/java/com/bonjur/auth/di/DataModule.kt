package com.bonjur.auth.di

import com.bonjur.auth.data.dataSource.AuthDataSource
import com.bonjur.auth.data.dataSource.AuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        impl: AuthDataSourceImpl
    ): AuthDataSource
}