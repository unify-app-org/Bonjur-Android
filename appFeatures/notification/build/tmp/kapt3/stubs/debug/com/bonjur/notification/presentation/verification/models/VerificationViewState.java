package com.bonjur.notification.presentation.verification.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BC\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0002\u0010\rJ\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\bH\u00c6\u0003J\t\u0010\u0019\u001a\u00020\bH\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0003JG\u0010\u001b\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\b2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u00d6\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001J\t\u0010!\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u000fR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\""}, d2 = {"Lcom/bonjur/notification/presentation/verification/models/VerificationViewState;", "Lcom/bonjur/appfoundation/FeatureState;", "items", "", "Lcom/bonjur/notification/domain/models/VerificationItem;", "phase", "Lcom/bonjur/notification/presentation/needsAction/models/RequestsPhase;", "isLoadingMore", "", "canLoadMore", "processingIds", "", "", "(Ljava/util/List;Lcom/bonjur/notification/presentation/needsAction/models/RequestsPhase;ZZLjava/util/Set;)V", "getCanLoadMore", "()Z", "getItems", "()Ljava/util/List;", "getPhase", "()Lcom/bonjur/notification/presentation/needsAction/models/RequestsPhase;", "getProcessingIds", "()Ljava/util/Set;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "", "hashCode", "", "toString", "notification_debug"})
public final class VerificationViewState implements com.bonjur.appfoundation.FeatureState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.bonjur.notification.domain.models.VerificationItem> items = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.presentation.needsAction.models.RequestsPhase phase = null;
    private final boolean isLoadingMore = false;
    private final boolean canLoadMore = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> processingIds = null;
    
    public VerificationViewState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.notification.domain.models.VerificationItem> items, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestsPhase phase, boolean isLoadingMore, boolean canLoadMore, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> processingIds) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.notification.domain.models.VerificationItem> getItems() {
        return null;
    }
    
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getProcessingIds() {
        return null;
    }
    
    public VerificationViewState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.bonjur.notification.domain.models.VerificationItem> component1() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.verification.models.VerificationViewState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.bonjur.notification.domain.models.VerificationItem> items, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestsPhase phase, boolean isLoadingMore, boolean canLoadMore, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> processingIds) {
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