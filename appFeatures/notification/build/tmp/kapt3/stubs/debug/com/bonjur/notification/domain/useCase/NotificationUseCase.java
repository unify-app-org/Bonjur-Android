package com.bonjur.notification.domain.useCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\u000b\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\f\u001a\u00020\rH\u00a6@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u000e"}, d2 = {"Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "", "fetchFeedPage", "Lcom/bonjur/notification/domain/models/NotificationFeedPage;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchRequestCounts", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchUnreadCount", "fetchVerificationCount", "markAllRead", "", "notification_debug"})
public abstract interface NotificationUseCase {
    
    /**
     * One page of the live notification feed (`api/ns`).
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchFeedPage(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.NotificationFeedPage> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAllRead(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Live pending-request total for the banner (club + hangout + event).
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchRequestCounts(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    /**
     * Admin-only pending-verification total; throws (403) when not an admin.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchVerificationCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    /**
     * Unread notification total for the Discover bell badge.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchUnreadCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}