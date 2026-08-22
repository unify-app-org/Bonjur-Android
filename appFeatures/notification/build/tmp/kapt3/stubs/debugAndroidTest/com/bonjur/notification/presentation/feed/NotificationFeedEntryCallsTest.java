package com.bonjur.notification.presentation.feed;

/**
 * Opening the feed used to fire four extra calls just to fill the "Needs your
 * action" banner — three join-request probes plus the admin-only
 * `clubs/pending`, which 403s for everyone who isn't a president.
 * Entering the screen must now hit the feed and nothing else.
 */
@org.junit.runner.RunWith(value = androidx.test.ext.junit.runners.AndroidJUnit4.class)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007\u00a8\u0006\u0006"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NotificationFeedEntryCallsTest;", "", "()V", "openingTheFeedOnlyLoadsTheFeed", "", "RecordingUseCase", "notification_debugAndroidTest"})
public final class NotificationFeedEntryCallsTest {
    
    public NotificationFeedEntryCallsTest() {
        super();
    }
    
    @org.junit.Test()
    public final void openingTheFeedOnlyLoadsTheFeed() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u000e\u0010\u0010\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u000e\u0010\u0011\u001a\u00020\u0012H\u0096@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0005H\u0096@\u00a2\u0006\u0002\u0010\u0015R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0016"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NotificationFeedEntryCallsTest$RecordingUseCase;", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "()V", "calls", "Ljava/util/concurrent/CopyOnWriteArrayList;", "", "getCalls", "()Ljava/util/concurrent/CopyOnWriteArrayList;", "fetchFeedPage", "Lcom/bonjur/notification/domain/models/NotificationFeedPage;", "page", "", "size", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchUnreadCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchVerificationCount", "markAllRead", "", "markRead", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notification_debugAndroidTest"})
    static final class RecordingUseCase implements com.bonjur.notification.domain.useCase.NotificationUseCase {
        @org.jetbrains.annotations.NotNull()
        private final java.util.concurrent.CopyOnWriteArrayList<java.lang.String> calls = null;
        
        public RecordingUseCase() {
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