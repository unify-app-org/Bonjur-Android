package com.bonjur.discover.di

import com.bonjur.discover.data.dataSources.DiscoverDataSource
import com.bonjur.discover.data.dataSources.DiscoverDataSourceImpl
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
    abstract fun bindDiscoverDataSource(
        impl: DiscoverDataSourceImpl
    ): DiscoverDataSource
}