package com.bonjur.member.policy;

/**
 * Single source of truth for who may change roles and which roles they may grant.
 * Both the shared options sheet and the detail view models call into this so the
 * rules can never diverge across activities.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0005J\u001e\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\bJ\u000e\u0010\r\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00020\u0005\u00a8\u0006\u000e"}, d2 = {"Lcom/bonjur/member/policy/MemberOptionsPolicy;", "", "()V", "assignableRoles", "", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "viewer", "canChangeRole", "", "activity", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$ActivityType;", "isSelf", "canReport", "canReportActivity", "member_debug"})
public final class MemberOptionsPolicy {
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.member.policy.MemberOptionsPolicy INSTANCE = null;
    
    private MemberOptionsPolicy() {
        super();
    }
    
    /**
     * Roles a viewer may grant to another member.
     * - President: all assignable roles.
     * - Vice president: only Member and Event creator.
     * - Anyone else: none.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole> assignableRoles(@org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole viewer) {
        return null;
    }
    
    /**
     * Whether the "Change role" row should be shown.
     * Only clubs and communities have roles; never on your own row.
     */
    public final boolean canChangeRole(@org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole viewer, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.ActivityType activity, boolean isSelf) {
        return false;
    }
    
    /**
     * Whether the "Report user" row should be shown. Everyone but yourself.
     */
    public final boolean canReport(boolean isSelf) {
        return false;
    }
    
    /**
     * Whether the "Report <activity>" row should be shown in the activity options
     * sheet. Everyone may report the activity except its creator/owner.
     */
    public final boolean canReportActivity(@org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole viewer) {
        return false;
    }
}