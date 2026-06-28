package com.bonjur.notification.domain.useCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\bH\u00a6@\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\t"}, d2 = {"Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "", "fetchInbox", "Lcom/bonjur/notification/domain/models/NotificationInbox;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchRequestCount", "", "markAllRead", "", "notification_debug"})
public abstract interface NotificationUseCase {
    
    /**
     * Mock feed until the notification service ships (mirrors iOS mock).
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchInbox(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.NotificationInbox> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAllRead(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Live pending-request total for the banner (club + hangout totalElements).
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchRequestCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}