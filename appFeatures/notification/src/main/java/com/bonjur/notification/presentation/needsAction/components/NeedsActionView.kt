package com.bonjur.notification.presentation.needsAction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.segmentView.CapsuleSegmentedPicker
import com.bonjur.designSystem.components.tabView.AppTabView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.notification.domain.models.ActionRequestItem
import com.bonjur.notification.domain.models.RelativeTime
import com.bonjur.notification.presentation.needsAction.models.ActionTab
import com.bonjur.notification.presentation.needsAction.models.NeedsActionAction
import com.bonjur.notification.presentation.needsAction.models.NeedsActionSideEffect
import com.bonjur.notification.presentation.needsAction.models.NeedsActionViewState
import com.bonjur.notification.presentation.needsAction.models.RequestSourceState
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase

@Composable
fun NeedsActionView(store: FeatureStore<NeedsActionViewState, NeedsActionAction, NeedsActionSideEffect>) {
    val state = store.state

    LaunchedEffect(Unit) { store.send(NeedsActionAction.OnAppear) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary.copy(alpha = 0.4f))
    ) {
        if (state.isAdmin) {
            VerificationBanner(state.verificationCount) { store.send(NeedsActionAction.VerificationTapped) }
        }

        CapsuleSegmentedPicker(
            options = ActionTab.entries,
            selectedOption = state.selectedTab,
            onOptionSelected = { store.send(NeedsActionAction.SelectTab(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        )

        val tabs = ActionTab.entries
        AppTabView(
            pageCount = tabs.size,
            currentPage = tabs.indexOf(state.selectedTab),
            onPageChange = { store.send(NeedsActionAction.SelectTab(tabs[it])) },
            modifier = Modifier.weight(1f)
        ) { page ->
            when (tabs[page]) {
                ActionTab.EVENTS -> ComingSoon(
                    "No event actions yet",
                    "Event approvals will appear here once events go live."
                )
                ActionTab.HANGOUTS -> RequestsTab(store, ActionTab.HANGOUTS, state.hangouts, "No pending hangout requests right now.")
                ActionTab.CLUBS -> RequestsTab(store, ActionTab.CLUBS, state.clubs, "No pending club requests right now.")
            }
        }
    }
}

@Composable
private fun VerificationBanner(count: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Palette.green900),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", style = AppTypography.BodyTextMd.semiBold, color = Palette.white)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text("Verification requests", style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
            Text(
                "$count clubs awaiting your approval",
                style = AppTypography.TextSm.regular,
                color = Palette.graySecondary
            )
        }
        if (count > 0) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Palette.cardBgRed)
            )
        }
    }
}

@Composable
private fun RequestsTab(
    store: FeatureStore<NeedsActionViewState, NeedsActionAction, NeedsActionSideEffect>,
    tab: ActionTab,
    source: RequestSourceState,
    emptySubtitle: String
) {
    if (source.items.isNotEmpty()) {
        RequestsList(store, tab, source)
        return
    }
    when (source.phase) {
        RequestsPhase.IDLE, RequestsPhase.LOADING -> LoadingState()
        RequestsPhase.FAILED -> ErrorState { store.send(NeedsActionAction.Retry(tab)) }
        RequestsPhase.LOADED -> ComingSoon("You're all caught up", emptySubtitle)
    }
}

@Composable
private fun RequestsList(
    store: FeatureStore<NeedsActionViewState, NeedsActionAction, NeedsActionSideEffect>,
    tab: ActionTab,
    source: RequestSourceState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(source.items, key = { it.id }) { item ->
            if (item.id == source.items.last().id && source.canLoadMore) {
                LaunchedEffect(item.id) { store.send(NeedsActionAction.LoadMore(tab)) }
            }
            RequestRow(
                item = item,
                isProcessing = store.state.processingIds.contains(item.id),
                onTap = { store.send(NeedsActionAction.RequestTapped(item)) },
                onAccept = { store.send(NeedsActionAction.Accept(item)) },
                onReject = { store.send(NeedsActionAction.Reject(item)) }
            )
        }
        if (source.isLoadingMore) {
            item {
                Box(Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun RequestRow(
    item: ActionRequestItem,
    isProcessing: Boolean,
    onTap: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.white)
            .border(1.dp, Palette.onBackground, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onTap),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Avatar(item.avatarUrl)
            Column(modifier = Modifier.weight(1f)) {
                Text(item.requesterName, style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
                Text(item.targetName, style = AppTypography.TextL.regular, color = Palette.graySecondary)
            }
        }
        val time = RelativeTime.short(item.createdAtMillis)
        if (time.isNotEmpty()) {
            Text(
                time,
                style = AppTypography.TextSm.regular,
                color = Palette.graySecondary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (isProcessing) {
            Box(Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppButton(
                    title = "Reject",
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    model = AppButtonModel(type = ButtonType.Destructive, contentSize = ContentSize.Fill, size = AppButtonSize.Small)
                )
                AppButton(
                    title = "Accept",
                    onClick = onAccept,
                    modifier = Modifier.weight(1f),
                    model = AppButtonModel(type = ButtonType.Primary, contentSize = ContentSize.Fill, size = AppButtonSize.Small)
                )
            }
        }
    }
}

@Composable
internal fun Avatar(url: String?) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Palette.grayQuaternary),
        contentAlignment = Alignment.Center
    ) {
        if (!url.isNullOrEmpty()) {
            CachedAsyncImage(
                url = url,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = {},
                error = {}
            )
        } else {
            Text("👤", style = AppTypography.BodyTextMd.regular)
        }
    }
}

@Composable
internal fun LoadingState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ErrorState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Couldn't load requests", style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
        Spacer(Modifier.height(8.dp))
        Text(
            "Try again",
            style = AppTypography.BodyTextMd.semiBold,
            color = Palette.green900,
            modifier = Modifier.clickable(onClick = onRetry)
        )
    }
}

@Composable
internal fun ComingSoon(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
        Spacer(Modifier.height(6.dp))
        Text(
            subtitle,
            style = AppTypography.TextL.regular,
            color = Palette.graySecondary,
            textAlign = TextAlign.Center
        )
    }
}
