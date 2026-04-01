package com.bonjur.profile.di

import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.profile.domain.usecase.ProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindAuthUseCase(
        impl: ProfileUseCaseImpl
    ): ProfileUseCase
}