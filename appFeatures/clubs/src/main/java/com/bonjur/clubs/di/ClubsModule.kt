package com.bonjur.clubs.di

import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.domain.useCase.ClubsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClubsModule {

    @Binds
    @Singleton
    abstract fun bindAuthUseCase(
        impl: ClubsUseCaseImpl
    ): ClubsUseCase
}