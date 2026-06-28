package com.bonjur.notification.domain.useCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u00a6@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/bonjur/notification/domain/useCase/VerificationUseCase;", "", "fetchPending", "Lcom/bonjur/notification/domain/models/VerificationPageResult;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setStatus", "", "clubId", "accept", "", "(IZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debug"})
public abstract interface VerificationUseCase {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchPending(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.VerificationPageResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setStatus(int clubId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}