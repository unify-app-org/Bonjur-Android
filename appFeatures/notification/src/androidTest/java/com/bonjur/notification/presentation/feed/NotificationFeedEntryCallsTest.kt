package com.bonjur.notification.presentation.feed

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonjur.notification.domain.models.NotificationFeedPage
import com.bonjur.notification.domain.models.RequestPageResult
import com.bonjur.notification.domain.useCase.NeedsActionUseCase
import com.bonjur.notification.domain.useCase.NotificationUseCase
import com.bonjur.notification.presentation.feed.models.NotificationFeedAction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Opening the feed once fired four extra calls just to fill the "Needs your action" banner.
 * Three were harmless count probes; the fourth, `clubs/pending`, is **admin-only and 403s for
 * everyone who isn't a president** — on every single open. All four were dropped 2026-08-22.
 *
 * 2026-08-24 the banner got its red dot + count back (requested), so the three count probes are
 * deliberately back — bundled behind one `fetchPendingActionCount()`. What must stay gone is the
 * admin probe, which is what this test now pins: entry loads the feed and the pending count, and
 * never touches `fetchVerificationCount` on either use case.
 */
@RunWith(AndroidJUnit4::class)
class NotificationFeedEntryCallsTest {

    private class RecordingUseCase(val calls: CopyOnWriteArrayList<String>) : NotificationUseCase {
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

    private class RecordingNeedsActionUseCase(
        val calls: CopyOnWriteArrayList<String>
    ) : NeedsActionUseCase {
        private val empty = RequestPageResult(items = emptyList(), hasMore = false)

        override suspend fun fetchClubRequests(page: Int, size: Int): RequestPageResult {
            calls += "fetchClubRequests"
            return empty
        }

        override suspend fun fetchHangoutRequests(page: Int, size: Int): RequestPageResult {
            calls += "fetchHangoutRequests"
            return empty
        }

        override suspend fun fetchEventRequests(page: Int, size: Int): RequestPageResult {
            calls += "fetchEventRequests"
            return empty
        }

        override suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean) {
            calls += "setClubStatus"
        }

        override suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean) {
            calls += "setHangoutStatus"
        }

        override suspend fun setEventStatus(eventId: String, userId: String, accept: Boolean) {
            calls += "setEventStatus"
        }

        override suspend fun fetchVerificationCount(): Int {
            calls += "fetchVerificationCount"
            return 0
        }

        override suspend fun fetchPendingActionCount(): Int {
            calls += "fetchPendingActionCount"
            return 0
        }
    }

    @Test
    fun openingTheFeedLoadsTheFeedAndTheBannerCountOnly() {
        val calls = CopyOnWriteArrayList<String>()
        val viewModel = NotificationFeedViewModel(
            RecordingUseCase(calls),
            RecordingNeedsActionUseCase(calls)
        )

        viewModel.store.send(NotificationFeedAction.FetchData)
        Thread.sleep(1_500)

        // The two run concurrently, so compare as a sorted set rather than a sequence.
        assertEquals(
            listOf("fetchFeedPage", "fetchPendingActionCount"),
            calls.toList().sorted()
        )
    }

    /** The 403-on-every-open regression, pinned on its own. */
    @Test
    fun openingTheFeedNeverProbesTheAdminVerificationQueue() {
        val calls = CopyOnWriteArrayList<String>()
        val viewModel = NotificationFeedViewModel(
            RecordingUseCase(calls),
            RecordingNeedsActionUseCase(calls)
        )

        viewModel.store.send(NotificationFeedAction.FetchData)
        Thread.sleep(1_500)

        assertTrue(
            "feed entry must not hit the admin-only verification queue, got $calls",
            calls.none { it == "fetchVerificationCount" }
        )
    }
}
