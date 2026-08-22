package com.bonjur.notification.domain.models;

/**
 * Which entity a feed row deep-links into (API `targetType`).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u0000 \t2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\tB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\n"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationTargetType;", "", "(Ljava/lang/String;I)V", "EVENT", "CLUB", "HANGOUT", "COMMUNITY", "USER", "NONE", "Companion", "notification_debug"})
public enum NotificationTargetType {
    /*public static final*/ EVENT /* = new EVENT() */,
    /*public static final*/ CLUB /* = new CLUB() */,
    /*public static final*/ HANGOUT /* = new HANGOUT() */,
    /*public static final*/ COMMUNITY /* = new COMMUNITY() */,
    /*public static final*/ USER /* = new USER() */,
    /*public static final*/ NONE /* = new NONE() */;
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.notification.domain.models.NotificationTargetType.Companion Companion = null;
    
    NotificationTargetType() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.bonjur.notification.domain.models.NotificationTargetType> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationTargetType$Companion;", "", "()V", "from", "Lcom/bonjur/notification/domain/models/NotificationTargetType;", "api", "", "notification_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.NotificationTargetType from(@org.jetbrains.annotations.Nullable()
        java.lang.String api) {
            return null;
        }
    }
}