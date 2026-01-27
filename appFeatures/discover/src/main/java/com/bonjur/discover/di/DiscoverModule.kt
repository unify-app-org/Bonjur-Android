package com.bonjur.discover.di

import com.bonjur.discover.domain.useCase.DiscoverUseCase
import com.bonjur.discover.domain.useCase.DiscoverUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiscoverModule {

    @Binds
    @Singleton
    abstract fun bindAuthUseCase(
        impl: DiscoverUseCaseImpl
    ): DiscoverUseCase
}