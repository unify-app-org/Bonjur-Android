package com.bonjur.clubs.presentation.components

import CardBackgroundView
import android.R.attr.y
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.presentation.model.*
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.InfoContainer.AppInfoContainer
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.segmentView.CapsuleSegmentedPicker
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.events.presentation.components.EventsCardView
import com.bonjur.events.presentation.models.EventsCardModel
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun ClubDetailsView(
    store: FeatureStore<ClubDetailsViewState, ClubDetailsAction, ClubDetailsSideEffect>
) {

    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val pagerState = rememberPagerState(
        initialPage = store.state.selectedSegment.toIndex(),
        pageCount = { 3 }
    )
    val coroutineScope = rememberCoroutineScope()

    // State tracking
    var isScrolled by remember { mutableStateOf(false) }
    var isNameVisible by remember { mutableStateOf(true) }
    var isSegmentSticky by remember { mutableStateOf(false) }
    var navBarHeight by remember { mutableStateOf(0.dp) }
    var isUpdatingFromPager by remember { mutableStateOf(false) }

    // Listen to pager changes (from swipe gesture)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                if (!isUpdatingFromPager) {
                    val segment = ClubDetailsViewState.SegmentTypes.fromIndex(index = currentPage)
                    if (store.state.selectedSegment != segment) {
                        store.send(ClubDetailsAction.SegmentChanged(segment))
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

    // Scroll tracking
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            isScrolled = index > 0 || offset > 30
        }
    }

    Box(
        modifier = Modifier
            .background(Palette.white)
            .fillMaxSize()
    ) {
        // Main content
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            // Stretchable header
            item(key = "header") {
                StretchableHeader(
                    imageUrl = store.state.uiModel?.coverImage,
                    colorType = store.state.uiModel?.coverColorType,
                    scrollOffset = if (listState.firstVisibleItemIndex == 0) {
                        listState.firstVisibleItemScrollOffset
                    } else {
                        Int.MAX_VALUE
                    }
                )
            }

            // Logo view
            item(key = "logo") {
                LogoView(
                    logoUrl = store.state.uiModel?.logo,
                    onEditClick = { /* Handle edit */ },
                    onCameraClick = { /* Handle camera */ }
                )
            }

            // Club info
            item(key = "club_info") {
                ClubInfoView(
                    uiModel = store.state.uiModel,
                    onNamePositioned = { yPosition ->
                        val navBarBottom = with(density) { navBarHeight.toPx() }
                        isNameVisible = yPosition > navBarBottom
                    }
                )
            }

            // Segment picker
            item(key = "segment") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                        .onGloballyPositioned { coordinates ->
                            val yPosition = coordinates.positionInRoot().y
                            val navBarBottom = with(density) { navBarHeight.toPx() }
                            isSegmentSticky = yPosition <= navBarBottom
                        }
                ) {
                    if (!isSegmentSticky) {
                        SegmentPicker(
                            selectedSegment = store.state.selectedSegment,
                            onSegmentSelected = { segment ->
                                store.send(ClubDetailsAction.SegmentChanged(segment))
                            },
                            modifier = Modifier
                                .offset(y = (-26).dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }

            // Tab content with dynamic height
            item(key = "tabs") {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .offset(y = (-26).dp)
                        .fillMaxWidth()
                ) { page ->
                    val segment = ClubDetailsViewState.SegmentTypes.values()[page]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        when (segment) {
                            ClubDetailsViewState.SegmentTypes.ABOUT ->
                                InfoTab(store.state.uiModel?.infoData ?: emptyList())

                            ClubDetailsViewState.SegmentTypes.EVENTS ->
                                EventsTab(store.state.uiModel?.eventsData ?: emptyList())

                            ClubDetailsViewState.SegmentTypes.MEMBERS ->
                                MembersTab()
                        }
                    }
                }
            }

            // Bottom spacing for join button
            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Navigation overlay
        NavigationOverlay(
            isScrolled = isScrolled,
            isNameVisible = isNameVisible,
            isSegmentSticky = isSegmentSticky,
            clubName = store.state.uiModel?.name ?: "",
            selectedSegment = store.state.selectedSegment,
            onBackClick = { store.send(ClubDetailsAction.BackTapped) },
            onMoreClick = { /* Handle more */ },
            onCameraClick = { /* Handle camera */ },
            onSegmentSelected = { segment ->
                store.send(ClubDetailsAction.SegmentChanged(segment))
            },
            onNavBarPositioned = { height ->
                navBarHeight = height
            },
            modifier = Modifier.zIndex(1f)
        )

        // Join button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .padding(bottom = 16.dp)
                .zIndex(2f)
        ) {
            AppButton(
                title = "Join",
                model = AppButtonModel(
                    contentSize = ContentSize.Fill
                ),
                onClick = { /* Handle join */ }
            )
        }
    }
}


