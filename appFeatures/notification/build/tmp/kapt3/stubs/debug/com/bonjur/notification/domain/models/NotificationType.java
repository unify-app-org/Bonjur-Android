package com.bonjur.notification.domain.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\b\u0086\u0081\u0002\u0018\u0000 \u00112\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0011B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010\u00a8\u0006\u0012"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationType;", "", "(Ljava/lang/String;I)V", "apiValue", "", "getApiValue", "()Ljava/lang/String;", "prefersRemoteImage", "", "getPrefersRemoteImage", "()Z", "BIRTHDAY", "HOLIDAY", "EVENT_REMINDER", "REQUEST_OUTCOME", "VERIFICATION_OUTCOME", "GENERAL", "Companion", "notification_debug"})
public enum NotificationType {
    /*public static final*/ BIRTHDAY /* = new BIRTHDAY() */,
    /*public static final*/ HOLIDAY /* = new HOLIDAY() */,
    /*public static final*/ EVENT_REMINDER /* = new EVENT_REMINDER() */,
    /*public static final*/ REQUEST_OUTCOME /* = new REQUEST_OUTCOME() */,
    /*public static final*/ VERIFICATION_OUTCOME /* = new VERIFICATION_OUTCOME() */,
    /*public static final*/ GENERAL /* = new GENERAL() */;
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.notification.domain.models.NotificationType.Companion Companion = null;
    
    NotificationType() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getApiValue() {
        return null;
    }
    
    public final boolean getPrefersRemoteImage() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.bonjur.notification.domain.models.NotificationType> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationType$Companion;", "", "()V", "from", "Lcom/bonjur/notification/domain/models/NotificationType;", "api", "", "notification_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.NotificationType from(@org.jetbrains.annotations.NotNull()
        java.lang.String api) {
            return null;
        }
    }
}