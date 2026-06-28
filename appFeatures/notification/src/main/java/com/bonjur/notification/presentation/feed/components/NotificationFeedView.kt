package com.bonjur.notification.presentation.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.notification.domain.models.NeedsActionSummary
import com.bonjur.notification.domain.models.NotificationFeedItem
import com.bonjur.notification.presentation.feed.models.NotificationFeedAction
import com.bonjur.notification.presentation.feed.models.NotificationFeedSideEffect
import com.bonjur.notification.presentation.feed.models.NotificationFeedViewState
import com.bonjur.notification.presentation.needsAction.components.Avatar

@Composable
fun NotificationFeedView(
    store: FeatureStore<NotificationFeedViewState, NotificationFeedAction, NotificationFeedSideEffect>
) {
    val inbox = store.state.inbox
    LaunchedEffect(Unit) { store.send(NotificationFeedAction.FetchData) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary.copy(alpha = 0.4f)),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 55.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (inbox.action.hasActions) {
            item {
                ActionBanner(inbox.action) { store.send(NotificationFeedAction.ActionBannerTapped) }
            }
        }
        inbox.sections.forEach { section ->
            item {
                Text(
                    section.title,
                    style = AppTypography.BodyTextMd.semiBold,
                    color = Palette.black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(section.items.size) { index ->
                FeedRow(section.items[index])
            }
        }
    }
}

@Composable
private fun ActionBanner(action: NeedsActionSummary, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.white)
            .border(1.dp, Palette.onBackground, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(Palette.green900),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", style = AppTypography.BodyTextMd.semiBold, color = Palette.white)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text("Needs your action", style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
            Text(
                "${action.requests} join requests · ${action.verifications} to verify",
                style = AppTypography.TextSm.regular,
                color = Palette.graySecondary
            )
        }
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Palette.cardBgRed))
    }
}

@Composable
private fun FeedRow(item: NotificationFeedItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.white)
            .border(1.dp, Palette.onBackground, RoundedCornerShape(16.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Avatar(item.imageUrl)
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
            Text(item.subtitle, style = AppTypography.TextL.regular, color = Palette.graySecondary)
            if (!item.note.isNullOrEmpty()) {
                Text(
                    item.note,
                    style = AppTypography.TextSm.regular,
                    color = Palette.graySecondary,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Palette.grayQuaternary)
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                )
            }
        }
        if (!item.isRead) {
            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Palette.secondary))
        }
    }
}
