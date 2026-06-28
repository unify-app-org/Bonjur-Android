package com.bonjur.notification.presentation.needsAction;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u0007\u0018\u00002\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J \u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\tH\u0082@\u00a2\u0006\u0002\u0010\u0014J\"\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u0003H\u0016J\u000e\u0010\u001f\u001a\u00020\u00162\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010 \u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010!\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\"\u001a\u00020\u00162\u0006\u0010#\u001a\u00020\u001cH\u0002J\u0010\u0010$\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0018\u0010%\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010&\u001a\u00020\u001aH\u0002J\b\u0010'\u001a\u00020\u0016H\u0002J\u0018\u0010(\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\tH\u0002J\u0018\u0010)\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010*\u001a\u00020+H\u0002J\u001c\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00180-2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00180-H\u0002J\u0012\u0010*\u001a\u0004\u0018\u00010+2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/bonjur/notification/presentation/needsAction/NeedsActionViewModel;", "Lcom/bonjur/appfoundation/FeatureViewModel;", "Lcom/bonjur/notification/presentation/needsAction/models/NeedsActionViewState;", "Lcom/bonjur/notification/presentation/needsAction/models/NeedsActionAction;", "Lcom/bonjur/notification/presentation/needsAction/models/NeedsActionSideEffect;", "useCase", "Lcom/bonjur/notification/domain/useCase/NeedsActionUseCase;", "(Lcom/bonjur/notification/domain/useCase/NeedsActionUseCase;)V", "clubPage", "", "hangoutPage", "navigator", "Lcom/bonjur/navigation/Navigator;", "pageSize", "currentPage", "tab", "Lcom/bonjur/notification/presentation/needsAction/models/ActionTab;", "fetch", "Lcom/bonjur/notification/domain/models/RequestPageResult;", "page", "(Lcom/bonjur/notification/presentation/needsAction/models/ActionTab;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "finishProcessing", "", "item", "Lcom/bonjur/notification/domain/models/ActionRequestItem;", "removed", "", "error", "", "handle", "action", "init", "loadInitial", "loadMore", "navigateTo", "route", "openProfile", "process", "accept", "refreshVerificationBanner", "setPage", "setSource", "source", "Lcom/bonjur/notification/presentation/needsAction/models/RequestSourceState;", "sortedByNewest", "", "items", "notification_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NeedsActionViewModel extends com.bonjur.appfoundation.FeatureViewModel<com.bonjur.notification.presentation.needsAction.models.NeedsActionViewState, com.bonjur.notification.presentation.needsAction.models.NeedsActionAction, com.bonjur.notification.presentation.needsAction.models.NeedsActionSideEffect> {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.useCase.NeedsActionUseCase useCase = null;
    private com.bonjur.navigation.Navigator navigator;
    private final int pageSize = 20;
    private int clubPage = 0;
    private int hangoutPage = 0;
    
    @javax.inject.Inject()
    public NeedsActionViewModel(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.useCase.NeedsActionUseCase useCase) {
        super(null);
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    com.bonjur.navigation.Navigator navigator) {
    }
    
    @java.lang.Override()
    public void handle(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.NeedsActionAction action) {
    }
    
    private final void refreshVerificationBanner() {
    }
    
    private final com.bonjur.notification.presentation.needsAction.models.RequestSourceState source(com.bonjur.notification.presentation.needsAction.models.ActionTab tab) {
        return null;
    }
    
    private final void setSource(com.bonjur.notification.presentation.needsAction.models.ActionTab tab, com.bonjur.notification.presentation.needsAction.models.RequestSourceState source) {
    }
    
    private final java.lang.Object fetch(com.bonjur.notification.presentation.needsAction.models.ActionTab tab, int page, kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion) {
        return null;
    }
    
    private final int currentPage(com.bonjur.notification.presentation.needsAction.models.ActionTab tab) {
        return 0;
    }
    
    private final void setPage(com.bonjur.notification.presentation.needsAction.models.ActionTab tab, int page) {
    }
    
    private final void loadInitial(com.bonjur.notification.presentation.needsAction.models.ActionTab tab) {
    }
    
    private final void loadMore(com.bonjur.notification.presentation.needsAction.models.ActionTab tab) {
    }
    
    private final void process(com.bonjur.notification.domain.models.ActionRequestItem item, boolean accept) {
    }
    
    private final void finishProcessing(com.bonjur.notification.domain.models.ActionRequestItem item, boolean removed, java.lang.String error) {
    }
    
    private final void openProfile(com.bonjur.notification.domain.models.ActionRequestItem item) {
    }
    
    private final void navigateTo(java.lang.String route) {
    }
    
    /**
     * Newest first; rows missing createdAt sink to the bottom but keep order.
     */
    private final java.util.List<com.bonjur.notification.domain.models.ActionRequestItem> sortedByNewest(java.util.List<com.bonjur.notification.domain.models.ActionRequestItem> items) {
        return null;
    }
}