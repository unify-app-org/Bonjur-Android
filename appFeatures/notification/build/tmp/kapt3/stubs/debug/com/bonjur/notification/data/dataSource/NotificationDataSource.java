package com.bonjur.notification.data.dataSource;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\bf\u0018\u00002\u00020\u0001J*\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ*\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ*\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ*\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ*\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\u0011\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\u0014H\u00a6@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017H\u00a6@\u00a2\u0006\u0002\u0010\u0018J&\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001eH\u00a6@\u00a2\u0006\u0002\u0010\u001fJ(\u0010 \u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010!\u001a\u0004\u0018\u00010\u0017H\u00a6@\u00a2\u0006\u0002\u0010\"J&\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001eH\u00a6@\u00a2\u0006\u0002\u0010%J&\u0010&\u001a\u00020\u001a2\u0006\u0010'\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001eH\u00a6@\u00a2\u0006\u0002\u0010%\u00a8\u0006("}, d2 = {"Lcom/bonjur/notification/data/dataSource/NotificationDataSource;", "", "fetchClubRequests", "Lcom/bonjur/network/model/PageNationResponse;", "", "Lcom/bonjur/notification/data/DTOs/ClubJoinRequestDTO;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchEventRequests", "Lcom/bonjur/notification/data/DTOs/EventJoinRequestDTO;", "fetchFeed", "Lcom/bonjur/notification/data/DTOs/NotificationDTO;", "fetchHangoutRequests", "Lcom/bonjur/notification/data/DTOs/HangoutJoinRequestDTO;", "fetchPendingClubs", "fetchUnreadCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAllRead", "", "markRead", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setClubStatus", "", "clubId", "userId", "accept", "", "(ILjava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setClubVerification", "rejectionReason", "(IZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEventStatus", "eventId", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHangoutStatus", "hangoutId", "notification_debug"})
public abstract interface NotificationDataSource {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchFeed(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.NotificationDTO>>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchUnreadCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAllRead(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * POST api/ns/v1/notifications/read/{id} — single-row read.
     */
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markRead(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchClubRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.ClubJoinRequestDTO>>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchHangoutRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO>>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchEventRequests(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.EventJoinRequestDTO>>> $completion);
    
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
    public abstract java.lang.Object setEventStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String eventId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchPendingClubs(int page, int size, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.bonjur.network.model.PageNationResponse<java.util.List<com.bonjur.notification.data.DTOs.ClubJoinRequestDTO>>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setClubVerification(int clubId, boolean accept, @org.jetbrains.annotations.Nullable()
    java.lang.String rejectionReason, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super byte[]> $completion);
}