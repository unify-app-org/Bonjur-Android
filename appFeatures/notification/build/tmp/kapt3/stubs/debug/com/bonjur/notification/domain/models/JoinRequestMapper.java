package com.bonjur.notification.domain.models;

/**
 * Normalizes the join-request / pending DTOs into domain models.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0007J\u0010\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\n"}, d2 = {"Lcom/bonjur/notification/domain/models/JoinRequestMapper;", "", "()V", "item", "Lcom/bonjur/notification/domain/models/ActionRequestItem;", "dto", "Lcom/bonjur/notification/data/DTOs/ClubJoinRequestDTO;", "Lcom/bonjur/notification/data/DTOs/HangoutJoinRequestDTO;", "verification", "Lcom/bonjur/notification/domain/models/VerificationItem;", "notification_debug"})
public final class JoinRequestMapper {
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.notification.domain.models.JoinRequestMapper INSTANCE = null;
    
    private JoinRequestMapper() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.bonjur.notification.domain.models.ActionRequestItem item(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.DTOs.ClubJoinRequestDTO dto) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.bonjur.notification.domain.models.ActionRequestItem item(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.DTOs.HangoutJoinRequestDTO dto) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.bonjur.notification.domain.models.VerificationItem verification(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.DTOs.ClubJoinRequestDTO dto) {
        return null;
    }
}