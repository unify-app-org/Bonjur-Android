//
//  DiscoverView.kt
//  Discover
//
//  Created by Huseyn Hasanov on 11.01.26
//

package com.bonjur.discover.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.clubs.presentation.list.components.ClubCardView
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.communities.presentation.list.components.CommunityCardView
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.tabView.AppTabView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.discover.domain.models.UserModel
import com.bonjur.discover.presentation.models.DiscoverAction
import com.bonjur.discover.presentation.models.DiscoverSideEffect
import com.bonjur.discover.presentation.models.DiscoverViewState
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.events.presentation.list.components.EventsCardView
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.hangouts.presentation.list.components.HangoutsCardView
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverView(
    store: FeatureStore<DiscoverViewState, DiscoverAction, DiscoverSideEffect>
) {
    val state = store.state

    val density = LocalDensity.current
    var scrollOffset by remember { mutableStateOf(0f) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var topViewHeight by remember { mutableStateOf(0.dp) }
    var profileViewHeight by remember { mutableStateOf(0.dp) }
    var filterViewHeight by remember { mutableStateOf(0.dp) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                scrollOffset += available.y
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(Unit) {
        store.send(DiscoverAction.FetchData)
    }

    // First ON_RESUME coincides with the initial fetch above, so skip it; every
    // later resume (returning from a detail screen) refetches the 4 activity
    // sections so join/request/exit/edit changes are reflected. Mirrors iOS's
    // onFirstAppear + onAppear(hasAppearedOnce) pair.
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasResumedOnce by remember { mutableStateOf(false) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (hasResumedOnce) {
                    store.send(DiscoverAction.RefreshActivities)
                }
                hasResumedOnce = true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(profileViewHeight, filterViewHeight) {
        topViewHeight = profileViewHeight + filterViewHeight
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(0f)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(topViewHeight))

            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { store.send(DiscoverAction.PullToRefresh) },
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(nestedScrollConnection)
                        .verticalScroll(rememberScrollState())
                ) {
                    CommunitiesView(
                        communities = state.uiModel.communities,
                        onCommunityTap = { item -> store.send(DiscoverAction.CommunityItemTapped(item.id)) },
                        onLoadMore = { store.send(DiscoverAction.LoadMore(AppUIEntities.ActivityType.COMMUNITY)) }
                    )

                    ClubsView(
                        screenWidth = screenWidth,
                        clubs = state.uiModel.clubs,
                        onClubTap = { item -> store.send(DiscoverAction.CLubItemTapped(item.id)) },
                        onViewAll = { store.send(DiscoverAction.ViewAllTapped(AppUIEntities.ActivityType.CLUBS)) },
                        onLoadMore = { store.send(DiscoverAction.LoadMore(AppUIEntities.ActivityType.CLUBS)) }
                    )

                    EventsView(
                        screenWidth = screenWidth,
                        events = state.uiModel.events,
                        clubs = state.uiModel.clubs,
                        onEventTap = { item -> store.send(DiscoverAction.EventItemTapped(item.id)) },
                        onButtonTap = { /* iOS events card button is a no-op here */ },
                        onViewAll = { store.send(DiscoverAction.ViewAllTapped(AppUIEntities.ActivityType.EVENTS)) },
                        onLoadMore = { store.send(DiscoverAction.LoadMore(AppUIEntities.ActivityType.EVENTS)) }
                    )

                    HangoutsView(
                        screenWidth = screenWidth,
                        hangouts = state.uiModel.hangouts,
                        onHangoutTap = { item -> store.send(DiscoverAction.HangoutItemTapped(item.id)) },
                        onButtonTap = { item -> store.send(DiscoverAction.JoinHangout(item.id)) },
                        onViewAll = { store.send(DiscoverAction.ViewAllTapped(AppUIEntities.ActivityType.HANG_OUTS)) },
                        onLoadMore = { store.send(DiscoverAction.LoadMore(AppUIEntities.ActivityType.HANG_OUTS)) }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .background(Palette.white)
                .zIndex(1f)
                .onGloballyPositioned { coordinates ->
                    profileViewHeight = with(density) { coordinates.size.height.toDp() }
                }
        ) {
            ProfileView(
                user = state.uiModel.user,
                onProfileTap = { store.send(DiscoverAction.ProfileTapped) },
                onNotification = { store.send(DiscoverAction.NotificationTapped) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .zIndex(2f)
                .padding(top = profileViewHeight)
        ) {
            FilterView(
                model = state.uiModel.filters,
                selectedItems = { items ->
                    store.send(DiscoverAction.FilterChanged(items.map { it.id }))
                },
                onChipsHeightChanged = { height ->
                    filterViewHeight = height
                }
            )
        }
    }
}

@Composable
private fun ProfileView(
    user: UserModel,
    onProfileTap: () -> Unit,
    onNotification: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile image
        IconButton(onClick = onProfileTap) {
            CachedAsyncImage(
                url = user.profileImage,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = { ProfilePlaceholder() },
                error = { ProfilePlaceholder() }
            ) { imageBitmap ->
                Image(
                     bitmap = imageBitmap,
                     contentDescription = "Profile",
                     modifier = Modifier
                         .size(36.dp)
                         .clip(CircleShape),
                     contentScale = ContentScale.Crop
                 )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = user.greeting,
                style = AppTypography.TextMd.regular,
                color = Palette.grayPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = user.name,
                style = AppTypography.BodyTextMd.bold,
                color = Palette.black,
                textAlign = TextAlign.Center
            )
        }

        IconButton(
            onClick = onNotification,
            modifier = Modifier
                .size(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Palette.grayQuaternary, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = Images.Icons.bell(),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(24.dp),
                    tint = Palette.black
                )
            }
        }
    }
}

@Composable
private fun ProfilePlaceholder() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(Palette.grayQuaternary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = Images.Icons.user(),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Palette.black
        )
    }
}

