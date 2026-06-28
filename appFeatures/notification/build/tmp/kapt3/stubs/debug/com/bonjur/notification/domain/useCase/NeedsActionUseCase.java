package com.bonjur.notification.domain.useCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\t\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\nJ&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0012J&\u0010\u0013\u001a\u00020\f2\u0006\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u00a6@\u00a2\u0006\u0002\u0010\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/bonjur/notification/domain/useCase/NeedsActionUseCase;", "", "fetchClubRequests", "Lcom/bonjur/notification/domain/models/RequestPageResult;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchHangoutRequests", "fetchVerificationCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setClubStatus", "", "clubId", "userId", "", "accept", "", "(ILjava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHangoutStatus", "hangoutId", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debug"})
public abstract interface NeedsActionUseCase {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchClubRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchHangoutRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setClubStatus(int clubId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setHangoutStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String hangoutId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * Admin-only pending-verification total. Throws (e.g. 403) when not an admin.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchVerificationCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}