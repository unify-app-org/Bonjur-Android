package com.bonjur.notification.presentation.needsAction.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0087\b\u0018\u00002\u00020\u0001BG\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0005H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\rH\u00c6\u0003JK\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u00c6\u0001J\u0013\u0010 \u001a\u00020\u000b2\b\u0010!\u001a\u0004\u0018\u00010\"H\u00d6\u0003J\t\u0010#\u001a\u00020\rH\u00d6\u0001J\t\u0010$\u001a\u00020\tH\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0012R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006%"}, d2 = {"Lcom/bonjur/notification/presentation/needsAction/models/NeedsActionViewState;", "Lcom/bonjur/appfoundation/FeatureState;", "selectedTab", "Lcom/bonjur/notification/presentation/needsAction/models/ActionTab;", "clubs", "Lcom/bonjur/notification/presentation/needsAction/models/RequestSourceState;", "hangouts", "processingIds", "", "", "isAdmin", "", "verificationCount", "", "(Lcom/bonjur/notification/presentation/needsAction/models/ActionTab;Lcom/bonjur/notification/presentation/needsAction/models/RequestSourceState;Lcom/bonjur/notification/presentation/needsAction/models/RequestSourceState;Ljava/util/Set;ZI)V", "getClubs", "()Lcom/bonjur/notification/presentation/needsAction/models/RequestSourceState;", "getHangouts", "()Z", "getProcessingIds", "()Ljava/util/Set;", "getSelectedTab", "()Lcom/bonjur/notification/presentation/needsAction/models/ActionTab;", "getVerificationCount", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "", "hashCode", "toString", "notification_debug"})
public final class NeedsActionViewState implements com.bonjur.appfoundation.FeatureState {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.presentation.needsAction.models.ActionTab selectedTab = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.presentation.needsAction.models.RequestSourceState clubs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.presentation.needsAction.models.RequestSourceState hangouts = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> processingIds = null;
    private final boolean isAdmin = false;
    private final int verificationCount = 0;
    
    public NeedsActionViewState(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.ActionTab selectedTab, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestSourceState clubs, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestSourceState hangouts, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> processingIds, boolean isAdmin, int verificationCount) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.ActionTab getSelectedTab() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.RequestSourceState getClubs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.RequestSourceState getHangouts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getProcessingIds() {
        return null;
    }
    
    public final boolean isAdmin() {
        return false;
    }
    
    public final int getVerificationCount() {
        return 0;
    }
    
    public NeedsActionViewState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.ActionTab component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.RequestSourceState component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.RequestSourceState component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.bonjur.notification.presentation.needsAction.models.NeedsActionViewState copy(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.ActionTab selectedTab, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestSourceState clubs, @org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.needsAction.models.RequestSourceState hangouts, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> processingIds, boolean isAdmin, int verificationCount) {
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