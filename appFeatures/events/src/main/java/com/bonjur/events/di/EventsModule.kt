package com.bonjur.events.di

import com.bonjur.events.domain.useCase.EventsUseCase
import com.bonjur.events.domain.useCase.EventsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventsModule {

    @Binds
    @Singleton
    abstract fun bindAuthUseCase(
        impl: EventsUseCaseImpl
    ): EventsUseCase
}