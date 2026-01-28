package com.bonjur.hangouts.di

import com.bonjur.hangouts.domain.useCase.HangoutsUseCase
import com.bonjur.hangouts.domain.useCase.HangoutsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class HangoutsModule {

    @Binds
    @Singleton
    abstract fun bindHangoutsUseCase(
        impl: HangoutsUseCaseImpl
    ): HangoutsUseCase
}