package com.bonjur.notification.presentation.feed.models

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.notification.R
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.notification.domain.models.NotificationFeedItem
import com.bonjur.notification.domain.models.NotificationInbox
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase

data class NotificationFeedViewState(
    val inbox: NotificationInbox = NotificationInbox.empty,
    /** First-load lifecycle (spinner / empty / error). Reuses the NeedsAction phase enum. */
    val phase: RequestsPhase = RequestsPhase.IDLE,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = false,
    /** Row currently shown in the modal preview sheet; null = sheet hidden. */
    val previewItem: NotificationFeedItem? = null,
    /** Pending join requests behind the "Needs your action" banner. Drives its red dot. */
    val pendingActionCount: Int = 0
) : FeatureState

sealed class NotificationFeedSideEffect : SideEffect {
    data class Error(val message: String?) : NotificationFeedSideEffect()
}

sealed class NotificationFeedAction : FeatureAction {
    object FetchData : NotificationFeedAction()
    object Retry : NotificationFeedAction()
    object LoadMore : NotificationFeedAction()
    object MarkAllRead : NotificationFeedAction()
    object ActionBannerTapped : NotificationFeedAction()
    /** Row tap → open the modal preview sheet. */
    data class ItemTapped(val id: String) : NotificationFeedAction()
    /** Preview "Continue" → deep-link to the target entity, then close. */
    object PreviewContinue : NotificationFeedAction()
    /** Preview "Close" / dismiss. */
    object DismissPreview : NotificationFeedAction()
}
