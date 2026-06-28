package com.bonjur.notification.data.dataSource;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J*\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ*\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ*\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ&\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u0014J\u001e\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u0016J&\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u0019\u00a8\u0006\u001a"}, d2 = {"Lcom/bonjur/notification/data/dataSource/NotificationDataSource;", "", "fetchClubRequests", "Lcom/bonjur/network/model/PageNationResponse;", "", "Lcom/bonjur/notification/data/DTOs/ClubJoinRequestDTO;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchHangoutRequests", "Lcom/bonjur/notification/data/DTOs/HangoutJoinRequestDTO;", "fetchPendingClubs", "setClubStatus", "", "clubId", "userId", "", "accept", "", "(ILjava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setClubVerification", "(IZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHangoutStatus", "hangoutId", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debug"})
public abstract interface NotificationDataSource {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchClubRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.ClubJoinRequestDTO>>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchHangoutRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO>>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setClubStatus(int clubId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setHangoutStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String hangoutId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchPendingClubs(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.ClubJoinRequestDTO>>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setClubVerification(int clubId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion);
}