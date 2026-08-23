package com.bonjur.notification.presentation.feed.components

import androidx.compose.ui.res.stringResource
import com.bonjur.notification.R
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designsystem.R as DesignR
import com.bonjur.notification.domain.models.NotificationFeedItem
import com.bonjur.notification.domain.models.NotificationTargetType
import com.bonjur.notification.domain.models.RelativeTime
import com.bonjur.notification.domain.models.NotificationType
import com.bonjur.notification.presentation.feed.models.NotificationFeedAction
import com.bonjur.notification.presentation.feed.models.NotificationFeedSideEffect
import com.bonjur.notification.presentation.feed.models.NotificationFeedViewState
import com.bonjur.notification.presentation.needsAction.components.ComingSoon
import com.bonjur.notification.presentation.needsAction.components.ErrorState
import com.bonjur.notification.presentation.needsAction.components.LoadingState
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase

@Composable
fun NotificationFeedView(
    store: FeatureStore<NotificationFeedViewState, NotificationFeedAction, NotificationFeedSideEffect>
) {
    val state = store.state
    LaunchedEffect(Unit) { store.send(NotificationFeedAction.FetchData) }

    NotificationFeedContent(store)

    state.previewItem?.let { item ->
        NotificationDetailSheet(
            item = item,
            onContinue = { store.send(NotificationFeedAction.PreviewContinue) },
            onClose = { store.send(NotificationFeedAction.DismissPreview) },
            onDismiss = { store.send(NotificationFeedAction.DismissPreview) }
        )
    }
}

@Composable
private fun NotificationFeedContent(
    store: FeatureStore<NotificationFeedViewState, NotificationFeedAction, NotificationFeedSideEffect>
) {
    val state = store.state
    Column(modifier = Modifier.fillMaxSize()) {
        // Pinned above the feed, not an item inside it. As a list item it went
        // off-screen whenever LazyColumn restored a previous scroll position on
        // re-entry, so the entry point was only reachable by scrolling back up.
        Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)) {
            ActionBanner { store.send(NotificationFeedAction.ActionBannerTapped) }
        }

        if (state.inbox.sections.isEmpty()) {
            when (state.phase) {
                RequestsPhase.IDLE, RequestsPhase.LOADING -> LoadingState()
                RequestsPhase.FAILED -> ErrorState { store.send(NotificationFeedAction.Retry) }
                RequestsPhase.LOADED -> ComingSoon(
                    stringResource(R.string.notif_all_caught_up),
                    stringResource(R.string.notif_empty_desc)
                )
            }
        } else {
            FeedList(store)
        }
    }
}

