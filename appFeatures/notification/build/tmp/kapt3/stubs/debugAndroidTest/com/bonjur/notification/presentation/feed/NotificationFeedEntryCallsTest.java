package com.bonjur.notification.presentation.feed;

/**
 * Opening the feed once fired four extra calls just to fill the "Needs your action" banner.
 * Three were harmless count probes; the fourth, `clubs/pending`, is **admin-only and 403s for
 * everyone who isn't a president** — on every single open. All four were dropped 2026-08-22.
 *
 * 2026-08-24 the banner got its red dot + count back (requested), so the three count probes are
 * deliberately back — bundled behind one `fetchPendingActionCount()`. What must stay gone is the
 * admin probe, which is what this test now pins: entry loads the feed and the pending count, and
 * never touches `fetchVerificationCount` on either use case.
 */
@org.junit.runner.RunWith(value = androidx.test.ext.junit.runners.AndroidJUnit4.class)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0002\u0006\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007\u00a8\u0006\b"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NotificationFeedEntryCallsTest;", "", "()V", "openingTheFeedLoadsTheFeedAndTheBannerCountOnly", "", "openingTheFeedNeverProbesTheAdminVerificationQueue", "RecordingNeedsActionUseCase", "RecordingUseCase", "notification_debugAndroidTest"})
public final class NotificationFeedEntryCallsTest {
    
    public NotificationFeedEntryCallsTest() {
        super();
    }
    
    @org.junit.Test()
    public final void openingTheFeedLoadsTheFeedAndTheBannerCountOnly() {
    }
    
    /**
     * The 403-on-every-open regression, pinned on its own.
     */
    @org.junit.Test()
    public final void openingTheFeedNeverProbesTheAdminVerificationQueue() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u001e\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u000f\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u0010\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u0011\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\fH\u0096@\u00a2\u0006\u0002\u0010\u0012J&\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001aJ&\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001dJ&\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0019H\u0096@\u00a2\u0006\u0002\u0010\u001dR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/bonjur/notification/presentation/feed/NotificationFeedEntryCallsTest$RecordingNeedsActionUseCase;", "Lcom/bonjur/notification/domain/useCase/NeedsActionUseCase;", "calls", "Ljava/util/concurrent/CopyOnWriteArrayList;", "", "(Ljava/util/concurrent/CopyOnWriteArrayList;)V", "getCalls", "()Ljava/util/concurrent/CopyOnWriteArrayList;", "empty", "Lcom/bonjur/notification/domain/models/RequestPageResult;", "fetchClubRequests", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchEventRequests", "fetchHangoutRequests", "fetchPendingActionCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchVerificationCount", "setClubStatus", "", "clubId", "userId", "accept", "", "(ILjava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEventStatus", "eventId", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setHangoutStatus", "hangoutId", "notification_debugAndroidTest"})
    static final class RecordingNeedsActionUseCase implements com.bonjur.notification.domain.useCase.NeedsActionUseCase {
        @org.jetbrains.annotations.NotNull()
        private final java.util.concurrent.CopyOnWriteArrayList<java.lang.String> calls = null;
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.domain.models.RequestPageResult empty = null;
        
        public RecordingNeedsActionUseCase(@org.jetbrains.annotations.NotNull()
        java.util.concurrent.CopyOnWriteArrayList<java.lang.String> calls) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.concurrent.CopyOnWriteArrayList<java.lang.String> getCalls() {
            return null;
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u000e\u0010\u0010\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u000e\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0004H\u0096@\u00a2\u0006\u0002\u0010\u0015R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0016"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NotificationFeedEntryCallsTest$RecordingUseCase;", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "calls", "Ljava/util/concurrent/CopyOnWriteArrayList;", "", "(Ljava/util/concurrent/CopyOnWriteArrayList;)V", "getCalls", "()Ljava/util/concurrent/CopyOnWriteArrayList;", "fetchFeedPage", "Lcom/bonjur/notification/domain/models/NotificationFeedPage;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchUnreadCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchVerificationCount", "markAllRead", "", "markRead", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debugAndroidTest"})
    static final class RecordingUseCase implements com.bonjur.notification.domain.useCase.NotificationUseCase {
        @org.jetbrains.annotations.NotNull()
        private final java.util.concurrent.CopyOnWriteArrayList<java.lang.String> calls = null;
        
        public RecordingUseCase(@org.jetbrains.annotations.NotNull()
        java.util.concurrent.CopyOnWriteArrayList<java.lang.String> calls) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.concurrent.CopyOnWriteArrayList<java.lang.String> getCalls() {
            return null;
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