package com.bonjur.notification.presentation.feed

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.model.ApiException
import com.bonjur.notification.domain.useCase.NotificationUseCase
import com.bonjur.notification.navigation.NotificationScreens
import com.bonjur.notification.presentation.feed.models.NotificationFeedAction
import com.bonjur.notification.presentation.feed.models.NotificationFeedSideEffect
import com.bonjur.notification.presentation.feed.models.NotificationFeedViewState
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

    fun init(navigator: Navigator) {
        if (::navigator.isInitialized) return
        this.navigator = navigator
    }

    override fun handle(action: NotificationFeedAction) {
        when (action) {
            NotificationFeedAction.FetchData -> fetchData()
            NotificationFeedAction.MarkAllRead -> markAllRead()
            NotificationFeedAction.ActionBannerTapped ->
                viewModelScope.launch { navigator.navigateTo(NotificationScreens.NeedsAction.route) }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val inbox = useCase.fetchInbox()
                updateState(state.copy(inbox = inbox))
            } catch (e: ApiException) {
                postEffect(NotificationFeedSideEffect.Error(e.message))
            }
        }
        refreshRequestCount()
    }

    /** Overrides the banner's `requests` count with the live total. Verifications
     * stay on the mock inbox value until that flows through the feed. */
    private fun refreshRequestCount() {
        viewModelScope.launch {
            try {
                val requests = useCase.fetchRequestCount()
                updateState(
                    state.copy(inbox = state.inbox.copy(action = state.inbox.action.copy(requests = requests)))
                )
            } catch (e: ApiException) {
                // Secondary number — leave the mock value on failure.
            }
        }
    }

    private fun markAllRead() {
        // Optimistic: clear unread locally, then tell the backend.
        val cleared = state.inbox.sections.map { section ->
            section.copy(items = section.items.map { it.copy(isRead = true) })
        }
        updateState(state.copy(inbox = state.inbox.copy(sections = cleared)))
        viewModelScope.launch {
            try {
                useCase.markAllRead()
            } catch (e: ApiException) {
                postEffect(NotificationFeedSideEffect.Error(e.message))
            }
        }
    }
}