@Composable
private fun FeedList(
    store: FeatureStore<NotificationFeedViewState, NotificationFeedAction, NotificationFeedSideEffect>
) {
    val inbox = store.state.inbox
    val flatItems = inbox.sections.flatMap { it.items }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary.copy(alpha = 0.4f)),
        // Clearance for the tab dock only — the system navigation bar is handled
        // once, globally, in AppNavigation.
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 55.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        inbox.sections.forEach { section ->
            item(key = "section-${section.title}") {
                Text(
                    section.title,
                    style = AppTypography.BodyTextMd.semiBold,
                    color = Palette.black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(section.items, key = { it.id }) { item ->
                if (item.id == flatItems.last().id && store.state.canLoadMore) {
                    LaunchedEffect(item.id) { store.send(NotificationFeedAction.LoadMore) }
                }
                FeedRow(item) { store.send(NotificationFeedAction.ItemTapped(item.id)) }
            }
        }
        if (store.state.isLoadingMore) {
            item {
                Box(Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun ActionBanner(onClick: () -> Unit) {
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
            Text(stringResource(R.string.notif_needs_action), style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
            Text(
                stringResource(R.string.notif_action_subtitle_idle),
                style = AppTypography.TextSm.regular,
                color = Palette.graySecondary
            )
        }
    }
}

@Composable
private fun FeedRow(item: NotificationFeedItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.white)
            .border(1.dp, Palette.onBackground, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NotificationAvatar(item)
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
            // Recomputed at render so a screen left open doesn't keep showing
            // the stamp baked in at map time.
            val time = RelativeTime.short(item.createdAtMillis)
            if (time.isNotEmpty()) {
                Text(
                    time,
                    style = AppTypography.TextSm.regular,
                    color = Palette.graySecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        if (!item.isRead) {
            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(Palette.secondary))
        }
    }
}

// MARK: - Type-aware avatar / icon cell

@Composable
private fun NotificationAvatar(item: NotificationFeedItem) {
    val shape = RoundedCornerShape(14.dp)
    if (item.type.prefersRemoteImage && !item.imageUrl.isNullOrEmpty()) {
        Box(
            modifier = Modifier.size(48.dp).clip(shape),
            contentAlignment = Alignment.Center
        ) {
            CachedAsyncImage(
                url = item.imageUrl,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Crop,
                placeholder = { NotificationIconCell(item.type) },
                error = { NotificationIconCell(item.type) }
            )
        }
    } else {
        NotificationIconCell(item.type)
    }
}

@Composable
private fun NotificationIconCell(type: NotificationType) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(iconBackground(type)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = notificationIconPainter(type),
            contentDescription = null,
            tint = Palette.green900,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun notificationIconPainter(type: NotificationType) = painterResource(
    when (type) {
        NotificationType.BIRTHDAY -> DesignR.drawable.ic_gift
        NotificationType.HOLIDAY -> DesignR.drawable.ic_party

        NotificationType.EVENT_REMINDER,
        NotificationType.REQUEST_EVENT,
        NotificationType.USER_REQUESTED_PRIVATE_EVENT,
        NotificationType.REJECTED_USER_FROM_EVENT,
        NotificationType.ACCEPTED_USER_FROM_EVENT -> DesignR.drawable.ic_calendar

        NotificationType.REQUEST_OUTCOME,
        NotificationType.REQUEST_CLUB,
        NotificationType.REJECTED_USER_FROM_CLUB,
        NotificationType.ACCEPTED_USER_FROM_CLUB,
        NotificationType.REQUEST_HANGOUT,
        NotificationType.USER_REQUESTED_PRIVATE_CLUB,
        NotificationType.USER_REQUESTED_PRIVATE_HANGOUT,
        NotificationType.REJECTED_USER_FROM_HANGOUT,
        NotificationType.ACCEPTED_USER_FROM_HANGOUT -> DesignR.drawable.ic_two_users

        NotificationType.USER_JOINED_PUBLIC_CLUB,
        NotificationType.USER_JOINED_PUBLIC_HANGOUT -> DesignR.drawable.ic_users_group

        NotificationType.VERIFICATION_OUTCOME,
        NotificationType.REQUEST_CLUB_VERIFICATION,
        NotificationType.VERIFIED_CLUB,
        NotificationType.REJECTED_CLUB_VERIFICATION -> DesignR.drawable.ic_verified_seal

        NotificationType.GENERAL -> DesignR.drawable.ic_bell
    }
)

private fun iconBackground(type: NotificationType): Color = when (type) {
    NotificationType.BIRTHDAY, NotificationType.HOLIDAY -> Palette.cardBgTertiary.copy(alpha = 0.5f)
    else -> Palette.grayQuaternary
}

// MARK: - Detail preview sheet

/**
 * Modal preview of a single notification: hero image/icon, title, body, optional
 * note and one CTA. Actionable rows (targetType != NONE) show "Continue"
 * (deep-links to the target); the rest show "Close". Compose port of iOS
 * `NotificationDetailView`.
 */
@Composable
private fun NotificationDetailSheet(
    item: NotificationFeedItem,
    onContinue: () -> Unit,
    onClose: () -> Unit,
    onDismiss: () -> Unit
) {
    val isActionable = item.targetType != NotificationTargetType.NONE
    AppBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailHero(item)
            Text(item.title, style = AppTypography.TitleSm.bold, color = Palette.black)
            Text(item.subtitle, style = AppTypography.BodyTextMd.regular, color = Palette.blackMedium)
            if (!item.note.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Palette.grayQuaternary)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(stringResource(R.string.notif_note), style = AppTypography.CaptionMd.medium, color = Palette.graySecondary)
                    Text(item.note, style = AppTypography.TextL.regular, color = Palette.black)
                }
            }
            Spacer(Modifier.height(4.dp))
            AppButton(
                title = if (isActionable) stringResource(R.string.notif_continue) else stringResource(R.string.notif_close),
                onClick = { if (isActionable) onContinue() else onClose() },
                modifier = Modifier.fillMaxWidth(),
                model = AppButtonModel(
                    type = ButtonType.Primary,
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Large
                )
            )
        }
    }
}

@Composable
private fun DetailHero(item: NotificationFeedItem) {
    val shape = RoundedCornerShape(18.dp)
    if (item.type.prefersRemoteImage && !item.imageUrl.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(shape),
            contentAlignment = Alignment.Center
        ) {
            CachedAsyncImage(
                url = item.imageUrl,
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop,
                placeholder = { LocalHero(item.type) },
                error = { LocalHero(item.type) }
            )
        }
    } else {
        LocalHero(item.type)
    }
}

@Composable
private fun LocalHero(type: NotificationType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 160.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Palette.grayQuaternary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = notificationIconPainter(type),
            contentDescription = null,
            tint = Palette.black,
            modifier = Modifier.size(48.dp)
        )
    }
}
