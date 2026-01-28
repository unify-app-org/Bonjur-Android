package com.bonjur.groups.di

import com.bonjur.groups.domain.useCase.GroupsUseCase
import com.bonjur.groups.domain.useCase.GroupsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupsModule {

    @Binds
    @Singleton
    abstract fun bindGroupsUseCase(
        impl: GroupsUseCaseImpl
    ): GroupsUseCase
}