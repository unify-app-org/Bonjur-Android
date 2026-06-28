package com.bonjur.notification.presentation.feed.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "Lcom/bonjur/appfoundation/FeatureAction;", "()V", "ActionBannerTapped", "FetchData", "MarkAllRead", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$ActionBannerTapped;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$FetchData;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$MarkAllRead;", "notification_debug"})
public abstract class NotificationFeedAction implements com.bonjur.appfoundation.FeatureAction {
    
    private NotificationFeedAction() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$ActionBannerTapped;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class ActionBannerTapped extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.ActionBannerTapped INSTANCE = null;
        
        private ActionBannerTapped() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$FetchData;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class FetchData extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.FetchData INSTANCE = null;
        
        private FetchData() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$MarkAllRead;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class MarkAllRead extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.MarkAllRead INSTANCE = null;
        
        private MarkAllRead() {
        }
    }
}