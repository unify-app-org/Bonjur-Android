package com.bonjur.auth.di

import com.bonjur.auth.data.dataSource.AuthDataSource
import com.bonjur.auth.data.dataSource.AuthDataSourceImpl
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.domain.useCase.AuthUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindAuthUseCase(
        impl: AuthUseCaseImpl
    ): AuthUseCase
}
