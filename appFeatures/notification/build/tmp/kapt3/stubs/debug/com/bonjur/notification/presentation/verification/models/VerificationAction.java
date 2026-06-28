package com.bonjur.notification.presentation.verification.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\b\u0003\u0004\u0005\u0006\u0007\b\t\nB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\b\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u00a8\u0006\u0013"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "Lcom/bonjur/appfoundation/FeatureAction;", "()V", "CellTapped", "LoadMore", "OnAppear", "PerformReject", "Refresh", "Reject", "Retry", "Verify", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$CellTapped;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$LoadMore;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$OnAppear;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$PerformReject;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Refresh;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Reject;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Retry;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Verify;", "notification_debug"})
public abstract class VerificationAction implements com.bonjur.appfoundation.FeatureAction {
    
    private VerificationAction() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$CellTapped;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "item", "Lcom/bonjur/notification/domain/models/VerificationItem;", "(Lcom/bonjur/notification/domain/models/VerificationItem;)V", "getItem", "()Lcom/bonjur/notification/domain/models/VerificationItem;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "notification_debug"})
    public static final class CellTapped extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.domain.models.VerificationItem item = null;
        
        public CellTapped(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem getItem() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.presentation.verification.models.VerificationAction.CellTapped copy(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$LoadMore;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "()V", "notification_debug"})
    public static final class LoadMore extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.verification.models.VerificationAction.LoadMore INSTANCE = null;
        
        private LoadMore() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$OnAppear;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "()V", "notification_debug"})
    public static final class OnAppear extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.verification.models.VerificationAction.OnAppear INSTANCE = null;
        
        private OnAppear() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$PerformReject;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "item", "Lcom/bonjur/notification/domain/models/VerificationItem;", "(Lcom/bonjur/notification/domain/models/VerificationItem;)V", "getItem", "()Lcom/bonjur/notification/domain/models/VerificationItem;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "notification_debug"})
    public static final class PerformReject extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.domain.models.VerificationItem item = null;
        
        public PerformReject(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem getItem() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.presentation.verification.models.VerificationAction.PerformReject copy(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Refresh;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "()V", "notification_debug"})
    public static final class Refresh extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.verification.models.VerificationAction.Refresh INSTANCE = null;
        
        private Refresh() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Reject;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "item", "Lcom/bonjur/notification/domain/models/VerificationItem;", "(Lcom/bonjur/notification/domain/models/VerificationItem;)V", "getItem", "()Lcom/bonjur/notification/domain/models/VerificationItem;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "notification_debug"})
    public static final class Reject extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.domain.models.VerificationItem item = null;
        
        public Reject(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem getItem() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.presentation.verification.models.VerificationAction.Reject copy(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Retry;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "()V", "notification_debug"})
    public static final class Retry extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.bonjur.notification.presentation.verification.models.VerificationAction.Retry INSTANCE = null;
        
        private Retry() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationAction$Verify;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "item", "Lcom/bonjur/notification/domain/models/VerificationItem;", "(Lcom/bonjur/notification/domain/models/VerificationItem;)V", "getItem", "()Lcom/bonjur/notification/domain/models/VerificationItem;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "notification_debug"})
    public static final class Verify extends com.bonjur.notification.presentation.verification.models.VerificationAction {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.domain.models.VerificationItem item = null;
        
        public Verify(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem getItem() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.domain.models.VerificationItem component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.presentation.verification.models.VerificationAction.Verify copy(@org.jetbrains.annotations.NotNull()
        com.bonjur.notification.domain.models.VerificationItem item) {
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
}