@Composable
private fun StretchableHeader(
    imageUrl: String?,
    colorType: AppUIEntities.BackgroundType?,
    scrollOffset: Int
) {
    val density = LocalDensity.current
    val baseHeight = 200.dp
    val pullDown = max(-scrollOffset, 0)
    val extraHeight = with(density) { pullDown.toDp() }
    val totalHeight = baseHeight + extraHeight
    val scale = 1f + (pullDown / 3500f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        CachedAsyncImage(
            url = imageUrl,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = {
                CardBackgroundView(
                    cardType = AppUIEntities.ActivityType.CLUBS,
                    bgColorType = colorType ?: AppUIEntities.BackgroundType.Primary,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 0.dp
                ) {}
            },
            error = {
                CardBackgroundView(
                    cardType = AppUIEntities.ActivityType.CLUBS,
                    bgColorType = colorType ?: AppUIEntities.BackgroundType.Primary,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 0.dp
                ) {}
            },
            content = { imageBitmap ->
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
        )
    }
}

@Composable
private fun LogoView(
    logoUrl: String?,
    onEditClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-44).dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(Palette.grayQuaternary, RoundedCornerShape(20.dp))
                    .border(
                        width = 3.dp,
                        color = Palette.grayTeritary.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                CachedAsyncImage(
                    url = logoUrl,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = {
                        Icon(
                            painter = Images.Icons.user(),
                            contentDescription = null,
                            tint = Palette.blackMedium,
                            modifier = Modifier.size(44.dp)
                        )
                    },
                    error = {
                        Icon(
                            painter = Images.Icons.user(),
                            contentDescription = null,
                            tint = Palette.blackMedium,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                )
            }

            // Camera button
            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = 4.dp)
                    .align(Alignment.BottomEnd)
                    .background(Palette.grayQuaternary, CircleShape)
                    .border(2.dp, Palette.whiteHigh, CircleShape)
                    .clickable(onClick = onCameraClick)
                    .padding(7.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = Images.Icons.camera(),
                    contentDescription = "Camera",
                    tint = Palette.blackMedium,
                    modifier = Modifier.size(18.dp)  // Exact 18x18 icon
                )
            }
        }

        IconButton(onClick = onEditClick) {
            Icon(
                painter = Images.Icons.penLine(),
                contentDescription = "Edit",
                tint = Palette.blackHigh
            )
        }
    }
}

