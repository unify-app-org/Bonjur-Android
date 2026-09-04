package com.bonjur.notification.domain.models;

/**
 * Feed row type (API `type`). Full mirror of iOS `NotificationType` — the wire
 * value round-trips into the deep-link action, so the catalog has to match.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u001c\b\u0086\u0081\u0002\u0018\u0000 &2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0002&\'B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f8F\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!j\u0002\b\"j\u0002\b#j\u0002\b$j\u0002\b%\u00a8\u0006("}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationType;", "", "(Ljava/lang/String;I)V", "apiValue", "", "getApiValue", "()Ljava/lang/String;", "prefersRemoteImage", "", "getPrefersRemoteImage", "()Z", "tapDestination", "Lcom/bonjur/notification/domain/models/NotificationType$TapDestination;", "getTapDestination", "()Lcom/bonjur/notification/domain/models/NotificationType$TapDestination;", "EVENT_REMINDER", "BIRTHDAY", "HOLIDAY", "REQUEST_OUTCOME", "VERIFICATION_OUTCOME", "REQUEST_CLUB", "USER_REQUESTED_PRIVATE_CLUB", "REJECTED_USER_FROM_CLUB", "ACCEPTED_USER_FROM_CLUB", "USER_JOINED_PUBLIC_CLUB", "REQUEST_CLUB_VERIFICATION", "VERIFIED_CLUB", "REJECTED_CLUB_VERIFICATION", "REQUEST_HANGOUT", "USER_REQUESTED_PRIVATE_HANGOUT", "REJECTED_USER_FROM_HANGOUT", "ACCEPTED_USER_FROM_HANGOUT", "USER_JOINED_PUBLIC_HANGOUT", "REQUEST_EVENT", "USER_REQUESTED_PRIVATE_EVENT", "REJECTED_USER_FROM_EVENT", "ACCEPTED_USER_FROM_EVENT", "GENERAL", "Companion", "TapDestination", "notification_debug"})
public enum NotificationType {
    /*public static final*/ EVENT_REMINDER /* = new EVENT_REMINDER() */,
    /*public static final*/ BIRTHDAY /* = new BIRTHDAY() */,
    /*public static final*/ HOLIDAY /* = new HOLIDAY() */,
    /*public static final*/ REQUEST_OUTCOME /* = new REQUEST_OUTCOME() */,
    /*public static final*/ VERIFICATION_OUTCOME /* = new VERIFICATION_OUTCOME() */,
    /*public static final*/ REQUEST_CLUB /* = new REQUEST_CLUB() */,
    /*public static final*/ USER_REQUESTED_PRIVATE_CLUB /* = new USER_REQUESTED_PRIVATE_CLUB() */,
    /*public static final*/ REJECTED_USER_FROM_CLUB /* = new REJECTED_USER_FROM_CLUB() */,
    /*public static final*/ ACCEPTED_USER_FROM_CLUB /* = new ACCEPTED_USER_FROM_CLUB() */,
    /*public static final*/ USER_JOINED_PUBLIC_CLUB /* = new USER_JOINED_PUBLIC_CLUB() */,
    /*public static final*/ REQUEST_CLUB_VERIFICATION /* = new REQUEST_CLUB_VERIFICATION() */,
    /*public static final*/ VERIFIED_CLUB /* = new VERIFIED_CLUB() */,
    /*public static final*/ REJECTED_CLUB_VERIFICATION /* = new REJECTED_CLUB_VERIFICATION() */,
    /*public static final*/ REQUEST_HANGOUT /* = new REQUEST_HANGOUT() */,
    /*public static final*/ USER_REQUESTED_PRIVATE_HANGOUT /* = new USER_REQUESTED_PRIVATE_HANGOUT() */,
    /*public static final*/ REJECTED_USER_FROM_HANGOUT /* = new REJECTED_USER_FROM_HANGOUT() */,
    /*public static final*/ ACCEPTED_USER_FROM_HANGOUT /* = new ACCEPTED_USER_FROM_HANGOUT() */,
    /*public static final*/ USER_JOINED_PUBLIC_HANGOUT /* = new USER_JOINED_PUBLIC_HANGOUT() */,
    /*public static final*/ REQUEST_EVENT /* = new REQUEST_EVENT() */,
    /*public static final*/ USER_REQUESTED_PRIVATE_EVENT /* = new USER_REQUESTED_PRIVATE_EVENT() */,
    /*public static final*/ REJECTED_USER_FROM_EVENT /* = new REJECTED_USER_FROM_EVENT() */,
    /*public static final*/ ACCEPTED_USER_FROM_EVENT /* = new ACCEPTED_USER_FROM_EVENT() */,
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
    public final com.bonjur.notification.domain.models.NotificationType.TapDestination getTapDestination() {
        return null;
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/bonjur/notification/domain/models/NotificationType$TapDestination;", "", "(Ljava/lang/String;I)V", "NEEDS_ACTION", "TARGET", "NONE", "notification_debug"})
    public static enum TapDestination {
        /*public static final*/ NEEDS_ACTION /* = new NEEDS_ACTION() */,
        /*public static final*/ TARGET /* = new TARGET() */,
        /*public static final*/ NONE /* = new NONE() */;
        
        TapDestination() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.bonjur.notification.domain.models.NotificationType.TapDestination> getEntries() {
            return null;
        }
    }
}