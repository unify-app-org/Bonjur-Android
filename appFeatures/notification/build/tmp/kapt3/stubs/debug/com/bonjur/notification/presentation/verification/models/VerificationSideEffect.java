package com.bonjur.notification.presentation.verification.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0002\u0003\u0004B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0002\u0005\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect;", "Lcom/bonjur/appfoundation/SideEffect;", "()V", "ConfirmReject", "Error", "Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect$ConfirmReject;", "Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect$Error;", "notification_debug"})
public abstract class VerificationSideEffect implements com.bonjur.appfoundation.SideEffect {
    
    private VerificationSideEffect() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect$ConfirmReject;", "Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect;", "item", "Lcom/bonjur/notification/domain/models/VerificationItem;", "(Lcom/bonjur/notification/domain/models/VerificationItem;)V", "getItem", "()Lcom/bonjur/notification/domain/models/VerificationItem;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "notification_debug"})
    public static final class ConfirmReject extends com.bonjur.notification.presentation.verification.models.VerificationSideEffect {
        @org.jetbrains.annotations.NotNull()
        private final com.bonjur.notification.domain.models.VerificationItem item = null;
        
        public ConfirmReject(@org.jetbrains.annotations.NotNull()
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
        public final com.bonjur.notification.presentation.verification.models.VerificationSideEffect.ConfirmReject copy(@org.jetbrains.annotations.NotNull()
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u000b\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0015\u0010\b\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect$Error;", "Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect;", "message", "", "(Ljava/lang/String;)V", "getMessage", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "notification_debug"})
    public static final class Error extends com.bonjur.notification.presentation.verification.models.VerificationSideEffect {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String message = null;
        
        public Error(@org.jetbrains.annotations.Nullable()
        java.lang.String message) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getMessage() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bonjur.notification.presentation.verification.models.VerificationSideEffect.Error copy(@org.jetbrains.annotations.Nullable()
        java.lang.String message) {
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