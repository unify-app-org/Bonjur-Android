package com.bonjur.notification.presentation.feed;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\n\u001a\u00020\u000bH\u0002J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0003H\u0016J\u000e\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\tJ\b\u0010\u000f\u001a\u00020\u000bH\u0002J\b\u0010\u0010\u001a\u00020\u000bH\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/notification/presentation/feed/NotificationFeedViewModel;", "Lcom/bonjur/appfoundation/FeatureViewModel;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedViewState;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedSideEffect;", "useCase", "Lcom/bonjur/notification/domain/useCase/NotificationUseCase;", "(Lcom/bonjur/notification/domain/useCase/NotificationUseCase;)V", "navigator", "Lcom/bonjur/navigation/Navigator;", "fetchData", "", "handle", "action", "init", "markAllRead", "refreshRequestCount", "notification_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class NotificationFeedViewModel extends com.bonjur.appfoundation.FeatureViewModel<com.bonjur.notification.presentation.feed.models.NotificationFeedViewState, com.bonjur.notification.presentation.feed.models.NotificationFeedAction, com.bonjur.notification.presentation.feed.models.NotificationFeedSideEffect> {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.useCase.NotificationUseCase useCase = null;
    private com.bonjur.navigation.Navigator navigator;
    
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
    
    /**
     * Overrides the banner's `requests` count with the live total. Verifications
     * stay on the mock inbox value until that flows through the feed.
     */
    private final void refreshRequestCount() {
    }
    
    private final void markAllRead() {
    }
}