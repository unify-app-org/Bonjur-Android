package com.bonjur.member.list;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u009e\u0001\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u001128\b\u0002\u0010\u0012\u001a2\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0016\u0012\u0013\u0012\u00110\b\u00a2\u0006\f\b\u0014\u0012\b\b\u0015\u0012\u0004\b\b(\u0017\u0012\u0004\u0012\u00020\u00010\u00132\b\b\u0002\u0010\u0018\u001a\u00020\u0006H\u0007\u00a8\u0006\u0019"}, d2 = {"MembersPreview", "", "sections", "", "Lcom/bonjur/member/model/MemberListSectionModel;", "totalCount", "", "viewerRole", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "currentUserId", "", "activityType", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$ActivityType;", "onMemberTap", "Lkotlin/Function1;", "Lcom/bonjur/member/model/MemberCellModel;", "onSeeAll", "Lkotlin/Function0;", "onAssignRole", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "userId", "role", "previewLimit", "member_debug"})
public final class MembersPreviewKt {
    
    /**
     * In-detail members section shared by every activity (clubs / events / hangouts /
     * communities): capped preview list + "See all" + per-row 3-dot options sheet.
     * Single source of truth — each detail's MembersTab is just a thin wrapper.
     * Role-assign is gated by [MemberOptionsPolicy] (CLUBS/COMMUNITY only); pass
     * [onAssignRole] there, leave it default elsewhere.
     */
    @androidx.compose.runtime.Composable()
    public static final void MembersPreview(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.member.model.MemberListSectionModel> sections, int totalCount, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole viewerRole, @org.jetbrains.annotations.Nullable()
    java.lang.String currentUserId, @org.jetbrains.annotations.NotNull()
    com.bonjur.designSystem.commonModel.AppUIEntities.ActivityType activityType, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.bonjur.member.model.MemberCellModel, kotlin.Unit> onMemberTap, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSeeAll, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole, kotlin.Unit> onAssignRole, int previewLimit) {
    }
}