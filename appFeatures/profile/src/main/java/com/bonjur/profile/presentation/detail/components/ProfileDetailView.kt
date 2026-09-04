package com.bonjur.profile.presentation.detail.components

import com.bonjur.designSystem.components.imagePreview.ImagePreviewable
import com.bonjur.designsystem.R as DesignR
import androidx.compose.ui.res.stringResource
import com.bonjur.profile.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.bonjur.designSystem.commonModel.AppUIEntities
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.list.components.ClubCardView
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.components.InfoContainer.AppInfoContainer
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.segmentView.CapsuleSegmentedPicker
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.events.presentation.list.components.EventsCardView
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.components.HangoutsCardView
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.detail.models.ProfileDetailAction
import com.bonjur.profile.presentation.detail.models.ProfileDetailSideEffect
import com.bonjur.profile.presentation.detail.models.ProfileDetailViewState
import kotlinx.coroutines.launch
import com.bonjur.designSystem.components.paging.LoadMoreOnScrollToEnd
import com.bonjur.designSystem.components.paging.PagingFooter

@Composable
fun ProfileDetailView(
    store: FeatureStore<ProfileDetailViewState, ProfileDetailAction, ProfileDetailSideEffect>
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = store.state.selectedSegment.toIndex(),
        pageCount = { ProfileDetailViewState.SegmentTypes.entries.size }
    )

    val options = remember {
        ProfileDetailViewState.SegmentTypes.entries.map { type ->
            object : SegmentedPickerOption {
                override val title = type.title
                override val id = type.name
            }
        }
    }

    val selectedOption = remember(store.state.selectedSegment) {
        options.first { it.id == store.state.selectedSegment.name }
    }

    var isSegmentSticky by remember { mutableStateOf(false) }
    var navBarHeight by remember { mutableStateOf(0.dp) }
    var isUpdatingFromPager by remember { mutableStateOf(false) }

    // Sync pager → store (swipe gesture)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (!isUpdatingFromPager) {
                val segment = ProfileDetailViewState.SegmentTypes.fromIndex(page)
                if (store.state.selectedSegment != segment) {
                    store.send(ProfileDetailAction.SegmentTapped(segment))
                }
            }
        }
    }

    // Sync store → pager (picker tap)
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

    // Each tab's rows live in an eager Column inside the single "tabs" lazy item, so
    // a per-row callback would fire on entry and pull every page at once. Drive paging
    // off the outer list's scroll position instead.
    val selectedSegmentHasMore = when (store.state.selectedSegment) {
        ProfileDetailViewState.SegmentTypes.CLUBS -> store.state.clubsHasMore
        ProfileDetailViewState.SegmentTypes.EVENTS -> store.state.eventsHasMore
        ProfileDetailViewState.SegmentTypes.HANGOUTS -> store.state.hangoutsHasMore
    }
    LoadMoreOnScrollToEnd(
        listState = listState,
        enabled = selectedSegmentHasMore
    ) {
        store.send(
            when (store.state.selectedSegment) {
                ProfileDetailViewState.SegmentTypes.CLUBS -> ProfileDetailAction.LoadMoreClubs
                ProfileDetailViewState.SegmentTypes.EVENTS -> ProfileDetailAction.LoadMoreEvents
                ProfileDetailViewState.SegmentTypes.HANGOUTS -> ProfileDetailAction.LoadMoreHangouts
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.white)
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Spacer for nav bar
            item(key = "nav_spacer") {
                Spacer(modifier = Modifier.height(navBarHeight))
            }

            // Compact header (avatar + name + subtitle + User Card ID chip)
            item(key = "compact_header") {
                store.state.uiModel?.userCardModel?.let { cardModel ->
                    CompactHeaderView(
                        card = cardModel,
                        isOwnProfile = store.state.isOwnProfile,
                        onCardTap = { store.send(ProfileDetailAction.UserCardTapped) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // User info
            item(key = "user_info") {
                UserInfoView(
                    uiModel = store.state.uiModel,
                    isOwnProfile = store.state.isOwnProfile,
                    onEditTap = { store.send(ProfileDetailAction.EditProfileTapped) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Segment picker (non-sticky)
            item(key = "segment") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp)
                        .onGloballyPositioned { coordinates ->
                            val yPos = coordinates.positionInRoot().y
                            val navBarPx = with(density) { navBarHeight.toPx() }
                            isSegmentSticky = yPos <= navBarPx
                        }
                ) {
                    if (!isSegmentSticky) {
                        CapsuleSegmentedPicker(
                            options = options,
                            selectedOption = selectedOption,
                            onOptionSelected = { option ->
                                val segment = ProfileDetailViewState.SegmentTypes.valueOf(option.id)
                                store.send(ProfileDetailAction.SegmentTapped(segment))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }
            }

            // Tab pager content
            item(key = "tabs") {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val segment = ProfileDetailViewState.SegmentTypes.fromIndex(page)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        when (segment) {
                            ProfileDetailViewState.SegmentTypes.CLUBS ->
                                ClubsTab(
                                    clubs = store.state.uiModel?.clubs ?: emptyList(),
                                    onItemTapped = { id ->
                                        store.send(ProfileDetailAction.ClubsItemTapped(id))
                                    },
                                    onCreate = {
                                        store.send(
                                            ProfileDetailAction.EmptyStateActionTapped(
                                                ProfileDetailViewState.SegmentTypes.CLUBS
                                            )
                                        )
                                    }
                                )

                            ProfileDetailViewState.SegmentTypes.EVENTS ->
                                EventsTab(
                                    events = store.state.uiModel?.events ?: emptyList(),
                                    onItemTapped = { id ->
                                        store.send(ProfileDetailAction.EventsItemTapped(id))
                                    },
                                    onCreate = {
                                        store.send(
                                            ProfileDetailAction.EmptyStateActionTapped(
                                                ProfileDetailViewState.SegmentTypes.EVENTS
                                            )
                                        )
                                    }
                                )

                            ProfileDetailViewState.SegmentTypes.HANGOUTS ->
                                HangoutsTab(
                                    hangouts = store.state.uiModel?.hangouts ?: emptyList(),
                                    onItemTapped = { id ->
                                        store.send(ProfileDetailAction.HangoutsItemTapped(id))
                                    },
                                    onCreate = {
                                        store.send(
                                            ProfileDetailAction.EmptyStateActionTapped(
                                                ProfileDetailViewState.SegmentTypes.HANGOUTS
                                            )
                                        )
                                    }
                                )
                        }
                    }
                }
            }

            item(key = "paging_footer") {
                PagingFooter(hasMore = selectedSegmentHasMore)
            }

            // Bottom spacing
            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        // Navigation overlay (sticky on top)
        ProfileNavigationOverlay(
            isSegmentSticky = isSegmentSticky,
            selectedSegment = store.state.selectedSegment,
            isOwnProfile = store.state.isOwnProfile,
            isPushed = store.state.isPushed,
            title = store.state.navigationTitle ?: stringResource(R.string.profile_title),
            onSettingsTapped = { store.send(ProfileDetailAction.SettingsTapped) },
            onBackTapped = { store.send(ProfileDetailAction.BackTapped) },
            onSegmentSelected = { segment ->
                store.send(ProfileDetailAction.SegmentTapped(segment))
            },
            onNavBarPositioned = { height -> navBarHeight = height },
            modifier = Modifier.zIndex(1f)
        )
    }
}

@Composable
private fun ProfileNavigationOverlay(
    isSegmentSticky: Boolean,
    selectedSegment: ProfileDetailViewState.SegmentTypes,
    isOwnProfile: Boolean,
    isPushed: Boolean,
    title: String,
    onSettingsTapped: () -> Unit,
    onBackTapped: () -> Unit,
    onSegmentSelected: (ProfileDetailViewState.SegmentTypes) -> Unit,
    onNavBarPositioned: (Dp) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Column(modifier = modifier.fillMaxWidth()) {
        // Top nav bar. Two layouts:
        //  - Root profile tab (!isPushed): big left-aligned title like the Groups/Clubs tabs.
        //    The Scaffold already insets content below the status bar, so NO statusBarsPadding here.
        //  - Pushed (from a members list): compact inline (centered) title with a back button,
        //    iOS .navigationBarTitleDisplayMode(.inline). Bottom bar is hidden, so the Scaffold
        //    gives zero top inset — this bar must add its own statusBarsPadding.
        Surface(
            color = Color.White,
            shadowElevation = 0.dp,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                onNavBarPositioned(with(density) { coordinates.size.height.toDp() })
            }
        ) {
            if (isPushed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 4.dp)
                        .heightIn(min = 44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Leading / trailing slots keep the centered title from shifting.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                            IconButton(onClick = onBackTapped) {
                                Icon(
                                    painter = Images.Icons.arrowLeft01(),
                                    contentDescription = stringResource(DesignR.string.common_back),
                                    tint = Palette.blackHigh
                                )
                            }
                        }

                        Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                            if (isOwnProfile) {
                                IconButton(onClick = onSettingsTapped) {
                                    Icon(
                                        painter = Images.Icons.gear(),
                                        contentDescription = stringResource(R.string.settings_title),
                                        tint = Palette.blackHigh
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = title,
                        style = AppTypography.TitleSm.bold,
                        color = Palette.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 48.dp)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = AppTypography.TitleL.extraBold,
                        color = Palette.black,
                        modifier = Modifier.weight(1f)
                    )

                    if (isOwnProfile) {
                        IconButton(onClick = onSettingsTapped) {
                            Icon(
                                painter = Images.Icons.gear(),
                                contentDescription = stringResource(R.string.settings_title),
                                tint = Palette.blackHigh
                            )
                        }
                    }
                }
            }
        }

        // Sticky segment
        AnimatedVisibility(
            visible = isSegmentSticky,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                CapsuleSegmentedPicker(
                    options = ProfileDetailViewState.SegmentTypes.values().toList(),
                    selectedOption = selectedSegment,
                    onOptionSelected = onSegmentSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactHeaderView(
    card: com.bonjur.profile.presentation.detail.models.UserCardModel,
    isOwnProfile: Boolean,
    onCardTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Long-press the avatar to see the photo full screen (mirrors iOS).
        ImagePreviewable(url = card.imageUrl) {
            com.bonjur.designSystem.components.cashedImage.CachedAsyncImage(
                url = card.imageUrl,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .border(3.dp, Palette.grayTeritary.copy(alpha = 0.3f), CircleShape)
                    .background(Palette.grayQuaternary),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                placeholder = {
                    Icon(
                        painter = Images.Icons.user(),
                        contentDescription = null,
                        tint = Palette.blackMedium,
                        modifier = Modifier
                            .size(88.dp)
                            .background(Palette.grayQuaternary, CircleShape)
                            .padding(22.dp)
                    )
                },
                error = {
                    Icon(
                        painter = Images.Icons.user(),
                        contentDescription = null,
                        tint = Palette.blackMedium,
                        modifier = Modifier
                            .size(88.dp)
                            .background(Palette.grayQuaternary, CircleShape)
                            .padding(22.dp)
                    )
                }
            )
        }

        Text(
            text = card.nameSurname,
            style = AppTypography.TitleMd.bold,
            color = Palette.black,
            modifier = Modifier.padding(top = 14.dp)
        )

        val subtitle = listOf(card.speciality, card.community)
            .filter { it.isNotEmpty() }
            .joinToString(" · ")
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = AppTypography.BodyTextSm.bold,
                color = Palette.blackMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        if (isOwnProfile) {
            // Tint from the selected card cover; fall back to the original green.
            // `Primary` is a pale green that is unreadable as text, so it reuses the
            // dark green pair like the null case. Mirrors iOS `ProfileDetailViewV2`.
            val cover = card.backgroundCover
            val chipForeground: Color
            val chipBackground: Color
            if (cover == null || cover is AppUIEntities.BackgroundType.Primary) {
                chipForeground = Palette.green900
                chipBackground = Palette.greenLight
            } else {
                chipForeground = cover.bgColor
                chipBackground = cover.bgColor.copy(alpha = 0.18f)
            }

            Surface(
                onClick = onCardTap,
                shape = CircleShape,
                color = chipBackground,
                border = BorderStroke(1.dp, chipForeground.copy(alpha = 0.5f)),
                modifier = Modifier.padding(top = 14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "🪪  User Card ID",
                        style = AppTypography.TextMd.bold,
                        color = chipForeground
                    )
                    Icon(
                        painter = Images.Icons.chevronRight(),
                        contentDescription = null,
                        tint = chipForeground,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserInfoView(
    uiModel: ProfileDetail.UIModel?,
    isOwnProfile: Boolean,
    onEditTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppInfoContainer(
        alignment = Alignment.Start,
        spacing = 16.dp,
        modifier = modifier
    ) {
        // Header row: "About" + edit button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profile_about),
                style = AppTypography.HeadingMd.medium,
                color = Palette.black
            )
            if (isOwnProfile) {
                IconButton(onClick = onEditTap) {
                    Icon(
                        painter = Images.Icons.penLine(),
                        contentDescription = "Edit",
                        tint = Palette.blackHigh
                    )
                }
            }
        }

        // About text
        Text(
            text = uiModel?.about ?: stringResource(R.string.profile_no_information),
            style = AppTypography.BodyTextSm.regular,
            color = Palette.blackHigh
        )

        // Tags
        if (uiModel?.tags?.isNotEmpty() == true) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiModel.tags.forEach { tag ->
                    Surface(
                        shape = CircleShape,
                        color = Palette.grayQuaternary
                    ) {
                        Text(
                            text = "#${tag.title.lowercase()}",
                            style = AppTypography.TextSm.regular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Gender / Birthday / Languages cells
        Column(verticalArrangement = Arrangement.spacedBy(21.dp)) {
            UserInfoCell(
                icon = Images.Icons.user(),
                title = stringResource(R.string.profile_gender),
                subtitle = uiModel?.gender ?: "-"
            )
            UserInfoCell(
                icon = Images.Icons.user(),
                title = stringResource(R.string.profile_birthday),
                subtitle = uiModel?.birthday ?: "-"
            )
            UserInfoCell(
                // iOS uses a globe here; gender/birthday still fall back to the
                // person icon because this module has no gender/cake drawable yet.
                icon = Images.Icons.globe(),
                title = stringResource(R.string.profile_languages),
                subtitle = uiModel?.languages?.joinToString(", ") { it.title } ?: "-"
            )
        }
    }
}

@Composable
private fun UserInfoCell(
    icon: Painter,
    title: String,
    subtitle: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Palette.blackMedium,
            modifier = Modifier
                .size(44.dp)
                .background(Palette.grayQuaternary, RoundedCornerShape(16.dp))
                .padding(10.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = AppTypography.TextMd.regular,
                    color = Palette.blackMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = subtitle,
                    style = AppTypography.BodyTextSm.regular,
                    color = Palette.black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Divider()
        }
    }
}

@Composable
private fun ClubsTab(
    clubs: List<ClubCardModel>,
    onItemTapped: (Int) -> Unit,
    onCreate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (clubs.isEmpty()) {
            EmptyStateView(type = ProfileDetailViewState.SegmentTypes.CLUBS, onCreate = onCreate)
        } else {
            clubs.forEach { club ->
                ClubCardView(
                    model = club,
                    onTap = { onItemTapped(club.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun EventsTab(
    events: List<EventsCardModel>,
    onItemTapped: (String) -> Unit,
    onCreate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (events.isEmpty()) {
            EmptyStateView(type = ProfileDetailViewState.SegmentTypes.EVENTS, onCreate = onCreate)
        } else {
            events.forEach { event ->
                EventsCardView(
                    model = event,
                    onButtonTap = { /* no-op in profile (matches iOS) */ },
                    onTap = { onItemTapped(event.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}



@Composable
private fun HangoutsTab(
    hangouts: List<HangoutsCardModel>,
    onItemTapped: (String) -> Unit,
    onCreate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (hangouts.isEmpty()) {
            EmptyStateView(type = ProfileDetailViewState.SegmentTypes.HANGOUTS, onCreate = onCreate)
        } else {
            hangouts.forEach { hangout ->
                HangoutsCardView(
                    model = hangout,
                    onButtonTap = { /* no-op in profile (matches iOS) */ },
                    onTap = { onItemTapped(hangout.id) }
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun EmptyStateView(
    type: ProfileDetailViewState.SegmentTypes,
    onCreate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppEmptyView(
            model = AppEmptyModel(
                icon = Images.Icons.twoUsers(),
                text = "You haven't joined any ${type.title.lowercase()} yet. Be the pioneer and start the very first one now!",
                buttonTitle = "Create a ${type.title.lowercase().removeSuffix("s")} +"
            ),
            onButtonClick = onCreate
        )
    }
}