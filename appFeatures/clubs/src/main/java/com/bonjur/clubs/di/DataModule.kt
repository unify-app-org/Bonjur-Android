package com.bonjur.clubs.di

import com.bonjur.clubs.data.dataSource.ClubsDataSource
import com.bonjur.clubs.data.dataSource.ClubsDataSourceImpl
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
    abstract fun bindClubsDataSource(
        impl: ClubsDataSourceImpl
    ): ClubsDataSource
}