@Composable
private fun CommunitiesView(
    communities: List<CommunityCardModel>,
    onCommunityTap: (CommunityCardModel) -> Unit,
    onLoadMore: () -> Unit
) {
    // rememberSaveable so the page survives Discover leaving composition on nav.
    // Plain remember reset this to 0, and AppTabView's LaunchedEffect(currentPage)
    // then force-scrolled the (restored) pager back to the first item on return.
    var currentPage by rememberSaveable { mutableStateOf(0) }

    if (communities.isNotEmpty()) {
        Column {
            SectionHeader(
                title = "Communities",
                showViewAll = false,
                onViewAll = {}
            )

            AppTabView(
                currentPage = currentPage,
                pageCount = communities.size,
                onPageChange = { newPage ->
                    currentPage = newPage
                    if (newPage >= communities.lastIndex) onLoadMore()
                },
                modifier = Modifier
                    .height(200.dp)
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    CommunityCardView(
                        model = communities[page],
                        onTap = { onCommunityTap(communities[page]) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ClubsView(
    screenWidth: Dp,
    clubs: List<ClubCardModel>,
    onClubTap: (ClubCardModel) -> Unit,
    onViewAll: () -> Unit,
    onLoadMore: () -> Unit
) {
    Column {
        SectionHeader(
            title = "Clubs",
            showViewAll = clubs.isNotEmpty(),
            onViewAll = onViewAll
        )

        if (clubs.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(clubs) { index, club ->
                    Box(modifier = Modifier.width(screenWidth - 60.dp)) {
                        ClubCardView(
                            model = club,
                            onTap = { onClubTap(club) }
                        )
                    }
                    LoadMoreTrigger(index = index, lastIndex = clubs.lastIndex, onLoadMore = onLoadMore)
                }
            }
        } else {
            AppEmptyView(
                model = AppEmptyModel(
                    icon = Images.Icons.twoUsers(),
                    text = "There are no clubs for this community yet. Be the pioneer and start the very first one now!",
                    buttonTitle = "Create a club +"
                ),
                onButtonClick = { /* Handle create */ },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun EventsView(
    screenWidth: Dp,
    events: List<EventsCardModel>,
    clubs: List<ClubCardModel>,
    onEventTap: (EventsCardModel) -> Unit,
    onButtonTap: (EventsCardModel) -> Unit,
    onViewAll: () -> Unit,
    onLoadMore: () -> Unit
) {
    val isEmpty = events.isEmpty() || clubs.isEmpty()

    Column {
        SectionHeader(
            title = "Events",
            showViewAll = events.isNotEmpty(),
            onViewAll = onViewAll
        )

        if (!isEmpty) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(events) { index, event ->
                    Box(modifier = Modifier.width(screenWidth - 90.dp)) {
                        EventsCardView(
                            model = event,
                            onButtonTap = { onButtonTap(event) },
                            onTap = { onEventTap(event) }
                        )
                    }
                    LoadMoreTrigger(index = index, lastIndex = events.lastIndex, onLoadMore = onLoadMore)
                }
            }
        } else {
            AppEmptyView(
                model = AppEmptyModel(
                    icon = Images.Icons.twoUsers(),
                    text = "There are no event for the clubs yet. Be the pioneer and start the very first one now!",
                    buttonTitle = "Create events +"
                ),
                onButtonClick = { /* Handle create */ },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun HangoutsView(
    screenWidth: Dp,
    hangouts: List<HangoutsCardModel>,
    onHangoutTap: (HangoutsCardModel) -> Unit,
    onButtonTap: (HangoutsCardModel) -> Unit,
    onViewAll: () -> Unit,
    onLoadMore: () -> Unit
) {
    Column {
        SectionHeader(
            title = "Hangouts",
            showViewAll = hangouts.isNotEmpty(),
            onViewAll = onViewAll
        )

        if (hangouts.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(hangouts) { index, hangout ->
                    Box(modifier = Modifier.width(screenWidth - 90.dp)) {
                        HangoutsCardView(
                            model = hangout,
                            onButtonTap = { onButtonTap(hangout) },
                            onTap = { onHangoutTap(hangout) }
                        )
                    }
                    LoadMoreTrigger(index = index, lastIndex = hangouts.lastIndex, onLoadMore = onLoadMore)
                }
            }
        } else {
            AppEmptyView(
                model = AppEmptyModel(
                    icon = Images.Icons.twoUsers(),
                    text = "There are no hangouts for this community yet. Be the pioneer and start the very first one now!",
                    buttonTitle = "Create a hangout +"
                ),
                onButtonClick = { /* Handle create */ },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/// Compose equivalent of iOS `loadMoreIfNeeded(index == count - 1)`: when the
/// last item enters composition (i.e. scrolled into view), trigger a page load.
/// Keyed on lastIndex so it re-fires after the list grows and a new last item
/// appears, and not on every recomposition.
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
private fun SectionHeader(
    title: String,
    showViewAll: Boolean,
    onViewAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AppTypography.TitleSm.semiBold,
            color = Palette.black
        )

        if (showViewAll) {
            TextButton(onClick = onViewAll) {
                Text(
                    text = "view all",
                    style = AppTypography.TextL.semiBold,
                    color = Palette.black,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}