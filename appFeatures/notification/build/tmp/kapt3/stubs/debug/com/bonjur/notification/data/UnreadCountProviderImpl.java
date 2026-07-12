package com.bonjur.notification.data;

/**
 * Notification-side implementation of [UnreadCountProvider]. Bound app-wide so
 * the Discover bell can read the unread total without depending on this module.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/bonjur/notification/data/UnreadCountProviderImpl;", "Lcom/bonjur/navigation/UnreadCountProvider;", "useCase", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "(Lcom/bonjur/notification/domain/useCase/NotificationUseCase;)V", "unreadCount", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debug"})
public final class UnreadCountProviderImpl implements com.bonjur.navigation.UnreadCountProvider {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.useCase.NotificationUseCase useCase = null;
    
    @javax.inject.Inject()
    public UnreadCountProviderImpl(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.useCase.NotificationUseCase useCase) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object unreadCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
}