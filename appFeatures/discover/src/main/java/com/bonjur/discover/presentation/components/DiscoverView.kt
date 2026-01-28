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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.models.ClubCardModel
import com.bonjur.clubs.presentation.components.ClubCardView
import com.bonjur.communities.CommunityCardModel
import com.bonjur.communities.CommunityCardView
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
import com.bonjur.events.presentation.models.EventsCardModel
import com.bonjur.events.presentation.components.EventsCardView
import com.bonjur.hangouts.HangoutsCardModel
import com.bonjur.hangouts.HangoutsCardView
import kotlin.collections.isNotEmpty

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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
                    .verticalScroll(rememberScrollState())
            ) {
                CommunitiesView(
                    communities = state.uiModel.communities,
                    onCommunityTap = { /* Handle tap */ }
                )

                ClubsView(
                    screenWidth = screenWidth,
                    clubs = state.uiModel.clubs,
                    onClubTap = { /* Handle tap */ },
                    onViewAll = { store.send(DiscoverAction.ViewAllTapped(AppUIEntities.ActivityType.CLUBS)) }
                )

                EventsView(
                    screenWidth = screenWidth,
                    events = state.uiModel.events,
                    clubs = state.uiModel.clubs,
                    onEventTap = { /* Handle tap */ },
                    onButtonTap = { /* Handle button */ },
                    onViewAll = { store.send(DiscoverAction.ViewAllTapped(AppUIEntities.ActivityType.EVENTS)) }
                )

                HangoutsView(
                    screenWidth = screenWidth,
                    hangouts = state.uiModel.hangouts,
                    onHangoutTap = { /* Handle tap */ },
                    onButtonTap = { /* Handle button */ },
                    onViewAll = { store.send(DiscoverAction.ViewAllTapped(AppUIEntities.ActivityType.HANG_OUTS)) }
                )
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
            ProfileView(user = state.uiModel.user)
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

                },
                onChipsHeightChanged = { height ->
                    filterViewHeight = height
                }
            )
        }
    }
}

@Composable
private fun ProfileView(user: UserModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile image
        IconButton(onClick = { /* Handle profile click */ }) {
            CachedAsyncImage(
                url = user.profileImage,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop,
                placeholder = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Palette.grayQuaternary, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = Images.Icons.user(),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Palette.black
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Palette.grayQuaternary, RoundedCornerShape(14.dp)),
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
            ) { imageBitmap ->
                Image(
                     bitmap = imageBitmap,
                     contentDescription = "Profile",
                     modifier = Modifier
                         .size(40.dp)
                         .clip(RoundedCornerShape(14.dp)),
                     contentScale = ContentScale.Crop
                 )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.greeting,
                style = AppTypography.TextMd.regular,
                color = Palette.grayPrimary,
                textAlign = TextAlign.Start
            )
            Text(
                text = user.name,
                style = AppTypography.BodyTextSm.medium,
                color = Palette.black,
                textAlign = TextAlign.Start
            )
        }

        IconButton(
            onClick = { /* Handle notification click */ },
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
private fun CommunitiesView(
    communities: List<CommunityCardModel>,
    onCommunityTap: (CommunityCardModel) -> Unit
) {
    var currentPage by remember { mutableStateOf(0) }

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
    onViewAll: () -> Unit
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
                items(clubs) { club ->
                    Box(modifier = Modifier.width(screenWidth - 60.dp)) {
                        ClubCardView(
                            model = club,
                            onTap = { onClubTap(club) }
                        )
                    }
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
    onViewAll: () -> Unit
) {
    if (events.isNotEmpty() && clubs.isNotEmpty()) {
        Column {
            SectionHeader(
                title = "Events",
                showViewAll = true,
                onViewAll = onViewAll
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(events) { event ->
                    Box(modifier = Modifier.width(screenWidth - 90.dp)) {
                        EventsCardView(
                            model = event,
                            onButtonTap = { onButtonTap(event) },
                            onTap = { onEventTap(event) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HangoutsView(
    screenWidth: Dp,
    hangouts: List<HangoutsCardModel>,
    onHangoutTap: (HangoutsCardModel) -> Unit,
    onButtonTap: (HangoutsCardModel) -> Unit,
    onViewAll: () -> Unit
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
                items(hangouts) { hangout ->
                    Box(modifier = Modifier.width(screenWidth - 90.dp)) {
                        HangoutsCardView(
                            model = hangout,
                            onButtonTap = { onButtonTap(hangout) },
                            onTap = { onHangoutTap(hangout) }
                        )
                    }
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