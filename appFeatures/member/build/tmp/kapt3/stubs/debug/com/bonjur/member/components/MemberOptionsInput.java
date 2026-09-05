package com.bonjur.member.components;

/**
 * Input for the shared member 3-dot options sheet. Built by each activity's
 * detail layer from `MemberOptionsPolicy`. Mirrors iOS `MemberOptionsInput`.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B[\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\r0\f\u0012\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\u0002\u0010\u0010J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\tH\u00c6\u0003J\t\u0010!\u001a\u00020\tH\u00c6\u0003J\u0015\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003J\u0015\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0003Jm\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\u0014\b\u0002\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\r0\f2\u0014\b\u0002\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\fH\u00c6\u0001J\u0013\u0010%\u001a\u00020\t2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010'\u001a\u00020(H\u00d6\u0001J\t\u0010)\u001a\u00020\u0003H\u00d6\u0001R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001d\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001d\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001b\u00a8\u0006*"}, d2 = {"Lcom/bonjur/member/components/MemberOptionsInput;", "", "memberName", "", "currentRole", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "assignableRoles", "", "showChangeRole", "", "showReport", "onAssignRole", "Lkotlin/Function1;", "", "onReport", "Lcom/bonjur/member/policy/ReportReason;", "(Ljava/lang/String;Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;Ljava/util/List;ZZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getAssignableRoles", "()Ljava/util/List;", "getCurrentRole", "()Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "getMemberName", "()Ljava/lang/String;", "getOnAssignRole", "()Lkotlin/jvm/functions/Function1;", "getOnReport", "getShowChangeRole", "()Z", "getShowReport", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "member_debug"})
public final class MemberOptionsInput {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String memberName = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole currentRole = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole> assignableRoles = null;
    private final boolean showChangeRole = false;
    private final boolean showReport = false;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.Unit> onAssignRole = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.bonjur.member.policy.ReportReason, kotlin.Unit> onReport = null;
    
    public MemberOptionsInput(@org.jetbrains.annotations.NotNull()
    java.lang.String memberName, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole currentRole, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole> assignableRoles, boolean showChangeRole, boolean showReport, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.Unit> onAssignRole, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.bonjur.member.policy.ReportReason, kotlin.Unit> onReport) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMemberName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole getCurrentRole() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole> getAssignableRoles() {
        return null;
    }
    
    public final boolean getShowChangeRole() {
        return false;
    }
    
    public final boolean getShowReport() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.Unit> getOnAssignRole() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.bonjur.member.policy.ReportReason, kotlin.Unit> getOnReport() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole> component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.Unit> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.bonjur.member.policy.ReportReason, kotlin.Unit> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.member.components.MemberOptionsInput copy(@org.jetbrains.annotations.NotNull()
    java.lang.String memberName, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole currentRole, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole> assignableRoles, boolean showChangeRole, boolean showReport, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.Unit> onAssignRole, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.bonjur.member.policy.ReportReason, kotlin.Unit> onReport) {
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