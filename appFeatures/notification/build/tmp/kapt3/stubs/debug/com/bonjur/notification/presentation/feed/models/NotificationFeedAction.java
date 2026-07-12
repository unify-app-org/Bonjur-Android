package com.bonjur.notification.presentation.feed.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\b\u0003\u0004\u0005\u0006\u0007\b\t\nB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\b\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "Lcom/bonjur/appfoundation/FeatureAction;", "()V", "ActionBannerTapped", "DismissPreview", "FetchData", "ItemTapped", "LoadMore", "MarkAllRead", "PreviewContinue", "Retry", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$ActionBannerTapped;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$DismissPreview;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$FetchData;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$ItemTapped;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$LoadMore;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$MarkAllRead;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$PreviewContinue;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$Retry;", "notification_debug"})
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
    
    /**
     * Preview "Close" / dismiss.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$DismissPreview;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class DismissPreview extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.DismissPreview INSTANCE = null;
        
        private DismissPreview() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$FetchData;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class FetchData extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.FetchData INSTANCE = null;
        
        private FetchData() {
        }
    }
    
    /**
     * Row tap → open the modal preview sheet.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$ItemTapped;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "id", "", "(Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class ItemTapped extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String id = null;
        
        public ItemTapped(@org.jetbrains.annotations.NotNull()
        java.lang.String id) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.ItemTapped copy(@org.jetbrains.annotations.NotNull()
        java.lang.String id) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$LoadMore;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class LoadMore extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.LoadMore INSTANCE = null;
        
        private LoadMore() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$MarkAllRead;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class MarkAllRead extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.MarkAllRead INSTANCE = null;
        
        private MarkAllRead() {
        }
    }
    
    /**
     * Preview "Continue" → deep-link to the target entity, then close.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$PreviewContinue;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class PreviewContinue extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.PreviewContinue INSTANCE = null;
        
        private PreviewContinue() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction$Retry;", "Lcom/bonjur/notification/presentation/feed/models/NotificationFeedAction;", "()V", "notification_debug"})
    public static final class Retry extends com.bonjur.notification.presentation.feed.models.NotificationFeedAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.feed.models.NotificationFeedAction.Retry INSTANCE = null;
        
        private Retry() {
        }
    }
}