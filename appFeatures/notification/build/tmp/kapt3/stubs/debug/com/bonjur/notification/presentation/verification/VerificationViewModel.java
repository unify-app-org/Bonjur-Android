package com.bonjur.notification.presentation.verification;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00040\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J\u000e\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\b\u001a\u00020\tJ\b\u0010\u0011\u001a\u00020\u000eH\u0002J\b\u0010\u0012\u001a\u00020\u000eH\u0002J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0018\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018H\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/bonjur/notification/presentation/verification/VerificationViewModel;", "Lcom/bonjur/appfoundation/FeatureViewModel;", "Lcom/bonjur/notification/presentation/verification/models/VerificationViewState;", "Lcom/bonjur/notification/presentation/verification/models/VerificationAction;", "Lcom/bonjur/notification/presentation/verification/models/VerificationSideEffect;", "useCase", "Lcom/bonjur/notification/domain/useCase/VerificationUseCase;", "(Lcom/bonjur/notification/domain/useCase/VerificationUseCase;)V", "navigator", "Lcom/bonjur/navigation/Navigator;", "page", "", "pageSize", "handle", "", "action", "init", "loadInitial", "loadMore", "openClub", "item", "Lcom/bonjur/notification/domain/models/VerificationItem;", "process", "accept", "", "notification_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class VerificationViewModel extends com.bonjur.appfoundation.FeatureViewModel<com.bonjur.notification.presentation.verification.models.VerificationViewState, com.bonjur.notification.presentation.verification.models.VerificationAction, com.bonjur.notification.presentation.verification.models.VerificationSideEffect> {
    @org.jetbrains.annotations.NotNull()
    private final com.bonjur.notification.domain.useCase.VerificationUseCase useCase = null;
    private com.bonjur.navigation.Navigator navigator;
    private final int pageSize = 20;
    private int page = 0;
    
    @javax.inject.Inject()
    public VerificationViewModel(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.domain.useCase.VerificationUseCase useCase) {
        super(null);
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    com.bonjur.navigation.Navigator navigator) {
    }
    
    @java.lang.Override()
    public void handle(@org.jetbrains.annotations.NotNull()
    com.bonjur.notification.presentation.verification.models.VerificationAction action) {
    }
    
    private final void loadInitial() {
    }
    
    private final void loadMore() {
    }
    
    private final void process(com.bonjur.notification.domain.models.VerificationItem item, boolean accept) {
    }
    
    private final void openClub(com.bonjur.notification.domain.models.VerificationItem item) {
    }
}