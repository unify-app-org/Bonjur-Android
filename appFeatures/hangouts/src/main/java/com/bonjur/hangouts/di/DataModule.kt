package com.bonjur.hangouts.di

import com.bonjur.hangouts.data.dataSource.HangoutsDataSource
import com.bonjur.hangouts.data.dataSource.HangoutsDataSourceImpl
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
    abstract fun bindHangoutsDataSource(
        impl: HangoutsDataSourceImpl
    ): HangoutsDataSource
}