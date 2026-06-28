package com.bonjur.notification.presentation.needsAction

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.ProfileDetailNavArgs
import com.bonjur.navigation.SharedRoutes
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import com.bonjur.notification.domain.models.ActionRequestItem
import com.bonjur.notification.domain.models.ActionRequestKind
import com.bonjur.notification.domain.models.RequestPageResult
import com.bonjur.notification.domain.useCase.NeedsActionUseCase
import com.bonjur.notification.navigation.NotificationScreens
import com.bonjur.notification.presentation.needsAction.models.ActionTab
import com.bonjur.notification.presentation.needsAction.models.NeedsActionAction
import com.bonjur.notification.presentation.needsAction.models.NeedsActionSideEffect
import com.bonjur.notification.presentation.needsAction.models.NeedsActionViewState
import com.bonjur.notification.presentation.needsAction.models.RequestSourceState
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeedsActionViewModel @Inject constructor(
    private val useCase: NeedsActionUseCase
) : FeatureViewModel<NeedsActionViewState, NeedsActionAction, NeedsActionSideEffect>(
    NeedsActionViewState()
) {
    private lateinit var navigator: Navigator
    private val pageSize = 20
    private var clubPage = 0
    private var hangoutPage = 0

    fun init(navigator: Navigator) {
        if (::navigator.isInitialized) return
        this.navigator = navigator
    }

    override fun handle(action: NeedsActionAction) {
        when (action) {
            NeedsActionAction.OnAppear -> {
                if (state.clubs.phase == RequestsPhase.IDLE) loadInitial(ActionTab.CLUBS)
                if (state.hangouts.phase == RequestsPhase.IDLE) loadInitial(ActionTab.HANGOUTS)
                refreshVerificationBanner()
            }
            is NeedsActionAction.SelectTab -> updateState(state.copy(selectedTab = action.tab))
            is NeedsActionAction.Refresh -> loadInitial(action.tab)
            is NeedsActionAction.Retry -> loadInitial(action.tab)
            is NeedsActionAction.LoadMore -> loadMore(action.tab)
            is NeedsActionAction.Accept -> process(action.item, accept = true)
            is NeedsActionAction.Reject -> postEffect(NeedsActionSideEffect.ConfirmReject(action.item))
            is NeedsActionAction.PerformReject -> process(action.item, accept = false)
            is NeedsActionAction.RequestTapped -> openProfile(action.item)
            NeedsActionAction.VerificationTapped -> navigateTo(NotificationScreens.Verification.route)
        }
    }

    // MARK: - Verification banner

    private fun refreshVerificationBanner() {
        viewModelScope.launch {
            try {
                val count = useCase.fetchVerificationCount()
                updateState(state.copy(isAdmin = true, verificationCount = count))
            } catch (e: ApiException) {
                updateState(state.copy(isAdmin = false, verificationCount = 0))
            }
        }
    }

    // MARK: - Source plumbing

    private fun source(tab: ActionTab): RequestSourceState? = when (tab) {
        ActionTab.CLUBS -> state.clubs
        ActionTab.HANGOUTS -> state.hangouts
        ActionTab.EVENTS -> null
    }

    private fun setSource(tab: ActionTab, source: RequestSourceState) {
        when (tab) {
            ActionTab.CLUBS -> updateState(state.copy(clubs = source))
            ActionTab.HANGOUTS -> updateState(state.copy(hangouts = source))
            ActionTab.EVENTS -> Unit
        }
    }

    private suspend fun fetch(tab: ActionTab, page: Int): RequestPageResult? = when (tab) {
        ActionTab.CLUBS -> useCase.fetchClubRequests(page, pageSize)
        ActionTab.HANGOUTS -> useCase.fetchHangoutRequests(page, pageSize)
        ActionTab.EVENTS -> null
    }

    private fun currentPage(tab: ActionTab): Int = when (tab) {
        ActionTab.CLUBS -> clubPage
        ActionTab.HANGOUTS -> hangoutPage
        ActionTab.EVENTS -> 0
    }

    private fun setPage(tab: ActionTab, page: Int) {
        when (tab) {
            ActionTab.CLUBS -> clubPage = page
            ActionTab.HANGOUTS -> hangoutPage = page
            ActionTab.EVENTS -> Unit
        }
    }

    // MARK: - Loading

    private fun loadInitial(tab: ActionTab) {
        val current = source(tab) ?: return
        setPage(tab, 0)
        if (current.items.isEmpty()) setSource(tab, current.copy(phase = RequestsPhase.LOADING))
        viewModelScope.launch {
            try {
                val result = fetch(tab, 0) ?: return@launch
                val src = source(tab) ?: return@launch
                setSource(
                    tab,
                    src.copy(
                        items = sortedByNewest(result.items),
                        canLoadMore = result.hasMore,
                        phase = RequestsPhase.LOADED
                    )
                )
            } catch (e: ApiException) {
                val src = source(tab) ?: return@launch
                if (src.items.isEmpty()) setSource(tab, src.copy(phase = RequestsPhase.FAILED))
                postEffect(NeedsActionSideEffect.Error(e.message))
            }
        }
    }

    private fun loadMore(tab: ActionTab) {
        val current = source(tab) ?: return
        if (!current.canLoadMore || current.isLoadingMore) return
        setSource(tab, current.copy(isLoadingMore = true))
        val next = currentPage(tab) + 1
        viewModelScope.launch {
            try {
                val result = fetch(tab, next) ?: return@launch
                setPage(tab, next)
                val src = source(tab) ?: return@launch
                setSource(
                    tab,
                    src.copy(
                        items = sortedByNewest(src.items + result.items),
                        canLoadMore = result.hasMore,
                        isLoadingMore = false
                    )
                )
            } catch (e: ApiException) {
                val src = source(tab) ?: return@launch
                setSource(tab, src.copy(isLoadingMore = false))
                postEffect(NeedsActionSideEffect.Error(e.message))
            }
        }
    }

    // MARK: - Accept / Reject

    private fun process(item: ActionRequestItem, accept: Boolean) {
        if (state.processingIds.contains(item.id)) return
        val userId = item.requesterId
        if (userId == null) {
            postEffect(NeedsActionSideEffect.Error("Missing user"))
            return
        }
        updateState(state.copy(processingIds = state.processingIds + item.id))
        viewModelScope.launch {
            try {
                when (val kind = item.kind) {
                    is ActionRequestKind.Club -> useCase.setClubStatus(kind.id, userId, accept)
                    is ActionRequestKind.Hangout -> useCase.setHangoutStatus(kind.id, userId, accept)
                }
                finishProcessing(item, removed = true, error = null)
            } catch (e: ApiException) {
                finishProcessing(item, removed = false, error = e.message)
            }
        }
    }

    private fun finishProcessing(item: ActionRequestItem, removed: Boolean, error: String?) {
        var next = state.copy(processingIds = state.processingIds - item.id)
        if (removed) {
            next = when (item.kind) {
                is ActionRequestKind.Club ->
                    next.copy(clubs = next.clubs.copy(items = next.clubs.items.filterNot { it.id == item.id }))
                is ActionRequestKind.Hangout ->
                    next.copy(hangouts = next.hangouts.copy(items = next.hangouts.items.filterNot { it.id == item.id }))
            }
        }
        updateState(next)
        if (error != null) postEffect(NeedsActionSideEffect.Error(error))
    }

    // MARK: - Navigation

    private fun openProfile(item: ActionRequestItem) {
        val userId = item.requesterId ?: return
        viewModelScope.launch {
            navigator.navigateTo(SharedRoutes.PROFILE_DETAIL, ProfileDetailNavArgs(userId))
        }
    }

    private fun navigateTo(route: String) {
        viewModelScope.launch { navigator.navigateTo(route) }
    }

    // MARK: - Sort

    /** Newest first; rows missing createdAt sink to the bottom but keep order. */
    private fun sortedByNewest(items: List<ActionRequestItem>): List<ActionRequestItem> =
        items.sortedWith(compareByDescending { it.createdAtMillis ?: Long.MIN_VALUE })
}
