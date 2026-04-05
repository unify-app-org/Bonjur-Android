package com.bonjur.communities.di

import com.bonjur.communities.data.dataSource.CommunitiesDataSource
import com.bonjur.communities.data.dataSource.CommunitiesDataSourceImpl
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
    abstract fun bindCommunitiesDataSource(
        impl: CommunitiesDataSourceImpl
    ): CommunitiesDataSource
}
