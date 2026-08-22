package com.bonjur.notification.presentation.feed;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0003H\u0016J\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0011H\u0002J\b\u0010\u0019\u001a\u00020\u0011H\u0002J\u0010\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\nH\u0002J\u0010\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\nH\u0002J\b\u0010\u001d\u001a\u00020\u0011H\u0002J\u0018\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u001f\u001a\u00020 H\u0002R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NotificationFeedViewModel;", "Lcom/bonjur/appfoundation/FeatureViewModel;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedViewState;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedSideEffect;", "useCase", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "(Lcom/bonjur/notification/domain/useCase/NotificationUseCase;)V", "feedItems", "", "Lcom/bonjur/notification/domain/models/NotificationFeedItem;", "navigator", "Lcom/bonjur/navigation/Navigator;", "page", "", "pageSize", "fetchData", "", "handle", "action", "init", "itemTapped", "id", "", "loadMore", "markAllRead", "markRead", "item", "openTarget", "previewContinue", "setReadFlag", "isRead", "", "notification_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NotificationFeedViewModel extends com.bonjur.appfoundation.FeatureViewModel<com.bonjur.notification.presentation.feed.models.NotificationFeedViewState, com.bonjur.notification.presentation.feed.models.NotificationFeedAction, com.bonjur.notification.presentation.feed.models.NotificationFeedSideEffect> {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.useCase.NotificationUseCase useCase = null;
    private com.bonjur.navigation.Navigator navigator;
    private final int pageSize = 20;
    private int page = 0;
    
    /**
     * Flat, newest-first accumulation across pages; regrouped into buckets on every apply.
     */
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.bonjur.notification.domain.models.NotificationFeedItem> feedItems;
    
    @javax.inject.Inject()
    public NotificationFeedViewModel(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.useCase.NotificationUseCase useCase) {
        super(null);
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    com.bonjur.navigation.Navigator navigator) {
    }
    
    @java.lang.Override()
    public void handle(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.feed.models.NotificationFeedAction action) {
    }
    
    private final void fetchData() {
    }
    
    private final void loadMore() {
    }
    
    /**
     * Explicit toolbar action — flips rows locally too.
     */
    private final void markAllRead() {
    }
    
    /**
     * Row tap opens the modal preview sheet (mirrors iOS). Deep-linking is
     * deferred to the sheet's "Continue" CTA.
     */
    private final void itemTapped(java.lang.String id) {
    }
    
    /**
     * Optimistically mark a single notification read, then persist it. A failure
     * rolls the row back; the badge re-syncs on the next fetch. Mirrors iOS.
     */
    private final void markRead(com.bonjur.notification.domain.models.NotificationFeedItem item) {
    }
    
    private final void setReadFlag(java.lang.String id, boolean isRead) {
    }
    
    /**
     * Preview "Continue": dismiss the sheet, then route by the row's type —
     * requests land on the NeedsAction screen, outcomes deep-link into the
     * target, informational rows just close (mirrors iOS `tapDestination`).
     */
    private final void previewContinue() {
    }
    
    private final void openTarget(com.bonjur.notification.domain.models.NotificationFeedItem item) {
    }
}