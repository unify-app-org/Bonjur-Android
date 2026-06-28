package com.bonjur.notification.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\fH'J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000fH'\u00a8\u0006\u0010"}, d2 = {"Lcom/bonjur/notification/di/NotificationModule;", "", "()V", "bindNeedsActionUseCase", "Lcom/bonjur/notification/domain/useCase/NeedsActionUseCase;", "impl", "Lcom/bonjur/notification/domain/useCase/NeedsActionUseCaseImpl;", "bindNotificationDataSource", "Lcom/bonjur/notification/data/dataSource/NotificationDataSource;", "Lcom/bonjur/notification/data/dataSource/NotificationDataSourceImpl;", "bindNotificationUseCase", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "Lcom/bonjur/notification/domain/useCase/NotificationUseCaseImpl;", "bindVerificationUseCase", "Lcom/bonjur/notification/domain/useCase/VerificationUseCase;", "Lcom/bonjur/notification/domain/useCase/VerificationUseCaseImpl;", "notification_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class NotificationModule {
    
    public NotificationModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.bonjur.notification.data.dataSource.NotificationDataSource bindNotificationDataSource(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.dataSource.NotificationDataSourceImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.bonjur.notification.domain.useCase.NotificationUseCase bindNotificationUseCase(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.useCase.NotificationUseCaseImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.bonjur.notification.domain.useCase.NeedsActionUseCase bindNeedsActionUseCase(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.useCase.NeedsActionUseCaseImpl impl);
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.bonjur.notification.domain.useCase.VerificationUseCase bindVerificationUseCase(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.useCase.VerificationUseCaseImpl impl);
}