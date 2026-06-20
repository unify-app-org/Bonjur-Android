package com.bonjur.member.model;

/**
 * Members grouped into role sections (President → Vise president → Event creators → Members).
 * Mirrors iOS `CommunitiesMemberModuleModel.GroupedMembersData`.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087\b\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0019\u0010\t\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/bonjur/member/model/GroupedMembersData;", "", "sections", "", "Lcom/bonjur/member/model/MemberListSectionModel;", "(Ljava/util/List;)V", "getSections", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Companion", "member_debug"})
public final class GroupedMembersData {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.bonjur.member.model.MemberListSectionModel> sections = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.member.model.GroupedMembersData.Companion Companion = null;
    
    public GroupedMembersData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.member.model.MemberListSectionModel> sections) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.member.model.MemberListSectionModel> getSections() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.member.model.MemberListSectionModel> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.member.model.GroupedMembersData copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.member.model.MemberListSectionModel> sections) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J*\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tJ\f\u0010\f\u001a\u00020\u000b*\u00020\nH\u0002J\f\u0010\r\u001a\u00020\u000e*\u00020\nH\u0002\u00a8\u0006\u000f"}, d2 = {"Lcom/bonjur/member/model/GroupedMembersData$Companion;", "", "()V", "from", "Lcom/bonjur/member/model/GroupedMembersData;", "users", "", "Lcom/bonjur/member/model/MemberCellModel;", "titleOverrides", "", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "", "sectionTitle", "sortPriority", "", "member_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.member.model.GroupedMembersData from(@org.jetbrains.annotations.NotNull()
        java.util.List<com.bonjur.member.model.MemberCellModel> users, @org.jetbrains.annotations.NotNull()
        java.util.Map<com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, java.lang.String> titleOverrides) {
            return null;
        }
        
        private final int sortPriority(com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole $this$sortPriority) {
            return 0;
        }
        
        private final java.lang.String sectionTitle(com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole $this$sectionTitle) {
            return null;
        }
    }
}