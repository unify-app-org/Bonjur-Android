package com.bonjur.events.di

import com.bonjur.events.data.dataSource.EventsDataSource
import com.bonjur.events.data.dataSource.EventsDataSourceImpl
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
    abstract fun bindEventsDataSource(
        impl: EventsDataSourceImpl
    ): EventsDataSource
}