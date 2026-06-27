package com.bonjur.member.list;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0006\u0003\u0004\u0005\u0006\u0007\bB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0006\t\n\u000b\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/bonjur/member/list/MemberListAction;", "Lcom/bonjur/appfoundation/FeatureAction;", "()V", "AssignRole", "BackTapped", "LoadMore", "MemberTapped", "OnAppear", "SearchTextChanged", "Lcom/bonjur/member/list/MemberListAction$AssignRole;", "Lcom/bonjur/member/list/MemberListAction$BackTapped;", "Lcom/bonjur/member/list/MemberListAction$LoadMore;", "Lcom/bonjur/member/list/MemberListAction$MemberTapped;", "Lcom/bonjur/member/list/MemberListAction$OnAppear;", "Lcom/bonjur/member/list/MemberListAction$SearchTextChanged;", "member_debug"})
public abstract class MemberListAction implements com.bonjur.appfoundation.FeatureAction {
    
    private MemberListAction() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/bonjur/member/list/MemberListAction$AssignRole;", "Lcom/bonjur/member/list/MemberListAction;", "userId", "", "role", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "(Ljava/lang/String;Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;)V", "getRole", "()Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "getUserId", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "member_debug"})
    public static final class AssignRole extends com.bonjur.member.list.MemberListAction {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String userId = null;
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole role = null;
        
        public AssignRole(@org.jetbrains.annotations.NotNull()
        java.lang.String userId, @org.jetbrains.annotations.NotNull()
        com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole role) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getUserId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole getRole() {
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
        public final com.bonjur.member.list.MemberListAction.AssignRole copy(@org.jetbrains.annotations.NotNull()
        java.lang.String userId, @org.jetbrains.annotations.NotNull()
        com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole role) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/member/list/MemberListAction$BackTapped;", "Lcom/bonjur/member/list/MemberListAction;", "()V", "member_debug"})
    public static final class BackTapped extends com.bonjur.member.list.MemberListAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.member.list.MemberListAction.BackTapped INSTANCE = null;
        
        private BackTapped() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/member/list/MemberListAction$LoadMore;", "Lcom/bonjur/member/list/MemberListAction;", "()V", "member_debug"})
    public static final class LoadMore extends com.bonjur.member.list.MemberListAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.member.list.MemberListAction.LoadMore INSTANCE = null;
        
        private LoadMore() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/member/list/MemberListAction$MemberTapped;", "Lcom/bonjur/member/list/MemberListAction;", "member", "Lcom/bonjur/member/model/MemberCellModel;", "(Lcom/bonjur/member/model/MemberCellModel;)V", "getMember", "()Lcom/bonjur/member/model/MemberCellModel;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "member_debug"})
    public static final class MemberTapped extends com.bonjur.member.list.MemberListAction {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.member.model.MemberCellModel member = null;
        
        public MemberTapped(@org.jetbrains.annotations.NotNull()
        com.bonjur.member.model.MemberCellModel member) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.member.model.MemberCellModel getMember() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.member.model.MemberCellModel component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.member.list.MemberListAction.MemberTapped copy(@org.jetbrains.annotations.NotNull()
        com.bonjur.member.model.MemberCellModel member) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/member/list/MemberListAction$OnAppear;", "Lcom/bonjur/member/list/MemberListAction;", "()V", "member_debug"})
    public static final class OnAppear extends com.bonjur.member.list.MemberListAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.member.list.MemberListAction.OnAppear INSTANCE = null;
        
        private OnAppear() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/bonjur/member/list/MemberListAction$SearchTextChanged;", "Lcom/bonjur/member/list/MemberListAction;", "text", "", "(Ljava/lang/String;)V", "getText", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "member_debug"})
    public static final class SearchTextChanged extends com.bonjur.member.list.MemberListAction {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String text = null;
        
        public SearchTextChanged(@org.jetbrains.annotations.NotNull()
        java.lang.String text) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getText() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.member.list.MemberListAction.SearchTextChanged copy(@org.jetbrains.annotations.NotNull()
        java.lang.String text) {
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
}