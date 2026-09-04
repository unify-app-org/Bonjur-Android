package com.bonjur.member.model;

/**
 * One page of members plus whether more pages remain. Mirrors iOS `MembersPage`.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\'\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\u000f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0006H\u00c6\u0003J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000fJ4\u0010\u0014\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bH\u00c6\u0001\u00a2\u0006\u0002\u0010\u0015J\u0013\u0010\u0016\u001a\u00020\u00062\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\bH\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001b"}, d2 = {"Lcom/bonjur/member/model/MembersPage;", "", "members", "", "Lcom/bonjur/member/model/MemberCellModel;", "hasMore", "", "totalCount", "", "(Ljava/util/List;ZLjava/lang/Integer;)V", "getHasMore", "()Z", "getMembers", "()Ljava/util/List;", "getTotalCount", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "copy", "(Ljava/util/List;ZLjava/lang/Integer;)Lcom/bonjur/member/model/MembersPage;", "equals", "other", "hashCode", "toString", "", "member_debug"})
public final class MembersPage {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.bonjur.member.model.MemberCellModel> members = null;
    private final boolean hasMore = false;
    
    /**
     * Total rows the query matches (`totalElements`). Comes from the same response
     * as the page, so it also reflects an active keyword filter — unlike a total
     * handed in by the caller from a detail payload.
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer totalCount = null;
    
    public MembersPage(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.member.model.MemberCellModel> members, boolean hasMore, @org.jetbrains.annotations.Nullable()
    java.lang.Integer totalCount) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.member.model.MemberCellModel> getMembers() {
        return null;
    }
    
    public final boolean getHasMore() {
        return false;
    }
    
    /**
     * Total rows the query matches (`totalElements`). Comes from the same response
     * as the page, so it also reflects an active keyword filter — unlike a total
     * handed in by the caller from a detail payload.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getTotalCount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.member.model.MemberCellModel> component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.member.model.MembersPage copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.member.model.MemberCellModel> members, boolean hasMore, @org.jetbrains.annotations.Nullable()
    java.lang.Integer totalCount) {
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