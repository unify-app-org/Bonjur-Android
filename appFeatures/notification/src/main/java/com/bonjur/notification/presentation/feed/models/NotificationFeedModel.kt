package com.bonjur.notification.presentation.feed.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.notification.domain.models.NotificationInbox

data class NotificationFeedViewState(
    val inbox: NotificationInbox = NotificationInbox.empty
) : FeatureState

sealed class NotificationFeedSideEffect : SideEffect {
    data class Error(val message: String?) : NotificationFeedSideEffect()
}

sealed class NotificationFeedAction : FeatureAction {
    object FetchData : NotificationFeedAction()
    object MarkAllRead : NotificationFeedAction()
    object ActionBannerTapped : NotificationFeedAction()
}
