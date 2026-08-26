package com.bonjur.notification.presentation.feed.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\fH\u00c6\u0003JG\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020\u00072\b\u0010 \u001a\u0004\u0018\u00010!H\u00d6\u0003J\t\u0010\"\u001a\u00020\fH\u00d6\u0001J\t\u0010#\u001a\u00020$H\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u000fR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006%"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedViewState;", "Lcom/bonjur/appfoundation/FeatureState;", "inbox", "Lcom/bonjur/notification/domain/models/NotificationInbox;", "phase", "Lcom/bonjur/notification/presentation/needsAction/models/RequestsPhase;", "isLoadingMore", "", "canLoadMore", "previewItem", "Lcom/bonjur/notification/domain/models/NotificationFeedItem;", "pendingActionCount", "", "(Lcom/bonjur/notification/domain/models/NotificationInbox;Lcom/bonjur/notification/presentation/needsAction/models/RequestsPhase;ZZLcom/bonjur/notification/domain/models/NotificationFeedItem;I)V", "getCanLoadMore", "()Z", "getInbox", "()Lcom/bonjur/notification/domain/models/NotificationInbox;", "getPendingActionCount", "()I", "getPhase", "()Lcom/bonjur/notification/presentation/needsAction/models/RequestsPhase;", "getPreviewItem", "()Lcom/bonjur/notification/domain/models/NotificationFeedItem;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "", "hashCode", "toString", "", "notification_debug"})
public final class NotificationFeedViewState implements com.bonjur.appfoundation.FeatureState {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.models.NotificationInbox inbox = null;
    
    /**
     * First-load lifecycle (spinner / empty / error). Reuses the NeedsAction phase enum.
     */
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.presentation.needsAction.models.RequestsPhase phase = null;
    private final boolean isLoadingMore = false;
    private final boolean canLoadMore = false;
    
    /**
     * Row currently shown in the modal preview sheet; null = sheet hidden.
     */
    @org.jetbrains.annotations.Nullable()
    private final com.bonjur.notification.domain.models.NotificationFeedItem previewItem = null;
    
    /**
     * Pending join requests behind the "Needs your action" banner. Drives its red dot.
     */
    private final int pendingActionCount = 0;
    
    public NotificationFeedViewState(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.models.NotificationInbox inbox, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestsPhase phase, boolean isLoadingMore, boolean canLoadMore, @org.jetbrains.annotations.Nullable()
    com.bonjur.notification.domain.models.NotificationFeedItem previewItem, int pendingActionCount) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.NotificationInbox getInbox() {
        return null;
    }
    
    /**
     * First-load lifecycle (spinner / empty / error). Reuses the NeedsAction phase enum.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.RequestsPhase getPhase() {
        return null;
    }
    
    public final boolean isLoadingMore() {
        return false;
    }
    
    public final boolean getCanLoadMore() {
        return false;
    }
    
    /**
     * Row currently shown in the modal preview sheet; null = sheet hidden.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.bonjur.notification.domain.models.NotificationFeedItem getPreviewItem() {
        return null;
    }
    
    /**
     * Pending join requests behind the "Needs your action" banner. Drives its red dot.
     */
    public final int getPendingActionCount() {
        return 0;
    }
    
    public NotificationFeedViewState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.domain.models.NotificationInbox component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.RequestsPhase component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.bonjur.notification.domain.models.NotificationFeedItem component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.feed.models.NotificationFeedViewState copy(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.models.NotificationInbox inbox, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestsPhase phase, boolean isLoadingMore, boolean canLoadMore, @org.jetbrains.annotations.Nullable()
    com.bonjur.notification.domain.models.NotificationFeedItem previewItem, int pendingActionCount) {
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