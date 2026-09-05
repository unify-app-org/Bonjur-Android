package com.bonjur.notification.domain.models;

/**
 * A single pending join request, normalized across club + hangout sources.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0010JZ\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bH\u00c6\u0001\u00a2\u0006\u0002\u0010 J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\t\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0015\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000eR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000e\u00a8\u0006'"}, d2 = {"Lcom/bonjur/notification/domain/models/ActionRequestItem;", "", "id", "", "kind", "Lcom/bonjur/notification/domain/models/ActionRequestKind;", "requesterId", "requesterName", "targetName", "avatarUrl", "createdAtMillis", "", "(Ljava/lang/String;Lcom/bonjur/notification/domain/models/ActionRequestKind;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;)V", "getAvatarUrl", "()Ljava/lang/String;", "getCreatedAtMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getId", "getKind", "()Lcom/bonjur/notification/domain/models/ActionRequestKind;", "getRequesterId", "getRequesterName", "getTargetName", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(Ljava/lang/String;Lcom/bonjur/notification/domain/models/ActionRequestKind;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;)Lcom/bonjur/notification/domain/models/ActionRequestItem;", "equals", "", "other", "hashCode", "", "toString", "notification_debug"})
public final class ActionRequestItem {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.models.ActionRequestKind kind = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String requesterId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String requesterName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String targetName = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String avatarUrl = null;
    
    /**
     * Epoch millis of createdAt, or null. Used for sort + relative stamp.
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long createdAtMillis = null;
    
    public ActionRequestItem(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.models.ActionRequestKind kind, @org.jetbrains.annotations.Nullable()
    java.lang.String requesterId, @org.jetbrains.annotations.NotNull()
    java.lang.String requesterName, @org.jetbrains.annotations.NotNull()
    java.lang.String targetName, @org.jetbrains.annotations.Nullable()
    java.lang.String avatarUrl, @org.jetbrains.annotations.Nullable()
    java.lang.Long createdAtMillis) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.ActionRequestKind getKind() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getRequesterId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRequesterName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTargetName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAvatarUrl() {
        return null;
    }
    
    /**
     * Epoch millis of createdAt, or null. Used for sort + relative stamp.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCreatedAtMillis() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.ActionRequestKind component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.ActionRequestItem copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.models.ActionRequestKind kind, @org.jetbrains.annotations.Nullable()
    java.lang.String requesterId, @org.jetbrains.annotations.NotNull()
    java.lang.String requesterName, @org.jetbrains.annotations.NotNull()
    java.lang.String targetName, @org.jetbrains.annotations.Nullable()
    java.lang.String avatarUrl, @org.jetbrains.annotations.Nullable()
    java.lang.Long createdAtMillis) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}