package com.bonjur.member.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u0012\u0010\u0006\u001a\u00020\u00012\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0003\u001a\b\u0010\t\u001a\u00020\u0001H\u0003\u001a8\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\u0091\u0001\u0010\u000e\u001a\u00020\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u00152\u0016\b\u0002\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u00132\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u0018\u001a\u00020\u00192\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001b2\u0010\b\u0002\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0005H\u0007\u00a2\u0006\u0002\u0010\u001e\u001a\u001a\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020\b2\b\u0010!\u001a\u0004\u0018\u00010\bH\u0007\u001a\u001e\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u001b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a+\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u00102\b\u0010&\u001a\u0004\u0018\u00010\u001bH\u0002\u00a2\u0006\u0002\u0010\'\u001a\u0018\u0010(\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\u0017\u001a\u0004\u0018\u00010\b\u00a8\u0006)"}, d2 = {"MemberAccessoryView", "", "accessory", "Lcom/bonjur/member/components/MemberCellAccessory;", "onAccessoryTap", "Lkotlin/Function0;", "MemberAvatar", "avatarUrl", "", "MemberAvatarFallback", "MemberCell", "member", "Lcom/bonjur/member/model/MemberCellModel;", "onTap", "MemberListView", "sections", "", "Lcom/bonjur/member/model/MemberListSectionModel;", "onRowTap", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "onOptionsTap", "currentUserId", "showCountText", "", "previewLimit", "", "totalCount", "onSeeAll", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function1;Ljava/lang/String;ZLjava/lang/Integer;Ljava/lang/Integer;Lkotlin/jvm/functions/Function0;)V", "MemberSectionHeader", "title", "memberCountText", "SeeAllButton", "count", "onClick", "capSections", "limit", "(Ljava/util/List;Ljava/lang/Integer;)Ljava/util/List;", "memberOptionsAccessory", "member_debug"})
public final class MemberListKt {
    
    /**
     * Options accessory that hides the 3-dot on the current user's own row — you can't
     * act on yourself, so no menu. Mirrors iOS `MemberCellViewData.options(from:currentUserId:)`.
     */
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.member.components.MemberCellAccessory memberOptionsAccessory(@org.jetbrains.annotations.NotNull()
    com.bonjur.member.model.MemberCellModel member, @org.jetbrains.annotations.Nullable()
    java.lang.String currentUserId) {
        return null;
    }
    
    /**
     * A single bordered member card. Mirrors iOS `MemberCellView`.
     */
    @androidx.compose.runtime.Composable()
    public static final void MemberCell(@org.jetbrains.annotations.NotNull()
    com.bonjur.member.model.MemberCellModel member, @org.jetbrains.annotations.NotNull()
    com.bonjur.member.components.MemberCellAccessory accessory, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onTap, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAccessoryTap) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MemberAvatar(java.lang.String avatarUrl) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MemberAvatarFallback() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MemberAccessoryView(com.bonjur.member.components.MemberCellAccessory accessory, kotlin.jvm.functions.Function0<kotlin.Unit> onAccessoryTap) {
    }
    
    /**
     * Section header: role title + member count. Mirrors iOS `MemberSectionHeaderView`.
     */
    @androidx.compose.runtime.Composable()
    public static final void MemberSectionHeader(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String memberCountText) {
    }
    
    /**
     * Sectioned member list. Renders each role section with a header then bordered rows.
     * When [previewLimit] is set, caps rows across sections (in section order) and shows a
     * "See all" affordance once the total exceeds the limit. Mirrors iOS `MemberListView`.
     */
    @androidx.compose.runtime.Composable()
    public static final void MemberListView(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.member.model.MemberListSectionModel> sections, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.bonjur.member.model.MemberCellModel, kotlin.Unit> onRowTap, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super com.bonjur.member.model.MemberCellModel, kotlin.Unit> onOptionsTap, @org.jetbrains.annotations.Nullable()
    java.lang.String currentUserId, boolean showCountText, @org.jetbrains.annotations.Nullable()
    java.lang.Integer previewLimit, @org.jetbrains.annotations.Nullable()
    java.lang.Integer totalCount, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSeeAll) {
    }
    
    /**
     * Caps total rows to [limit] across sections, preserving section order and grouping.
     */
    private static final java.util.List<com.bonjur.member.model.MemberListSectionModel> capSections(java.util.List<com.bonjur.member.model.MemberListSectionModel> sections, java.lang.Integer limit) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SeeAllButton(int count, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}