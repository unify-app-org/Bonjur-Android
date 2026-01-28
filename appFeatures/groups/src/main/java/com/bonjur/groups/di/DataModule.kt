package com.bonjur.groups.di

import com.bonjur.groups.data.dataSource.GroupsDataSource
import com.bonjur.groups.data.dataSource.GroupsDataSourceImpl
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
    abstract fun bindGroupsDataSource(
        impl: GroupsDataSourceImpl
    ): GroupsDataSource
}