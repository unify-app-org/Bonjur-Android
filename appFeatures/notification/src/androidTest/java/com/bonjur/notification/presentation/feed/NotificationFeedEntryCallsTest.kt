package com.bonjur.notification.presentation.feed

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonjur.notification.domain.models.NotificationFeedPage
import com.bonjur.notification.domain.useCase.NotificationUseCase
import com.bonjur.notification.presentation.feed.models.NotificationFeedAction
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Opening the feed used to fire four extra calls just to fill the "Needs your
 * action" banner — three join-request probes plus the admin-only
 * `clubs/pending`, which 403s for everyone who isn't a president.
 * Entering the screen must now hit the feed and nothing else.
 */
@RunWith(AndroidJUnit4::class)
class NotificationFeedEntryCallsTest {

    private class RecordingUseCase : NotificationUseCase {
        val calls = CopyOnWriteArrayList<String>()

        override suspend fun fetchFeedPage(page: Int, size: Int): NotificationFeedPage {
            calls += "fetchFeedPage"
            return NotificationFeedPage(items = emptyList(), hasMore = false)
        }

        override suspend fun markAllRead() { calls += "markAllRead" }

        override suspend fun markRead(id: String) { calls += "markRead" }

        override suspend fun fetchVerificationCount(): Int {
            calls += "fetchVerificationCount"
            return 0
        }

        override suspend fun fetchUnreadCount(): Int {
            calls += "fetchUnreadCount"
            return 0
        }
    }

    @Test
    fun openingTheFeedOnlyLoadsTheFeed() {
        val useCase = RecordingUseCase()
        val viewModel = NotificationFeedViewModel(useCase)

        viewModel.store.send(NotificationFeedAction.FetchData)
        Thread.sleep(1_500)

        assertEquals(listOf("fetchFeedPage"), useCase.calls.toList())
    }
}
