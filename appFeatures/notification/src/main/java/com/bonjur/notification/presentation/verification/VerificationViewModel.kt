package com.bonjur.notification.presentation.verification

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.ClubDetailsNavArgs
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.SharedRoutes
import com.bonjur.network.model.ApiException
import com.bonjur.notification.domain.models.VerificationItem
import com.bonjur.notification.domain.useCase.VerificationUseCase
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase
import com.bonjur.notification.presentation.verification.models.VerificationAction
import com.bonjur.notification.presentation.verification.models.VerificationSideEffect
import com.bonjur.notification.presentation.verification.models.VerificationViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val useCase: VerificationUseCase
) : FeatureViewModel<VerificationViewState, VerificationAction, VerificationSideEffect>(
    VerificationViewState()
) {
    private lateinit var navigator: Navigator
    private val pageSize = 20
    private var page = 0

    fun init(navigator: Navigator) {
        if (::navigator.isInitialized) return
        this.navigator = navigator
    }

    override fun handle(action: VerificationAction) {
        when (action) {
            VerificationAction.OnAppear -> if (state.phase == RequestsPhase.IDLE) loadInitial()
            VerificationAction.Refresh -> loadInitial()
            VerificationAction.Retry -> loadInitial()
            VerificationAction.LoadMore -> loadMore()
            is VerificationAction.Verify -> process(action.item, accept = true)
            is VerificationAction.Reject -> postEffect(VerificationSideEffect.ConfirmReject(action.item))
            is VerificationAction.PerformReject -> process(action.item, accept = false)
            is VerificationAction.CellTapped -> openClub(action.item)
        }
    }

    private fun loadInitial() {
        page = 0
        if (state.items.isEmpty()) updateState(state.copy(phase = RequestsPhase.LOADING))
        viewModelScope.launch {
            try {
                val result = useCase.fetchPending(0, pageSize)
                updateState(
                    state.copy(
                        items = result.items,
                        canLoadMore = result.hasMore,
                        phase = RequestsPhase.LOADED
                    )
                )
            } catch (e: ApiException) {
                if (state.items.isEmpty()) updateState(state.copy(phase = RequestsPhase.FAILED))
                postEffect(VerificationSideEffect.Error(e.message))
            }
        }
    }

    private fun loadMore() {
        if (!state.canLoadMore || state.isLoadingMore) return
        updateState(state.copy(isLoadingMore = true))
        val next = page + 1
        viewModelScope.launch {
            try {
                val result = useCase.fetchPending(next, pageSize)
                page = next
                updateState(
                    state.copy(
                        items = state.items + result.items,
                        canLoadMore = result.hasMore,
                        isLoadingMore = false
                    )
                )
            } catch (e: ApiException) {
                updateState(state.copy(isLoadingMore = false))
                postEffect(VerificationSideEffect.Error(e.message))
            }
        }
    }

    private fun process(item: VerificationItem, accept: Boolean) {
        if (state.processingIds.contains(item.id)) return
        updateState(state.copy(processingIds = state.processingIds + item.id))
        viewModelScope.launch {
            try {
                useCase.setStatus(item.clubId, accept)
                updateState(
                    state.copy(
                        processingIds = state.processingIds - item.id,
                        items = state.items.filterNot { it.id == item.id }
                    )
                )
            } catch (e: ApiException) {
                updateState(state.copy(processingIds = state.processingIds - item.id))
                postEffect(VerificationSideEffect.Error(e.message))
            }
        }
    }

    private fun openClub(item: VerificationItem) {
        viewModelScope.launch {
            navigator.navigateTo(SharedRoutes.CLUB_DETAILS, ClubDetailsNavArgs(item.clubId))
        }
    }
}
