package com.bonjur.notification.di

import com.bonjur.notification.data.dataSource.NotificationDataSource
import com.bonjur.notification.data.dataSource.NotificationDataSourceImpl
import com.bonjur.notification.domain.useCase.NeedsActionUseCase
import com.bonjur.notification.domain.useCase.NeedsActionUseCaseImpl
import com.bonjur.notification.domain.useCase.NotificationUseCase
import com.bonjur.notification.domain.useCase.NotificationUseCaseImpl
import com.bonjur.notification.domain.useCase.VerificationUseCase
import com.bonjur.notification.domain.useCase.VerificationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationDataSource(impl: NotificationDataSourceImpl): NotificationDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationUseCase(impl: NotificationUseCaseImpl): NotificationUseCase

    @Binds
    @Singleton
    abstract fun bindNeedsActionUseCase(impl: NeedsActionUseCaseImpl): NeedsActionUseCase

    @Binds
    @Singleton
    abstract fun bindVerificationUseCase(impl: VerificationUseCaseImpl): VerificationUseCase
}
