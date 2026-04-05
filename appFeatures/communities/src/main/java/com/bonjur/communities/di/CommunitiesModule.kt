package com.bonjur.communities.di

import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.domain.useCase.CommunitiesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommunitiesModule {

    @Binds
    @Singleton
    abstract fun bindCommunitiesUseCase(
        impl: CommunitiesUseCaseImpl
    ): CommunitiesUseCase
}
