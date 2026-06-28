package com.bonjur.notification.domain.useCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00a2\u0006\u0002\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/notification/domain/useCase/VerificationUseCaseImpl;", "Lcom/bonjur/notification/domain/useCase/VerificationUseCase;", "dataSource", "Lcom/bonjur/notification/data/dataSource/NotificationDataSource;", "(Lcom/bonjur/notification/data/dataSource/NotificationDataSource;)V", "fetchPending", "Lcom/bonjur/notification/domain/models/VerificationPageResult;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setStatus", "", "clubId", "accept", "", "(IZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debug"})
public final class VerificationUseCaseImpl implements com.bonjur.notification.domain.useCase.VerificationUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.data.dataSource.NotificationDataSource dataSource = null;
    
    @javax.inject.Inject()
    public VerificationUseCaseImpl(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.dataSource.NotificationDataSource dataSource) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchPending(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.VerificationPageResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setStatus(int clubId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}