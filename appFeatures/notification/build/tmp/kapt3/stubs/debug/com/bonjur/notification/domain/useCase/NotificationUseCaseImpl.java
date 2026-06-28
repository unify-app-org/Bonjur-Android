package com.bonjur.notification.domain.useCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00020\tH\u0096@\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\n\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/bonjur/notification/domain/useCase/NotificationUseCaseImpl;", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "dataSource", "Lcom/bonjur/notification/data/dataSource/NotificationDataSource;", "(Lcom/bonjur/notification/data/dataSource/NotificationDataSource;)V", "fetchInbox", "Lcom/bonjur/notification/domain/models/NotificationInbox;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchRequestCount", "", "markAllRead", "", "notification_debug"})
public final class NotificationUseCaseImpl implements com.bonjur.notification.domain.useCase.NotificationUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.data.dataSource.NotificationDataSource dataSource = null;
    
    @javax.inject.Inject()
    public NotificationUseCaseImpl(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.dataSource.NotificationDataSource dataSource) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchInbox(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.NotificationInbox> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object markAllRead(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchRequestCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
}