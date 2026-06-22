//
//  GroupsListView.kt
//  Groups
//
//  Created by Huseyn Hasanov on 23.01.26
//

package com.bonjur.groups.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.list.components.ClubCardView
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.segmentView.CapsuleSegmentedPicker
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.designSystem.components.serach.SearchView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.events.presentation.list.components.EventsCardView
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.groups.presentation.models.GroupsListAction
import com.bonjur.groups.presentation.models.GroupsListSideEffect
import com.bonjur.groups.presentation.models.GroupsListViewState
import com.bonjur.groups.presentation.models.GroupsListViewState.SegmentType
import com.bonjur.hangouts.presentation.list.components.HangoutsCardView
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import kotlinx.coroutines.launch
import kotlin.collections.isNotEmpty

@Composable
fun GroupsListView(
    store: FeatureStore<GroupsListViewState, GroupsListAction, GroupsListSideEffect>
) {

    val pagerState = rememberPagerState(
        initialPage = store.state.selectedSegment.toIndex(),
        pageCount = { 3 }
    )
    val coroutineScope = rememberCoroutineScope()
    var isUpdatingFromPager by remember { mutableStateOf(false) }

    // First load fires on composition (guaranteed; the bottom-tab host doesn't reliably
    // reach RESUMED, so ON_RESUME alone can miss the first load). Mirrors iOS
    // `.onAppear { fetchData }`: the ON_RESUME observer below then refetches on every
    // *return* (e.g. back from a detail screen) so join/exit changes show. A skip-first
    // guard avoids a double fetch when composition and the initial resume coincide.
    LaunchedEffect(Unit) {
        store.send(GroupsListAction.FetchData)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    var hasResumedOnce by remember { mutableStateOf(false) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (hasResumedOnce) {
                    store.send(GroupsListAction.FetchData)
                }
                hasResumedOnce = true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Listen to pager changes (from swipe gesture)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                if (!isUpdatingFromPager) {
                    val segment = SegmentType.fromIndex(currentPage)
                    if (store.state.selectedSegment != segment) {
                        store.send(GroupsListAction.SegmentChanged(segment))
                    }
                }
            }
    }

    // Listen to segment changes (from picker tap)
    LaunchedEffect(store.state.selectedSegment) {
        val targetPage = store.state.selectedSegment.toIndex()
        if (pagerState.currentPage != targetPage) {
            isUpdatingFromPager = true
            coroutineScope.launch {
                pagerState.animateScrollToPage(targetPage)
                isUpdatingFromPager = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopView(
            searchText = store.state.searchText,
            selectedSegment = store.state.selectedSegment,
            onSegmentChanged = { segment ->
                store.send(GroupsListAction.SegmentChanged(segment))
            },
            onSearchTextChanged = { text ->
                store.send(GroupsListAction.SearchTextChanged(text))
            }
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> ClubsScrollView(
                    clubs = store.state.uiModel.clubs,
                    onItemTap = { id -> store.send(GroupsListAction.ClubItemTapped(id)) },
                    onLoadMore = { store.send(GroupsListAction.LoadMoreClubs) }
                )
                1 -> EventsScrollView(
                    events = store.state.uiModel.events,
                    onItemTap = { id -> store.send(GroupsListAction.EventItemTapped(id)) }
                )
                2 -> HangoutsScrollView(
                    hangouts = store.state.uiModel.hangouts,
                    onItemTap = { id -> store.send(GroupsListAction.HangoutItemTapped(id)) },
                    onLoadMore = { store.send(GroupsListAction.LoadMoreHangouts) }
                )
            }
        }
    }
}

@Composable
private fun TopView(
    searchText: String,
    selectedSegment: SegmentType,
    onSegmentChanged: (SegmentType) -> Unit,
    onSearchTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Palette.white),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Title
        Text(
            text = "My activities",
            style = AppTypography.TitleL.extraBold,
            color = Palette.black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            textAlign = TextAlign.Start
        )

        // Search and Segment
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            SearchView(
                text = searchText,
                onTextChange = onSearchTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Segmented Picker
            val options = remember {
                SegmentType.entries.map { type ->
                    object : SegmentedPickerOption {
                        override val title = type.title
                        override val id = type.name
                    }
                }
            }

            val selectedOption = remember(selectedSegment) {
                options.first { it.id == selectedSegment.name }
            }

            CapsuleSegmentedPicker(
                options = options,
                selectedOption = selectedOption,
                onOptionSelected = { option ->
                    val segment = SegmentType.valueOf(option.id)
                    onSegmentChanged(segment)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun ClubsScrollView(
    clubs: List<ClubCardModel>,
    onItemTap: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    if (clubs.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { TabCaption(SegmentType.CLUBS) }
            itemsIndexed(clubs, key = { _, it -> it.uuid }) { index, club ->
                ClubCardView(
                    model = club,
                    onTap = { onItemTap(club.id) }
                )
                LoadMoreTrigger(index = index, lastIndex = clubs.lastIndex, onLoadMore = onLoadMore)
            }
        }
    } else {
        EmptyStateView(type = SegmentType.CLUBS)
    }
}

@Composable
private fun EventsScrollView(
    events: List<EventsCardModel>,
    onItemTap: (String) -> Unit
) {
    if (events.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { TabCaption(SegmentType.EVENTS) }
            items(events, key = { it.uuid }) { event ->
                EventsCardView(
                    model = event,
                    onButtonTap = { /* iOS card button is a no-op in Groups */ },
                    onTap = { onItemTap(event.id) }
                )
            }
        }
    } else {
        EmptyStateView(type = SegmentType.EVENTS)
    }
}

@Composable
private fun HangoutsScrollView(
    hangouts: List<HangoutsCardModel>,
    onItemTap: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    if (hangouts.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { TabCaption(SegmentType.HANGOUTS) }
            itemsIndexed(hangouts, key = { _, it -> it.uuid }) { index, hangout ->
                HangoutsCardView(
                    model = hangout,
                    onButtonTap = { /* iOS card button is a no-op in Groups */ },
                    onTap = { onItemTap(hangout.id) }
                )
                LoadMoreTrigger(index = index, lastIndex = hangouts.lastIndex, onLoadMore = onLoadMore)
            }
        }
    } else {
        EmptyStateView(type = SegmentType.HANGOUTS)
    }
}

/// Short description of what the active tab lists, shown above its cards (iOS `tabCaption`).
@Composable
private fun TabCaption(segment: SegmentType) {
    Text(
        text = segment.caption,
        style = AppTypography.TextSm.medium,
        color = Palette.blackMedium,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}

/// Compose equivalent of iOS `loadMoreXIfNeeded(index == count - 1)`: fires once
/// when the last item enters composition, re-fires after the list grows.
@Composable
private fun LoadMoreTrigger(
    index: Int,
    lastIndex: Int,
    onLoadMore: () -> Unit
) {
    if (index == lastIndex && lastIndex >= 0) {
        LaunchedEffect(lastIndex) {
            onLoadMore()
        }
    }
}

@Composable
private fun EmptyStateView(type: SegmentType) {
    val icon = when (type) {
        SegmentType.CLUBS -> Images.Icons.twoUsers()
        SegmentType.EVENTS -> Images.Icons.calendar()
        SegmentType.HANGOUTS -> Images.Icons.twoUsers()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppEmptyView(
            model = AppEmptyModel(
                icon = icon,
                text = type.emptyText,
                buttonTitle = type.emptyButtonTitle
            ),
            onButtonClick = { /* iOS empty-state button is a no-op in Groups */ }
        )
    }
}
