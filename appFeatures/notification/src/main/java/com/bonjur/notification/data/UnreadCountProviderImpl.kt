package com.bonjur.notification.data

import com.bonjur.navigation.UnreadCountProvider
import com.bonjur.network.model.ApiException
import com.bonjur.notification.domain.useCase.NotificationUseCase
import javax.inject.Inject

/**
 * Notification-side implementation of [UnreadCountProvider]. Bound app-wide so
 * the Discover bell can read the unread total without depending on this module.
 */
class UnreadCountProviderImpl @Inject constructor(
    private val useCase: NotificationUseCase
) : UnreadCountProvider {

    override suspend fun unreadCount(): Int = try {
        useCase.fetchUnreadCount()
    } catch (e: ApiException) {
        0
    }
}
