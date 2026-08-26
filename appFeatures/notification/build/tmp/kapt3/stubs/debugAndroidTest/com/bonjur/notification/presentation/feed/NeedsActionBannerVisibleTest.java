package com.bonjur.notification.presentation.feed;

/**
 * "Needs your action" is the first thing in the feed and must be on screen the
 * moment the screen opens — reported as sitting above the fold, needing a scroll.
 */
@org.junit.runner.RunWith(value = androidx.test.ext.junit.runners.AndroidJUnit4.class)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0002\u000e\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0007J\b\u0010\t\u001a\u00020\bH\u0007J\b\u0010\n\u001a\u00020\bH\u0007J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\rH\u0002R\u0013\u0010\u0003\u001a\u00020\u00048G\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NeedsActionBannerVisibleTest;", "", "()V", "compose", "Landroidx/compose/ui/test/junit4/ComposeContentTestRule;", "getCompose", "()Landroidx/compose/ui/test/junit4/ComposeContentTestRule;", "bannerIsOnScreenWithAFullFeed", "", "bannerIsOnScreenWithAnEmptyFeed", "bannerStaysOnScreenAfterScrollingTheFeed", "showFeed", "rows", "", "FakeNeedsActionUseCase", "FakeUseCase", "notification_debugAndroidTest"})
public final class NeedsActionBannerVisibleTest {
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.test.junit4.ComposeContentTestRule compose = null;
    
    public NeedsActionBannerVisibleTest() {
        super();
    }
    
    @org.junit.Rule()
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.test.junit4.ComposeContentTestRule getCompose() {
        return null;
    }
    
    private final void showFeed(int rows) {
    }
    
    @org.junit.Test()
    public final void bannerIsOnScreenWithAFullFeed() {
    }
    
    @org.junit.Test()
    public final void bannerIsOnScreenWithAnEmptyFeed() {
    }
    
    /**
     * The card must survive scrolling: as a list item it disappeared once the feed
     * moved, and LazyColumn restores that offset on re-entry — which is how it
     * ended up off-screen the moment the screen opened.
     */
    @org.junit.Test()
    public final void bannerStaysOnScreenAfterScrollingTheFeed() {
    }
    
    /**
     * The banner's count comes from here; this screen only cares that the banner renders.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0096@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\n\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0096@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0096@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\f\u001a\u00020\u0007H\u0096@\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\u0007H\u0096@\u00a2\u0006\u0002\u0010\rJ&\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u0016J&\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u0019J&\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0002\u0010\u0019R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NeedsActionBannerVisibleTest$FakeNeedsActionUseCase;", "Lcom/bonjur/notification/domain/useCase/NeedsActionUseCase;", "()V", "empty", "Lcom/bonjur/notification/domain/models/RequestPageResult;", "fetchClubRequests", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchEventRequests", "fetchHangoutRequests", "fetchPendingActionCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchVerificationCount", "setClubStatus", "", "clubId", "userId", "", "accept", "", "(ILjava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEventStatus", "eventId", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHangoutStatus", "hangoutId", "notification_debugAndroidTest"})
    static final class FakeNeedsActionUseCase implements com.bonjur.notification.domain.useCase.NeedsActionUseCase {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.domain.models.RequestPageResult empty = null;
        
        public FakeNeedsActionUseCase() {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchClubRequests(int page, int size, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchHangoutRequests(int page, int size, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchEventRequests(int page, int size, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.RequestPageResult> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object setClubStatus(int clubId, @org.jetbrains.annotations.NotNull()
        java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object setHangoutStatus(@org.jetbrains.annotations.NotNull()
        java.lang.String hangoutId, @org.jetbrains.annotations.NotNull()
        java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object setEventStatus(@org.jetbrains.annotations.NotNull()
        java.lang.String eventId, @org.jetbrains.annotations.NotNull()
        java.lang.String userId, boolean accept, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchVerificationCount(@org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchPendingActionCount(@org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0003H\u0096@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u0003H\u0096@\u00a2\u0006\u0002\u0010\u000bJ\u000e\u0010\f\u001a\u00020\u0003H\u0096@\u00a2\u0006\u0002\u0010\u000bJ\u000e\u0010\r\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NeedsActionBannerVisibleTest$FakeUseCase;", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "rows", "", "(I)V", "fetchFeedPage", "Lcom/bonjur/notification/domain/models/NotificationFeedPage;", "page", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchUnreadCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchVerificationCount", "markAllRead", "", "markRead", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debugAndroidTest"})
    static final class FakeUseCase implements com.bonjur.notification.domain.useCase.NotificationUseCase {
        private final int rows = 0;
        
        public FakeUseCase(int rows) {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchFeedPage(int page, int size, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super com.bonjur.notification.domain.models.NotificationFeedPage> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object markAllRead(@org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object markRead(@org.jetbrains.annotations.NotNull()
        java.lang.String id, @org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchVerificationCount(@org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.Nullable()
        public java.lang.Object fetchUnreadCount(@org.jetbrains.annotations.NotNull()
        kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
            return null;
        }
    }
}