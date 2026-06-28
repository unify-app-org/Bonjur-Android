package com.bonjur.notification.domain.models;

/**
 * Full payload backing the notification inbox screen.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087\b\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J#\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0017"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationInbox;", "", "action", "Lcom/bonjur/notification/domain/models/NeedsActionSummary;", "sections", "", "Lcom/bonjur/notification/domain/models/NotificationSection;", "(Lcom/bonjur/notification/domain/models/NeedsActionSummary;Ljava/util/List;)V", "getAction", "()Lcom/bonjur/notification/domain/models/NeedsActionSummary;", "getSections", "()Ljava/util/List;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Companion", "notification_debug"})
public final class NotificationInbox {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.models.NeedsActionSummary action = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.bonjur.notification.domain.models.NotificationSection> sections = null;
    @org.jetbrains.annotations.NotNull()
    private static final com.bonjur.notification.domain.models.NotificationInbox empty = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.notification.domain.models.NotificationInbox.Companion Companion = null;
    
    public NotificationInbox(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.models.NeedsActionSummary action, @org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.notification.domain.models.NotificationSection> sections) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.NeedsActionSummary getAction() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.notification.domain.models.NotificationSection> getSections() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.NeedsActionSummary component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.notification.domain.models.NotificationSection> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.NotificationInbox copy(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.models.NeedsActionSummary action, @org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.notification.domain.models.NotificationSection> sections) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationInbox$Companion;", "", "()V", "empty", "Lcom/bonjur/notification/domain/models/NotificationInbox;", "getEmpty", "()Lcom/bonjur/notification/domain/models/NotificationInbox;", "notification_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.NotificationInbox getEmpty() {
            return null;
        }
    }
}