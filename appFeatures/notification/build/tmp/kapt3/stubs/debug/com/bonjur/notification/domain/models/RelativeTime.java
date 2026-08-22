package com.bonjur.notification.domain.models;

/**
 * Compact relative-time formatter for join-request `createdAt` stamps.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0017\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\u0017\u0010\n\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a2\u0006\u0002\u0010\tJ\u0017\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a2\u0006\u0002\u0010\tJ\u0017\u0010\f\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a2\u0006\u0002\u0010\tJ\u0017\u0010\r\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00a2\u0006\u0002\u0010\tJ\u001f\u0010\u000e\u001a\u00020\b2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u0010\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0011R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/bonjur/notification/domain/models/RelativeTime;", "", "()V", "houseStamp", "Ljava/time/format/DateTimeFormatter;", "parse", "", "value", "", "(Ljava/lang/String;)Ljava/lang/Long;", "parseHouseStamp", "parseIso", "parseOffset", "parseZoneLess", "short", "millis", "now", "(Ljava/lang/Long;J)Ljava/lang/String;", "notification_debug"})
public final class RelativeTime {
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter houseStamp = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.notification.domain.models.RelativeTime INSTANCE = null;
    
    private RelativeTime() {
        super();
    }
    
    /**
     * The format notification-service actually emits: `19-08-2026 17:55:35`.
     * Same house audit stamp events/communities already parse. No zone on the
     * wire — it is server-local, so it is read in the device zone like the rest
     * of the app. ISO shapes stay as fallbacks (the API spec documents those).
     * An unparseable stamp shows no time on the row and sinks it to "Earlier",
     * so keep this tolerant. Mirrors iOS `RelativeTime.parse`.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long parse(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
        return null;
    }
    
    private final java.lang.Long parseHouseStamp(java.lang.String value) {
        return null;
    }
    
    private final java.lang.Long parseIso(java.lang.String value) {
        return null;
    }
    
    private final java.lang.Long parseOffset(java.lang.String value) {
        return null;
    }
    
    private final java.lang.Long parseZoneLess(java.lang.String value) {
        return null;
    }
}