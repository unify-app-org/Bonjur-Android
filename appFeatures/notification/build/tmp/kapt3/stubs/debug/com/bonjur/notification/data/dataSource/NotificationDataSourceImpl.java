package com.bonjur.notification.data.dataSource;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\u000f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J*\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\rJ*\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\b0\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\rJ*\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\rJ$\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00130\u00122\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0002J&\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001aJ\u001e\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001cJ&\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001f\u00a8\u0006 "}, d2 = {"Lcom/bonjur/notification/data/dataSource/NotificationDataSourceImpl;", "Lcom/bonjur/network/APIClient/NetworkService;", "Lcom/bonjur/notification/data/dataSource/NotificationDataSource;", "apiClient", "Lcom/bonjur/network/APIClient/ApiClientProtocol;", "(Lcom/bonjur/network/APIClient/ApiClientProtocol;)V", "fetchClubRequests", "Lcom/bonjur/network/model/PageNationResponse;", "", "Lcom/bonjur/notification/data/DTOs/ClubJoinRequestDTO;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchHangoutRequests", "Lcom/bonjur/notification/data/DTOs/HangoutJoinRequestDTO;", "fetchPendingClubs", "query", "", "", "setClubStatus", "", "clubId", "userId", "accept", "", "(ILjava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setClubVerification", "(IZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHangoutStatus", "hangoutId", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debug"})
public final class NotificationDataSourceImpl extends com.bonjur.network.APIClient.NetworkService implements com.bonjur.notification.data.dataSource.NotificationDataSource {
    
    @javax.inject.Inject()
    public NotificationDataSourceImpl(@org.jetbrains.annotations.NotNull()
    com.bonjur.network.APIClient.ApiClientProtocol apiClient) {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchClubRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.ClubJoinRequestDTO>>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchHangoutRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO>>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setClubStatus(int clubId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setHangoutStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String hangoutId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchPendingClubs(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.ClubJoinRequestDTO>>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setClubVerification(int clubId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion) {
        return null;
    }
    
    private final java.util.Map<java.lang.String, java.lang.String> query(int page, int size) {
        return null;
    }
}