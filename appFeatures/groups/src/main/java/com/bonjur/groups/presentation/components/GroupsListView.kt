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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

    LaunchedEffect(Unit) {
        store.send(GroupsListAction.FetchData)
    }

    // Listen to pager changes (from swipe gesture)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                if (!isUpdatingFromPager) {
                    val segment = GroupsListViewState.SegmentType.fromIndex(currentPage)
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
                0 -> ClubsScrollView(clubs = store.state.uiModel.clubs)
                1 -> EventsScrollView(events = store.state.uiModel.events)
                2 -> HangoutsScrollView(hangouts = store.state.uiModel.hangouts)
            }
        }
    }
}

@Composable
private fun TopView(
    selectedSegment: GroupsListViewState.SegmentType,
    onSegmentChanged: (GroupsListViewState.SegmentType) -> Unit,
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
            text = "Groups",
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
                text = "",
                onTextChange = onSearchTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Segmented Picker
            val options = remember {
                GroupsListViewState.SegmentType.entries.map { type ->
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
                    val segment = GroupsListViewState.SegmentType.valueOf(option.id)
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
private fun ClubsScrollView(clubs: List<ClubCardModel>) {
    if (clubs.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(clubs, key = { it.uuid }) { club ->
                ClubCardView(
                    model = club,
                    onTap = {
                        // Handle club tap
                    }
                )
            }
        }
    } else {
        EmptyStateView(type = GroupsListViewState.SegmentType.CLUBS)
    }
}

@Composable
private fun EventsScrollView(events: List<EventsCardModel>) {
    if (events.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(events, key = { it.uuid }) { event ->
                EventsCardView(
                    model = event,
                    onButtonTap = {
                        // Handle button tap
                    },
                    onTap = {
                        // Handle event tap
                    }
                )
            }
        }
    } else {
        EmptyStateView(type = GroupsListViewState.SegmentType.EVENTS)
    }
}

@Composable
private fun HangoutsScrollView(hangouts: List<HangoutsCardModel>) {
    if (hangouts.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(hangouts, key = { it.uuid }) { hangout ->
                HangoutsCardView(
                    model = hangout,
                    onButtonTap = {
                        // Handle button tap
                    },
                    onTap = {
                        // Handle hangout tap
                    }
                )
            }
        }
    } else {
        EmptyStateView(type = GroupsListViewState.SegmentType.HANGOUTS)
    }
}

@Composable
private fun EmptyStateView(type: GroupsListViewState.SegmentType) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppEmptyView(
            model = AppEmptyModel(
                icon = Images.Icons.twoUsers(),
                text = "There are no ${type.title.lowercase()} for this community yet. Be the pioneer and start the very first one now!",
                buttonTitle = "Create a ${type.title.lowercase().removeSuffix("s")} +"
            ),
            onButtonClick = {
                // Handle create
            }
        )
    }
}