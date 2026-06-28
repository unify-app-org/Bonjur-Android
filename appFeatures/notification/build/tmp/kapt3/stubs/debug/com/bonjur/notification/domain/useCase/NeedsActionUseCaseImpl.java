package com.bonjur.notification.domain.useCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\f\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\rJ&\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015J&\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/bonjur/notification/domain/useCase/NeedsActionUseCaseImpl;", "Lcom/bonjur/notification/domain/useCase/NeedsActionUseCase;", "dataSource", "Lcom/bonjur/notification/data/dataSource/NotificationDataSource;", "(Lcom/bonjur/notification/data/dataSource/NotificationDataSource;)V", "fetchClubRequests", "Lcom/bonjur/notification/domain/models/RequestPageResult;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchHangoutRequests", "fetchVerificationCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setClubStatus", "", "clubId", "userId", "", "accept", "", "(ILjava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHangoutStatus", "hangoutId", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debug"})
public final class NeedsActionUseCaseImpl implements com.bonjur.notification.domain.useCase.NeedsActionUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.data.dataSource.NotificationDataSource dataSource = null;
    
    @javax.inject.Inject()
    public NeedsActionUseCaseImpl(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.dataSource.NotificationDataSource dataSource) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchClubRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchHangoutRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setClubStatus(int clubId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setHangoutStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String hangoutId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchVerificationCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
}