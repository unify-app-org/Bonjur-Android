package com.bonjur.notification.presentation.verification.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.notification.domain.models.VerificationItem
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase

data class VerificationViewState(
    val items: List<VerificationItem> = emptyList(),
    val phase: RequestsPhase = RequestsPhase.IDLE,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = false,
    val processingIds: Set<String> = emptySet()
) : FeatureState

sealed class VerificationSideEffect : SideEffect {
    data class Error(val message: String?) : VerificationSideEffect()
    data class ConfirmReject(val item: VerificationItem) : VerificationSideEffect()
}

sealed class VerificationAction : FeatureAction {
    object OnAppear : VerificationAction()
    object Refresh : VerificationAction()
    object LoadMore : VerificationAction()
    object Retry : VerificationAction()
    data class Verify(val item: VerificationItem) : VerificationAction()
    data class Reject(val item: VerificationItem) : VerificationAction()
    data class PerformReject(val item: VerificationItem) : VerificationAction()
    data class CellTapped(val item: VerificationItem) : VerificationAction()
}
