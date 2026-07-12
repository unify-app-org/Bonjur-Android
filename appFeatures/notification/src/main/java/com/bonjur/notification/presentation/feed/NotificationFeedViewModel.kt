package com.bonjur.notification.presentation.feed

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.ClubDetailsNavArgs
import com.bonjur.navigation.EventDetailsNavArgs
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.ProfileDetailNavArgs
import com.bonjur.navigation.SharedRoutes
import com.bonjur.network.model.ApiException
import com.bonjur.notification.domain.models.NeedsActionSummary
import com.bonjur.notification.domain.models.NotificationFeedItem
import com.bonjur.notification.domain.models.NotificationFeedMapper
import com.bonjur.notification.domain.models.NotificationTargetType
import com.bonjur.notification.domain.useCase.NotificationUseCase
import com.bonjur.notification.navigation.NotificationScreens
import com.bonjur.notification.presentation.feed.models.NotificationFeedAction
import com.bonjur.notification.presentation.feed.models.NotificationFeedSideEffect
import com.bonjur.notification.presentation.feed.models.NotificationFeedViewState
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationFeedViewModel @Inject constructor(
    private val useCase: NotificationUseCase
) : FeatureViewModel<NotificationFeedViewState, NotificationFeedAction, NotificationFeedSideEffect>(
    NotificationFeedViewState()
) {
    private lateinit var navigator: Navigator
    private val pageSize = 20
    private var page = 0
    /** Flat, newest-first accumulation across pages; regrouped into buckets on every apply. */
    private var feedItems: List<NotificationFeedItem> = emptyList()

    fun init(navigator: Navigator) {
        if (::navigator.isInitialized) return
        this.navigator = navigator
    }

    override fun handle(action: NotificationFeedAction) {
        when (action) {
            NotificationFeedAction.FetchData, NotificationFeedAction.Retry -> fetchData()
            NotificationFeedAction.LoadMore -> loadMore()
            NotificationFeedAction.MarkAllRead -> markAllRead()
            NotificationFeedAction.ActionBannerTapped ->
                viewModelScope.launch { navigator.navigateTo(NotificationScreens.NeedsAction.route) }
            is NotificationFeedAction.ItemTapped -> itemTapped(action.id)
            NotificationFeedAction.PreviewContinue -> previewContinue()
            NotificationFeedAction.DismissPreview -> updateState(state.copy(previewItem = null))
        }
    }

    // MARK: - Feed loading

    private fun fetchData() {
        if (state.inbox.sections.isEmpty()) updateState(state.copy(phase = RequestsPhase.LOADING))
        viewModelScope.launch {
            try {
                val result = useCase.fetchFeedPage(page = 0, size = pageSize)
                page = 0
                feedItems = result.items
                updateState(
                    state.copy(
                        inbox = state.inbox.copy(sections = NotificationFeedMapper.sections(feedItems)),
                        canLoadMore = result.hasMore,
                        phase = RequestsPhase.LOADED
                    )
                )
            } catch (e: ApiException) {
                if (state.inbox.sections.isEmpty()) updateState(state.copy(phase = RequestsPhase.FAILED))
                postEffect(NotificationFeedSideEffect.Error(e.message))
            }
        }
        refreshActionBanner()
    }

    private fun loadMore() {
        if (!state.canLoadMore || state.isLoadingMore) return
        updateState(state.copy(isLoadingMore = true))
        val next = page + 1
        viewModelScope.launch {
            try {
                val result = useCase.fetchFeedPage(page = next, size = pageSize)
                page = next
                feedItems = feedItems + result.items
                updateState(
                    state.copy(
                        inbox = state.inbox.copy(sections = NotificationFeedMapper.sections(feedItems)),
                        canLoadMore = result.hasMore,
                        isLoadingMore = false
                    )
                )
            } catch (e: ApiException) {
                updateState(state.copy(isLoadingMore = false))
                postEffect(NotificationFeedSideEffect.Error(e.message))
            }
        }
    }

    // MARK: - Needs your action (banner)

    /** Composes the banner client-side: join-request totals (club + hangout +
     * event) plus the admin-only verification probe (403 → 0). Failures silent. */
    private fun refreshActionBanner() {
        viewModelScope.launch {
            val requests = try {
                useCase.fetchRequestCounts()
            } catch (e: ApiException) {
                0
            }
            val verifications = try {
                useCase.fetchVerificationCount()
            } catch (e: ApiException) {
                0
            }
            updateState(
                state.copy(inbox = state.inbox.copy(action = NeedsActionSummary(requests, verifications)))
            )
        }
    }

    // MARK: - Read state

    /** Explicit toolbar action — flips rows locally too. */
    private fun markAllRead() {
        val cleared = state.inbox.sections.map { section ->
            section.copy(items = section.items.map { it.copy(isRead = true) })
        }
        feedItems = feedItems.map { it.copy(isRead = true) }
        updateState(state.copy(inbox = state.inbox.copy(sections = cleared)))
        viewModelScope.launch {
            try {
                useCase.markAllRead()
            } catch (e: ApiException) {
                postEffect(NotificationFeedSideEffect.Error(e.message))
            }
        }
    }

    // MARK: - Preview + navigation

    /** Row tap opens the modal preview sheet (mirrors iOS). Deep-linking is
     * deferred to the sheet's "Continue" CTA. */
    private fun itemTapped(id: String) {
        val item = state.inbox.sections.flatMap { it.items }.firstOrNull { it.id == id } ?: return
        updateState(state.copy(previewItem = item))
    }

    /** Preview "Continue": dismiss the sheet, then deep-link into the target. */
    private fun previewContinue() {
        val item = state.previewItem ?: return
        updateState(state.copy(previewItem = null))
        openTarget(item)
    }

    private fun openTarget(item: NotificationFeedItem) {
        val targetId = item.targetId ?: return
        viewModelScope.launch {
            when (item.targetType) {
                NotificationTargetType.CLUB ->
                    targetId.toIntOrNull()?.let {
                        navigator.navigateTo(SharedRoutes.CLUB_DETAILS, ClubDetailsNavArgs(it))
                    }
                NotificationTargetType.EVENT ->
                    navigator.navigateTo(SharedRoutes.EVENT_DETAILS, EventDetailsNavArgs(targetId))
                NotificationTargetType.USER ->
                    navigator.navigateTo(SharedRoutes.PROFILE_DETAIL, ProfileDetailNavArgs(targetId))
                NotificationTargetType.NONE -> Unit
            }
        }
    }
}