@Composable
private fun ClubInfoView(
    uiModel: ClubsDetails.UIModel?,
    onNamePositioned: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .offset(y= (-26).dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Club name
        Text(
            text = uiModel?.name ?: "",
            style = AppTypography.TitleL.extraBold,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onNamePositioned(coordinates.positionInRoot().y)
                }
        )

        // Access type and community
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isPrivate = uiModel?.accessType == AppUIEntities.AccessType.PRIVATE
            Surface(
                shape = CircleShape,
                color = if (isPrivate) Palette.white else Palette.blackHigh,
                border = androidx.compose.foundation.BorderStroke(
                    0.5.dp,
                    Palette.blackHigh
                )
            ) {
                Text(
                    text = if (isPrivate) "Private" else "Public",
                    style = AppTypography.TextSm.medium,
                    color = if (isPrivate) Palette.blackHigh else Palette.whiteHigh,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Text(
                text = uiModel?.communityName ?: "",
                style = AppTypography.TextL.medium,
                color = Palette.appBlue,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { /* Handle community link */ }
                }
            )
        }

        // Member count
        Text(
            text = "${uiModel?.membersCount ?: 0} members",
            style = AppTypography.TextMd.regular,
            color = Palette.blackHigh
        )

        // Tags
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiModel?.tags?.forEach { item ->
                Surface(
                    shape = CircleShape,
                    color = Palette.grayQuaternary
                ) {
                    Text(
                        text = "#${item.title.lowercase()}",
                        style = AppTypography.TextSm.regular,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Create event button
        AppButton(
            title = "Create new event +",
            model = AppButtonModel(
                type = ButtonType.Secondary,
                contentSize = ContentSize.Fill,
                size = AppButtonSize.Medium
            ),
            onClick = { /* Handle create event */ }
        )
    }
}

@Composable
private fun NavigationOverlay(
    isScrolled: Boolean,
    isNameVisible: Boolean,
    isSegmentSticky: Boolean,
    clubName: String,
    selectedSegment: ClubDetailsViewState.SegmentTypes,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    onCameraClick: () -> Unit,
    onSegmentSelected: (ClubDetailsViewState.SegmentTypes) -> Unit,
    onNavBarPositioned: (Dp) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Column(modifier = modifier.fillMaxWidth()) {
        // Navigation bar
        Surface(
            color = if (isScrolled) Color.White else Color.Transparent,
            shadowElevation = if (isScrolled) 4.dp else 0.dp,
            modifier = Modifier.onGloballyPositioned { coordinates ->
                onNavBarPositioned(with(density) { coordinates.size.height.toDp() })
            }
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NavBarButton(
                        icon = Images.Icons.arrowLeft01(),
                        isScrolled = isScrolled,
                        onClick = onBackClick
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        NavBarButton(
                            icon = Images.Icons.ellipsis02(),
                            isScrolled = isScrolled,
                            onClick = onMoreClick
                        )

                        AnimatedVisibility(visible = !isScrolled) {
                            NavBarButton(
                                icon = Images.Icons.camera(),
                                isScrolled = isScrolled,
                                onClick = onCameraClick
                            )
                        }
                    }
                }

                // Title overlay - Show when name is NOT visible
                this@Column.AnimatedVisibility(
                    visible = !isNameVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 80.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = clubName,
                            style = AppTypography.HeadingXL.bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
            )  {
                SegmentPicker(
                    selectedSegment = selectedSegment,
                    onSegmentSelected = onSegmentSelected,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun NavBarButton(
    icon: androidx.compose.ui.graphics.painter.Painter,
    isScrolled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .background(
                color = if (isScrolled) Palette.grayQuaternary else Palette.whiteMedium,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Palette.blackHigh,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SegmentPicker(
    selectedSegment: ClubDetailsViewState.SegmentTypes,
    onSegmentSelected: (ClubDetailsViewState.SegmentTypes) -> Unit,
    modifier: Modifier = Modifier
) {
    CapsuleSegmentedPicker(
        options = ClubDetailsViewState.SegmentTypes.values().toList(),
        selectedOption = selectedSegment,
        onOptionSelected = onSegmentSelected,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun InfoTab(infoData: List<ClubsDetails.Info>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (infoData.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No information available",
                    style = AppTypography.TextL.medium,
                    color = Palette.blackMedium
                )
            }
        } else {
            infoData.forEach { item ->
                AppInfoContainer(spacing = 10.dp) {
                    Text(
                        text = item.title,
                        style = AppTypography.HeadingMd.medium,
                        color = Palette.blackHigh,
                        modifier = Modifier.fillMaxWidth()
                    )

                    item.subItems.forEach { subItem ->
                        InfoSubItem(subItem = subItem)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoSubItem(subItem: ClubsDetails.SubInfo) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        subItem.title?.let { title ->
            Text(
                text = title,
                style = AppTypography.TextMd.regular,
                color = Palette.blackMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            text = subItem.description,
            style = AppTypography.BodyTextSm.regular,
            color = if (subItem.isLink) Palette.appBlue else Palette.blackHigh,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EventsTab(events: List<EventsCardModel>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (events.isEmpty()) {
            AppEmptyView(
                model = AppEmptyModel(
                    icon = Images.Icons.twoUsers(),
                    text = "There are no events for this club yet. Be the pioneer and start the very first one now!",
                    buttonTitle = "Create an event +"
                ),
                onButtonClick = { /* Handle create event */ }
            )
        } else {
            events.forEach { event ->
                EventsCardView(
                    model = event,
                    onTap = { /* Handle tap */ },
                    onButtonTap = { /* Handle button tap */ }
                )
            }
        }
    }
}

@Composable
private fun MembersTab() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Members Tab - Coming Soon",
            style = AppTypography.TextL.medium,
            color = Palette.blackMedium
        )
    }
}
