package com.bonjur.notification.domain.models;

/**
 * Maps notification-service rows into feed items and groups a flat page stream
 * into the date buckets the feed renders (Today / Yesterday / This week /
 * Earlier). Mirrors iOS `NotificationFeedMapper`.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\tJ.\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011J\u001a\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00070\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationFeedMapper;", "", "()V", "REJECTION_TYPES", "", "Lcom/bonjur/notification/domain/models/NotificationType;", "item", "Lcom/bonjur/notification/domain/models/NotificationFeedItem;", "dto", "Lcom/bonjur/notification/data/DTOs/NotificationDTO;", "sections", "", "Lcom/bonjur/notification/domain/models/NotificationSection;", "items", "now", "", "zone", "Ljava/time/ZoneId;", "sorted", "notification_debug"})
public final class NotificationFeedMapper {
    
    /**
     * Rejection outcomes are the only rows whose note box carries the admin reason.
     */
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<com.bonjur.notification.domain.models.NotificationType> REJECTION_TYPES = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.notification.domain.models.NotificationFeedMapper INSTANCE = null;
    
    private NotificationFeedMapper() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.bonjur.notification.domain.models.NotificationFeedItem item(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.data.DTOs.NotificationDTO dto) {
        return null;
    }
    
    /**
     * Newest first; rows missing createdAt keep their relative order and sink
     * to the bottom. The server order is not relied on — pages are
     * concatenated as they load, so the merged list is sorted client-side.
     * Mirrors iOS `NotificationFeedMapper.sorted`.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.notification.domain.models.NotificationFeedItem> sorted(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.notification.domain.models.NotificationFeedItem> items) {
        return null;
    }
    
    /**
     * Buckets are filled newest-first; items without a parseable createdAt
     * sink to "Earlier".
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.notification.domain.models.NotificationSection> sections(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.notification.domain.models.NotificationFeedItem> items, long now, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zone) {
        return null;
    }
}