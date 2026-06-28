package com.bonjur.member.list;

/**
 * Cross-module input for the shared members list. The closures hit each activity's own
 * endpoint/useCase, so the screen itself stays activity-agnostic (iOS injects the same way).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B\u00ff\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012]\u0010\t\u001aY\b\u0001\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000f\u0012\u0015\u0012\u0013\u0018\u00010\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00010\n\u0012J\b\u0002\u0010\u0013\u001aD\b\u0001\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u0014\u0012#\b\u0002\u0010\u0018\u001a\u001d\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00170\u0019\u00a2\u0006\u0002\u0010\u001bJ\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\bH\u00c6\u0003Je\u0010/\u001aY\b\u0001\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000f\u0012\u0015\u0012\u0013\u0018\u00010\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00010\nH\u00c6\u0003\u00a2\u0006\u0002\u0010$JP\u00100\u001aD\b\u0001\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u0014H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001fJ$\u00101\u001a\u001d\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00170\u0019H\u00c6\u0003J\u008a\u0002\u00102\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2_\b\u0002\u0010\t\u001aY\b\u0001\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000f\u0012\u0015\u0012\u0013\u0018\u00010\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00010\n2J\b\u0002\u0010\u0013\u001aD\b\u0001\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u00142#\b\u0002\u0010\u0018\u001a\u001d\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00170\u0019H\u00c6\u0001\u00a2\u0006\u0002\u00103J\u0013\u00104\u001a\u0002052\b\u00106\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00107\u001a\u00020\u000bH\u00d6\u0001J\t\u00108\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dRU\u0010\u0013\u001aD\b\u0001\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\u0014\u00a2\u0006\n\n\u0002\u0010 \u001a\u0004\b\u001e\u0010\u001fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"Rj\u0010\t\u001aY\b\u0001\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u00110\u000b\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000f\u0012\u0015\u0012\u0013\u0018\u00010\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00010\n\u00a2\u0006\n\n\u0002\u0010%\u001a\u0004\b#\u0010$R,\u0010\u0018\u001a\u001d\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00170\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\"R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*\u00a8\u00069"}, d2 = {"Lcom/bonjur/member/list/MemberListInputData;", "", "title", "", "viewerRole", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "currentUserId", "activityType", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$ActivityType;", "loadPage", "Lkotlin/Function4;", "", "Lkotlin/ParameterName;", "name", "page", "size", "keyword", "Lkotlin/coroutines/Continuation;", "Lcom/bonjur/member/model/MembersPage;", "assignRole", "Lkotlin/Function3;", "userId", "role", "", "onMemberTapped", "Lkotlin/Function1;", "memberId", "(Ljava/lang/String;Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;Ljava/lang/String;Lcom/bonjur/designSystem/commonModel/AppUIEntities$ActivityType;Lkotlin/jvm/functions/Function4;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;)V", "getActivityType", "()Lcom/bonjur/designSystem/commonModel/AppUIEntities$ActivityType;", "getAssignRole", "()Lkotlin/jvm/functions/Function3;", "Lkotlin/jvm/functions/Function3;", "getCurrentUserId", "()Ljava/lang/String;", "getLoadPage", "()Lkotlin/jvm/functions/Function4;", "Lkotlin/jvm/functions/Function4;", "getOnMemberTapped", "()Lkotlin/jvm/functions/Function1;", "getTitle", "getViewerRole", "()Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(Ljava/lang/String;Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;Ljava/lang/String;Lcom/bonjur/designSystem/commonModel/AppUIEntities$ActivityType;Lkotlin/jvm/functions/Function4;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;)Lcom/bonjur/member/list/MemberListInputData;", "equals", "", "other", "hashCode", "toString", "member_debug"})
public final class MemberListInputData {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole viewerRole = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String currentUserId = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.designSystem.commonModel.AppUIEntities.ActivityType activityType = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function4<java.lang.Integer, java.lang.Integer, java.lang.String, kotlin.coroutines.Continuation<? super com.bonjur.member.model.MembersPage>, java.lang.Object> loadPage = null;
    @org.jetbrains.annotations.Nullable()
    private final kotlin.jvm.functions.Function3<java.lang.String, com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> assignRole = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onMemberTapped = null;
    
    public MemberListInputData(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole viewerRole, @org.jetbrains.annotations.Nullable()
    java.lang.String currentUserId, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.ActivityType activityType, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.String, ? super kotlin.coroutines.Continuation<? super com.bonjur.member.model.MembersPage>, ? extends java.lang.Object> loadPage, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function3<? super java.lang.String, ? super com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> assignRole, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onMemberTapped) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole getViewerRole() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCurrentUserId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.designSystem.commonModel.AppUIEntities.ActivityType getActivityType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function4<java.lang.Integer, java.lang.Integer, java.lang.String, kotlin.coroutines.Continuation<? super com.bonjur.member.model.MembersPage>, java.lang.Object> getLoadPage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function3<java.lang.String, com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> getAssignRole() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> getOnMemberTapped() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.designSystem.commonModel.AppUIEntities.ActivityType component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function4<java.lang.Integer, java.lang.Integer, java.lang.String, kotlin.coroutines.Continuation<? super com.bonjur.member.model.MembersPage>, java.lang.Object> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function3<java.lang.String, com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.member.list.MemberListInputData copy(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole viewerRole, @org.jetbrains.annotations.Nullable()
    java.lang.String currentUserId, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.ActivityType activityType, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function4<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.String, ? super kotlin.coroutines.Continuation<? super com.bonjur.member.model.MembersPage>, ? extends java.lang.Object> loadPage, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function3<? super java.lang.String, ? super com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> assignRole, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onMemberTapped) {
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