package com.bonjur.hangouts.presentation.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.InfoContainer.AppInfoContainer
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.segmentView.CapsuleSegmentedPicker
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.hangouts.domain.model.HangoutDetails
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsAction
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsSideEffect
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsViewState
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun HangoutDetailsView(
    store: FeatureStore<HangoutDetailsViewState, HangoutDetailsAction, HangoutDetailsSideEffect>
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = store.state.selectedSegment.toIndex(),
        pageCount = { HangoutDetailsViewState.SegmentTypes.entries.size }
    )

    var isScrolled by remember { mutableStateOf(false) }
    var isNameVisible by remember { mutableStateOf(true) }
    var isSegmentSticky by remember { mutableStateOf(false) }
    var navBarHeight by remember { mutableStateOf(0.dp) }
    var isUpdatingFromPager by remember { mutableStateOf(false) }

    // Pager → store (swipe)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (!isUpdatingFromPager) {
                val segment = HangoutDetailsViewState.SegmentTypes.fromIndex(page)
                if (store.state.selectedSegment != segment) {
                    store.send(HangoutDetailsAction.SegmentChanged(segment))
                }
            }
        }
    }

    // Store → pager (picker tap)
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
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                isScrolled = index > 0 || offset > 30
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.white)
    ) {
        // Main scrollable content + join button
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                item(key = "header") {
                    StretchableHeader(
                        scrollOffset = if (listState.firstVisibleItemIndex == 0)
                            listState.firstVisibleItemScrollOffset else Int.MAX_VALUE,
                        baseHeight = navBarHeight
                    )
                }

                // Hangout info
                item(key = "hangout_info") {
                    HangoutInfoView(
                        uiModel = store.state.uiModel,
                        onNamePositioned = { yPos ->
                            val navBarPx = with(density) { navBarHeight.toPx() }
                            isNameVisible = yPos > navBarPx
                        },
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
                            .padding(top = 16.dp)
                            .onGloballyPositioned { coordinates ->
                                val yPos = coordinates.positionInRoot().y
                                val navBarPx = with(density) { navBarHeight.toPx() }
                                isSegmentSticky = yPos <= navBarPx
                            }
                    ) {
                        if (!isSegmentSticky) {
                            HangoutSegmentPicker(
                                selectedSegment = store.state.selectedSegment,
                                onSegmentSelected = { segment ->
                                    store.send(HangoutDetailsAction.SegmentChanged(segment))
                                }
                            )
                        } else {
                            Spacer(modifier = Modifier.height(48.dp))
                        }
                    }
                }

                // Pager tabs
                item(key = "tabs") {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val segment = HangoutDetailsViewState.SegmentTypes.fromIndex(page)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            when (segment) {
                                HangoutDetailsViewState.SegmentTypes.ABOUT ->
                                    InfoTab(infoData = store.state.uiModel?.infoData ?: emptyList())

                                HangoutDetailsViewState.SegmentTypes.MEMBERS ->
                                    MembersTab(membersData = store.state.uiModel?.membersData)
                            }
                        }
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Join button pinned at bottom (mirrors Swift's VStack bottom AppButton)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                AppButton(
                    title = "Join",
                    model = AppButtonModel(contentSize = ContentSize.Fill),
                    onClick = { /* Handle join */ }
                )
            }
        }

        // Navigation overlay
        HangoutNavigationOverlay(
            isScrolled = isScrolled,
            isNameVisible = isNameVisible,
            isSegmentSticky = isSegmentSticky,
            hangoutName = store.state.uiModel?.name ?: "",
            selectedSegment = store.state.selectedSegment,
            onBackClick = { store.send(HangoutDetailsAction.BackTapped) },
            onMoreClick = { /* Handle more */ },
            onEditClick = { /* Handle edit */ },
            onSegmentSelected = { segment ->
                store.send(HangoutDetailsAction.SegmentChanged(segment))
            },
            onNavBarPositioned = { height -> navBarHeight = height },
            modifier = Modifier.zIndex(1f)
        )
    }
}

