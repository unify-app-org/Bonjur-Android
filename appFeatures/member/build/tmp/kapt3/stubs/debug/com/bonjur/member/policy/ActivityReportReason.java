package com.bonjur.member.policy;

/**
 * Report reasons for an activity itself (club / event / hangout).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\f\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000j\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/member/policy/ActivityReportReason;", "", "code", "", "titleRes", "", "(Ljava/lang/String;ILjava/lang/String;I)V", "getCode", "()Ljava/lang/String;", "displayTitle", "getDisplayTitle", "INAPPROPRIATE_CONTENT", "SPAM", "SCAM_AND_COMMERCIAL", "HARASSMENT", "MISLEADING_INFO", "OTHER", "member_debug"})
public enum ActivityReportReason {
    /*public static final*/ INAPPROPRIATE_CONTENT /* = new INAPPROPRIATE_CONTENT(null, 0) */,
    /*public static final*/ SPAM /* = new SPAM(null, 0) */,
    /*public static final*/ SCAM_AND_COMMERCIAL /* = new SCAM_AND_COMMERCIAL(null, 0) */,
    /*public static final*/ HARASSMENT /* = new HARASSMENT(null, 0) */,
    /*public static final*/ MISLEADING_INFO /* = new MISLEADING_INFO(null, 0) */,
    /*public static final*/ OTHER /* = new OTHER(null, 0) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String code = null;
    private final int titleRes = 0;
    
    ActivityReportReason(java.lang.String code, int titleRes) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDisplayTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.bonjur.member.policy.ActivityReportReason> getEntries() {
        return null;
    }
}