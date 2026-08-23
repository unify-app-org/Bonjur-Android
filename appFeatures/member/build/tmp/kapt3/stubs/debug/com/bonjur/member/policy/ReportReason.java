package com.bonjur.member.policy;

/**
 * Hardcoded report reasons backing the "Report user" sheet.
 * `displayTitle` resolves through [LanguageManager] on every read so the sheet
 * follows an in-app language switch.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000j\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/bonjur/member/policy/ReportReason;", "", "code", "", "titleRes", "", "(Ljava/lang/String;ILjava/lang/String;I)V", "getCode", "()Ljava/lang/String;", "displayTitle", "getDisplayTitle", "FAKE_PROFILE", "INAPPROPRIATE_PROFILE_PICTURE", "INAPPROPRIATE_PROFILE_TEXT", "INAPPROPRIATE_OFFERS", "OFFENSIVE", "UNDERAGE", "SCAM_AND_COMMERCIAL", "OTHER", "member_debug"})
public enum ReportReason {
    /*public static final*/ FAKE_PROFILE /* = new FAKE_PROFILE(null, 0) */,
    /*public static final*/ INAPPROPRIATE_PROFILE_PICTURE /* = new INAPPROPRIATE_PROFILE_PICTURE(null, 0) */,
    /*public static final*/ INAPPROPRIATE_PROFILE_TEXT /* = new INAPPROPRIATE_PROFILE_TEXT(null, 0) */,
    /*public static final*/ INAPPROPRIATE_OFFERS /* = new INAPPROPRIATE_OFFERS(null, 0) */,
    /*public static final*/ OFFENSIVE /* = new OFFENSIVE(null, 0) */,
    /*public static final*/ UNDERAGE /* = new UNDERAGE(null, 0) */,
    /*public static final*/ SCAM_AND_COMMERCIAL /* = new SCAM_AND_COMMERCIAL(null, 0) */,
    /*public static final*/ OTHER /* = new OTHER(null, 0) */;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String code = null;
    private final int titleRes = 0;
    
    ReportReason(java.lang.String code, int titleRes) {
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
    public static kotlin.enums.EnumEntries<com.bonjur.member.policy.ReportReason> getEntries() {
        return null;
    }
}