// ---------------------------------------------------------------------------
// Stretchable Header
// ---------------------------------------------------------------------------

@Composable
private fun StretchableHeader(
    scrollOffset: Int,
    baseHeight: Dp
) {
    val density = LocalDensity.current
    val baseHeight = baseHeight
    val pullDown = max(-scrollOffset, 0)
    val extraHeight = with(density) { pullDown.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(baseHeight + extraHeight)
            .background(Color.White)
    )
}

// ---------------------------------------------------------------------------
// Navigation Overlay
// ---------------------------------------------------------------------------

@Composable
private fun HangoutNavigationOverlay(
    isScrolled: Boolean,
    isNameVisible: Boolean,
    isSegmentSticky: Boolean,
    hangoutName: String,
    selectedSegment: HangoutDetailsViewState.SegmentTypes,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    onEditClick: () -> Unit,
    onSegmentSelected: (HangoutDetailsViewState.SegmentTypes) -> Unit,
    onNavBarPositioned: (Dp) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Column(modifier = modifier.fillMaxWidth()) {
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button
                    NavBarButton(
                        icon = Images.Icons.arrowLeft01(),
                        onClick = onBackClick
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // More button (always visible)
                        NavBarButton(
                            icon = Images.Icons.ellipsis02(),
                            onClick = onMoreClick
                        )
                        AnimatedVisibility(visible = !isScrolled) {
                            NavBarButton(
                                icon = Images.Icons.penLine(),
                                onClick = onEditClick
                            )
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = !isNameVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 80.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hangoutName,
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
            ) {
                HangoutSegmentPicker(
                    selectedSegment = selectedSegment,
                    onSegmentSelected = onSegmentSelected,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun NavBarButton(
    icon: Painter,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Palette.blackHigh
        )
    }
}

@Composable
private fun HangoutSegmentPicker(
    selectedSegment: HangoutDetailsViewState.SegmentTypes,
    onSegmentSelected: (HangoutDetailsViewState.SegmentTypes) -> Unit,
    modifier: Modifier = Modifier
) {
    CapsuleSegmentedPicker(
        options = HangoutDetailsViewState.SegmentTypes.entries,
        selectedOption = selectedSegment,
        onOptionSelected = onSegmentSelected,
        modifier = modifier.fillMaxWidth()
    )
}

// ---------------------------------------------------------------------------
// Hangout Info
// ---------------------------------------------------------------------------

@Composable
private fun HangoutInfoView(
    uiModel: HangoutDetails.UIModel?,
    onNamePositioned: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Name
        Text(
            text = uiModel?.name ?: "",
            style = AppTypography.TitleL.extraBold,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onNamePositioned(coordinates.boundsInWindow().top)
                }
        )

        // Access type + community link
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isPrivate = uiModel?.accessType == AppUIEntities.AccessType.PRIVATE
            Surface(
                shape = CircleShape,
                color = if (isPrivate) Palette.white else Palette.blackHigh,
                border = BorderStroke(0.5.dp, Palette.blackHigh)
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
                color = Palette.appBlue
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
            uiModel?.tags?.forEach { tag ->
                Surface(shape = CircleShape, color = Palette.grayQuaternary) {
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
}

// ---------------------------------------------------------------------------
// Tab Contents
// ---------------------------------------------------------------------------

@Composable
private fun InfoTab(infoData: List<HangoutDetails.Info>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (infoData.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No information available",
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
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun InfoSubItem(subItem: HangoutDetails.SubInfo) {
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
private fun MembersTab(membersData: Any?) {
    Box(
        modifier = Modifier.fillMaxWidth().height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Members Tab - Coming Soon",
            style = AppTypography.TextL.medium,
            color = Palette.blackMedium
        )
    }
}