package com.bonjur.notification.domain.models;

/**
 * Maps notification-service rows into feed items and groups a flat page stream
 * into the date buckets the feed renders (Today / Yesterday / This week /
 * Earlier). Mirrors iOS `NotificationFeedMapper`.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006J.\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\b2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationFeedMapper;", "", "()V", "item", "Lcom/bonjur/notification/domain/models/NotificationFeedItem;", "dto", "Lcom/bonjur/notification/data/DTOs/NotificationDTO;", "sections", "", "Lcom/bonjur/notification/domain/models/NotificationSection;", "items", "now", "", "zone", "Ljava/time/ZoneId;", "notification_debug"})
public final class NotificationFeedMapper {
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
     * Buckets keep the incoming (server, newest-first) order within each
     * section; items without a parseable createdAt sink to "Earlier".
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.notification.domain.models.NotificationSection> sections(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.notification.domain.models.NotificationFeedItem> items, long now, @org.jetbrains.annotations.NotNull()
    java.time.ZoneId zone) {
        return null;
    }
}