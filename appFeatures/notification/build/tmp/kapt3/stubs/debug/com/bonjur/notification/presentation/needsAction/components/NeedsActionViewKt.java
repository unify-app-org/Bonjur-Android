package com.bonjur.notification.presentation.needsAction.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0001\u001a\u0018\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003H\u0001\u001a\u0016\u0010\u0007\u001a\u00020\u00012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0001\u001a\b\u0010\n\u001a\u00020\u0001H\u0001\u001a\"\u0010\u000b\u001a\u00020\u00012\u0018\u0010\f\u001a\u0014\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\rH\u0007\u001aB\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a2\u0010\u0019\u001a\u00020\u00012\u0018\u0010\f\u001a\u0014\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\r2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0003\u001a:\u0010\u001e\u001a\u00020\u00012\u0018\u0010\f\u001a\u0014\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\r2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u0003H\u0003\u001a\u001e\u0010 \u001a\u00020\u00012\u0006\u0010!\u001a\u00020\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u00a8\u0006$"}, d2 = {"Avatar", "", "url", "", "ComingSoon", "title", "subtitle", "ErrorState", "onRetry", "Lkotlin/Function0;", "LoadingState", "NeedsActionView", "store", "Lcom/bonjur/appfoundation/FeatureStore;", "Lcom/bonjur/notification/presentation/needsAction/models/NeedsActionViewState;", "Lcom/bonjur/notification/presentation/needsAction/models/NeedsActionAction;", "Lcom/bonjur/notification/presentation/needsAction/models/NeedsActionSideEffect;", "RequestRow", "item", "Lcom/bonjur/notification/domain/models/ActionRequestItem;", "isProcessing", "", "onTap", "onAccept", "onReject", "RequestsList", "tab", "Lcom/bonjur/notification/presentation/needsAction/models/ActionTab;", "source", "Lcom/bonjur/notification/presentation/needsAction/models/RequestSourceState;", "RequestsTab", "emptySubtitle", "VerificationBanner", "count", "", "onClick", "notification_debug"})
public final class NeedsActionViewKt {
    
    @androidx.compose.runtime.Composable()
    public static final void NeedsActionView(@org.jetbrains.annotations.NotNull()
    com.bonjur.appfoundation.FeatureStore<com.bonjur.notification.presentation.needsAction.models.NeedsActionViewState, com.bonjur.notification.presentation.needsAction.models.NeedsActionAction, com.bonjur.notification.presentation.needsAction.models.NeedsActionSideEffect> store) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void VerificationBanner(int count, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RequestsTab(com.bonjur.appfoundation.FeatureStore<com.bonjur.notification.presentation.needsAction.models.NeedsActionViewState, com.bonjur.notification.presentation.needsAction.models.NeedsActionAction, com.bonjur.notification.presentation.needsAction.models.NeedsActionSideEffect> store, com.bonjur.notification.presentation.needsAction.models.ActionTab tab, com.bonjur.notification.presentation.needsAction.models.RequestSourceState source, java.lang.String emptySubtitle) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RequestsList(com.bonjur.appfoundation.FeatureStore<com.bonjur.notification.presentation.needsAction.models.NeedsActionViewState, com.bonjur.notification.presentation.needsAction.models.NeedsActionAction, com.bonjur.notification.presentation.needsAction.models.NeedsActionSideEffect> store, com.bonjur.notification.presentation.needsAction.models.ActionTab tab, com.bonjur.notification.presentation.needsAction.models.RequestSourceState source) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RequestRow(com.bonjur.notification.domain.models.ActionRequestItem item, boolean isProcessing, kotlin.jvm.functions.Function0<kotlin.Unit> onTap, kotlin.jvm.functions.Function0<kotlin.Unit> onAccept, kotlin.jvm.functions.Function0<kotlin.Unit> onReject) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void Avatar(@org.jetbrains.annotations.Nullable()
    java.lang.String url) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void LoadingState() {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ErrorState(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRetry) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ComingSoon(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String subtitle) {
    }
}