package com.bonjur.notification.presentation.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.hasScrollAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.notification.domain.models.NotificationFeedItem
import com.bonjur.notification.domain.models.NotificationFeedPage
import com.bonjur.notification.domain.models.NotificationTargetType
import com.bonjur.notification.domain.models.NotificationType
import com.bonjur.notification.domain.models.RequestPageResult
import com.bonjur.notification.domain.useCase.NeedsActionUseCase
import com.bonjur.notification.domain.useCase.NotificationUseCase
import com.bonjur.notification.presentation.feed.components.NotificationFeedView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * "Needs your action" is the first thing in the feed and must be on screen the
 * moment the screen opens — reported as sitting above the fold, needing a scroll.
 */
@RunWith(AndroidJUnit4::class)
class NeedsActionBannerVisibleTest {

    @get:Rule
    val compose = createComposeRule()

    private class FakeUseCase(private val rows: Int) : NotificationUseCase {
        override suspend fun fetchFeedPage(page: Int, size: Int) = NotificationFeedPage(
            items = (0 until rows).map {
                NotificationFeedItem(
                    id = "id-$it",
                    type = NotificationType.EVENT_REMINDER,
                    title = "Notification $it",
                    subtitle = "subtitle $it",
                    note = null,
                    imageUrl = null,
                    timeAgo = "now",
                    isRead = false,
                    targetType = NotificationTargetType.NONE,
                    createdAtMillis = System.currentTimeMillis()
                )
            },
            hasMore = false
        )
        override suspend fun markAllRead() {}
        override suspend fun markRead(id: String) {}
        override suspend fun fetchVerificationCount() = 0
        override suspend fun fetchUnreadCount() = 0
    }

    /** The banner's count comes from here; this screen only cares that the banner renders. */
    private class FakeNeedsActionUseCase : NeedsActionUseCase {
        private val empty = RequestPageResult(items = emptyList(), hasMore = false)
        override suspend fun fetchClubRequests(page: Int, size: Int) = empty
        override suspend fun fetchHangoutRequests(page: Int, size: Int) = empty
        override suspend fun fetchEventRequests(page: Int, size: Int) = empty
        override suspend fun setClubStatus(clubId: Int, userId: String, accept: Boolean) {}
        override suspend fun setHangoutStatus(hangoutId: String, userId: String, accept: Boolean) {}
        override suspend fun setEventStatus(eventId: String, userId: String, accept: Boolean) {}
        override suspend fun fetchVerificationCount() = 0
        override suspend fun fetchPendingActionCount() = 0
    }

    private fun showFeed(rows: Int) {
        val viewModel = NotificationFeedViewModel(FakeUseCase(rows), FakeNeedsActionUseCase())
        compose.setContent {
            // Mirrors NotificationFeedScreen's layout without the Hilt/navigator wiring.
            Column(modifier = Modifier.fillMaxSize()) {
                AppTopBar(isScrolled = true, showTitle = true, title = "Notification", onBack = {})
                NotificationFeedView(store = viewModel.store)
            }
        }
        compose.waitForIdle()
        Thread.sleep(800)
        compose.waitForIdle()
    }

    @Test
    fun bannerIsOnScreenWithAFullFeed() {
        showFeed(rows = 30)
        compose.onNodeWithText("Needs your action").assertIsDisplayed()
    }

    @Test
    fun bannerIsOnScreenWithAnEmptyFeed() {
        showFeed(rows = 0)
        compose.onNodeWithText("Needs your action").assertIsDisplayed()
    }

    /**
     * The card must survive scrolling: as a list item it disappeared once the feed
     * moved, and LazyColumn restores that offset on re-entry — which is how it
     * ended up off-screen the moment the screen opened.
     */
    @Test
    fun bannerStaysOnScreenAfterScrollingTheFeed() {
        showFeed(rows = 40)
        compose.onNodeWithText("Needs your action").assertIsDisplayed()

        compose.onNode(hasScrollAction()).performScrollToIndex(30)
        compose.waitForIdle()

        compose.onNodeWithText("Needs your action").assertIsDisplayed()
    }
}
