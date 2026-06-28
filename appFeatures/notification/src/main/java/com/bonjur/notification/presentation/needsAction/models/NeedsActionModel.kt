package com.bonjur.notification.presentation.needsAction.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.notification.domain.models.ActionRequestItem

// MARK: - Tabs

/** Top segmented control. Declaration order = display order (Events, Hangouts, Clubs). */
enum class ActionTab(
    override val title: String,
    override val id: String
) : SegmentedPickerOption {
    EVENTS("Events", "events"),
    HANGOUTS("Hangouts", "hangouts"),
    CLUBS("Clubs", "clubs")
}

// MARK: - Load phase

enum class RequestsPhase { IDLE, LOADING, LOADED, FAILED }

// MARK: - Per-source state

data class RequestSourceState(
    val items: List<ActionRequestItem> = emptyList(),
    val phase: RequestsPhase = RequestsPhase.IDLE,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = false
)

// MARK: - View State

data class NeedsActionViewState(
    val selectedTab: ActionTab = ActionTab.CLUBS,
    val clubs: RequestSourceState = RequestSourceState(),
    val hangouts: RequestSourceState = RequestSourceState(),
    val processingIds: Set<String> = emptySet(),
    val isAdmin: Boolean = false,
    val verificationCount: Int = 0
) : FeatureState

// MARK: - Side effects

sealed class NeedsActionSideEffect : SideEffect {
    data class Error(val message: String?) : NeedsActionSideEffect()
    data class ConfirmReject(val item: ActionRequestItem) : NeedsActionSideEffect()
}

// MARK: - Actions

sealed class NeedsActionAction : FeatureAction {
    object OnAppear : NeedsActionAction()
    data class SelectTab(val tab: ActionTab) : NeedsActionAction()
    data class Refresh(val tab: ActionTab) : NeedsActionAction()
    data class LoadMore(val tab: ActionTab) : NeedsActionAction()
    data class Retry(val tab: ActionTab) : NeedsActionAction()
    data class Accept(val item: ActionRequestItem) : NeedsActionAction()
    data class Reject(val item: ActionRequestItem) : NeedsActionAction()
    data class PerformReject(val item: ActionRequestItem) : NeedsActionAction()
    data class RequestTapped(val item: ActionRequestItem) : NeedsActionAction()
    object VerificationTapped : NeedsActionAction()
}
