package com.bonjur.member.list;

/**
 * Activity-agnostic see-all members screen. Pages + role changes go through the
 * closures supplied in [MemberListInputData], so this single screen serves clubs /
 * events / hangouts / communities (mirrors iOS shared MembersListViewModel).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u0000 \"2\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u0001:\u0001\"B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\n\u0010\u0019\u001a\u0004\u0018\u00010\u0016H\u0002J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u0003H\u0016J\u0010\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0016H\u0002J\u0016\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\tH\u0002J\b\u0010!\u001a\u00020\u0014H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/bonjur/member/list/MemberListViewModel;", "Lcom/bonjur/appfoundation/FeatureViewModel;", "Lcom/bonjur/member/list/MemberListViewState;", "Lcom/bonjur/member/list/MemberListAction;", "Lcom/bonjur/member/list/MemberListSideEffect;", "()V", "inputData", "Lcom/bonjur/member/list/MemberListInputData;", "isFetching", "", "loadedMembers", "", "Lcom/bonjur/member/model/MemberCellModel;", "navigator", "Lcom/bonjur/navigation/Navigator;", "nextPage", "", "searchJob", "Lkotlinx/coroutines/Job;", "assignRole", "", "userId", "", "role", "Lcom/bonjur/designSystem/commonModel/AppUIEntities$UserActivityRole;", "currentKeyword", "handle", "action", "handleSearchTextChanged", "text", "init", "loadNextPage", "initial", "refreshMembers", "Companion", "member_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MemberListViewModel extends com.bonjur.appfoundation.FeatureViewModel<com.bonjur.member.list.MemberListViewState, com.bonjur.member.list.MemberListAction, com.bonjur.member.list.MemberListSideEffect> {
    private static final int PAGE_SIZE = 20;
    private static final long SEARCH_DEBOUNCE_MS = 300L;
    private com.bonjur.member.list.MemberListInputData inputData;
    private com.bonjur.navigation.Navigator navigator;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.bonjur.member.model.MemberCellModel> loadedMembers = null;
    private int nextPage = 0;
    private boolean isFetching = false;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job searchJob;
    @org.jetbrains.annotations.NotNull()
    public static final com.bonjur.member.list.MemberListViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public MemberListViewModel() {
        super(null);
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    com.bonjur.member.list.MemberListInputData inputData, @org.jetbrains.annotations.NotNull()
    com.bonjur.navigation.Navigator navigator) {
    }
    
    @java.lang.Override()
    public void handle(@org.jetbrains.annotations.NotNull()
    com.bonjur.member.list.MemberListAction action) {
    }
    
    /**
     * Debounced server-side member search, mirroring the clubs/events list (300ms).
     */
    private final void handleSearchTextChanged(java.lang.String text) {
    }
    
    /**
     * Trimmed search term, null when blank so the query param is omitted.
     */
    private final java.lang.String currentKeyword() {
        return null;
    }
    
    private final void loadNextPage(boolean initial) {
    }
    
    private final void assignRole(java.lang.String userId, com.bonjur.designSystem.commonModel.AppUIEntities.UserActivityRole role) {
    }
    
    private final void refreshMembers() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/bonjur/member/list/MemberListViewModel$Companion;", "", "()V", "PAGE_SIZE", "", "SEARCH_DEBOUNCE_MS", "", "member_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}