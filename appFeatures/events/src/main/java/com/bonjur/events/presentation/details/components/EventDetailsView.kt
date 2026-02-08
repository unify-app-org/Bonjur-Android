package com.bonjur.events.presentation.details.components

import CardBackgroundView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
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
import com.bonjur.events.domain.models.EventsDetails
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.InfoContainer.AppInfoContainer
import com.bonjur.designSystem.components.attachments.AttachmentItemModel
import com.bonjur.designSystem.components.attachments.AttachmentItemView
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
import com.bonjur.events.presentation.details.model.EventDetailsAction
import com.bonjur.events.presentation.details.model.EventDetailsSideEffect
import com.bonjur.events.presentation.details.model.EventDetailsViewState
import kotlinx.coroutines.launch
import kotlin.collections.isNotEmpty
import kotlin.math.max

@Composable
fun EventDetailsView(
    store: FeatureStore<EventDetailsViewState, EventDetailsAction, EventDetailsSideEffect>
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val pagerState = rememberPagerState(
        initialPage = store.state.selectedSegment.toIndex(),
        pageCount = { 2 }
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
                    val segment = EventDetailsViewState.SegmentTypes.fromIndex(index = currentPage)
                    if (store.state.selectedSegment != segment) {
                        store.send(EventDetailsAction.SegmentChanged(segment))
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
            modifier = Modifier.fillMaxSize()
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

            // Event info
            item(key = "event_info") {
                EventInfoView(
                    uiModel = store.state.uiModel,
                    isFileUploadReachedMaxLimit = store.state.isFileUploadReachedMaxLimit,
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
                        .padding(top = 16.dp)
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
                                store.send(EventDetailsAction.SegmentChanged(segment))
                            }
                        )
                    } else {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }

            // Tab content
            item(key = "tabs") {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val segment = EventDetailsViewState.SegmentTypes.values()[page]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        when (segment) {
                            EventDetailsViewState.SegmentTypes.ABOUT ->
                                InfoTab(store.state.uiModel?.infoData ?: emptyList())

                            EventDetailsViewState.SegmentTypes.MEMBERS ->
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
            eventName = store.state.uiModel?.name ?: "",
            selectedSegment = store.state.selectedSegment,
            onBackClick = { store.send(EventDetailsAction.BackTapped) },
            onMoreClick = { /* Handle more */ },
            onCameraClick = { /* Handle camera */ },
            onSegmentSelected = { segment ->
                store.send(EventDetailsAction.SegmentChanged(segment))
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
    val baseHeight = 164.dp
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
                    cardType = AppUIEntities.ActivityType.EVENTS,
                    bgColorType = colorType ?: AppUIEntities.BackgroundType.Primary,
                    modifier = Modifier.fillMaxSize(),
                    cornerRadius = 0.dp
                ) {}
            },
            error = {
                CardBackgroundView(
                    cardType = AppUIEntities.ActivityType.EVENTS,
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
private fun EventInfoView(
    uiModel: EventsDetails.UIModel?,
    isFileUploadReachedMaxLimit: Boolean,
    onNamePositioned: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Event name with edit button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiModel?.name ?: "",
                style = AppTypography.TitleL.extraBold,
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        onNamePositioned(coordinates.positionInRoot().y)
                    }
            )

            IconButton(onClick = { /* Handle edit */ }) {
                Icon(
                    painter = Images.Icons.penLine(),
                    contentDescription = "Edit",
                    tint = Palette.blackHigh
                )
            }
        }

        // Access type and community
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isPrivate = uiModel?.accessType == AppUIEntities.AccessType.PRIVATE
            Surface(
                shape = CircleShape,
                color = if (isPrivate) Palette.white else Palette.blackHigh,
                border = BorderStroke(
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

        // Tags and reminder button
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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

            // Reminder button
            AppButton(
                title = "Reminder",
                model = AppButtonModel(
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Medium
                ),
                onClick = { /* Handle reminder */ }
            )
        }

        // Attachments
        AttachmentsView(
            attachments = uiModel?.attachments ?: emptyList(),
            isFileUploadReachedMaxLimit = isFileUploadReachedMaxLimit
        )
    }
}

@Composable
private fun AttachmentsView(
    attachments: List<AttachmentItemModel>,
    isFileUploadReachedMaxLimit: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Attachments",
                style = AppTypography.HeadingXL.medium,
                color = Palette.black
            )

            if (attachments.isNotEmpty()) {
                Text(
                    text = "You can upload files up to 15 MB total for this event.",
                    style = AppTypography.BodyTextSm.regular,
                    color = Palette.blackMedium
                )
            }
        }

        if (attachments.isNotEmpty()) {
            // Attachment items
            attachments.forEach { attachment ->
                AttachmentItemView(
                    model = AttachmentItemModel(
                        id = attachment.id,
                        name = attachment.name,
                        size = attachment.size,
                        type = attachment.type,
                        canEdit = true
                    ),
                    onDeleteClick = { /* Handle delete attachment with id: ${attachment.id} */ }
                )
            }

            // Add button (if not reached limit)
            if (!isFileUploadReachedMaxLimit) {
                AppButton(
                    title = "Add +",
                    model = AppButtonModel(
                        type = ButtonType.Secondary,
                        contentSize = ContentSize.Fill,
                        size = AppButtonSize.Small
                    ),
                    onClick = { /* Handle add attachment */ }
                )
            }
        } else {
            // Empty state
            AppEmptyView(
                model = AppEmptyModel(
                    icon = null,
                    text = "You can upload files up to 15 MB total for this event.",
                    buttonTitle = "Add +"
                ),
                onButtonClick = { /* Handle add attachment */ }
            )
        }
    }
}

@Composable
private fun NavigationOverlay(
    isScrolled: Boolean,
    isNameVisible: Boolean,
    isSegmentSticky: Boolean,
    eventName: String,
    selectedSegment: EventDetailsViewState.SegmentTypes,
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
    onCameraClick: () -> Unit,
    onSegmentSelected: (EventDetailsViewState.SegmentTypes) -> Unit,
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

                // Title overlay
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
                            text = eventName,
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
                SegmentPicker(
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
    selectedSegment: EventDetailsViewState.SegmentTypes,
    onSegmentSelected: (EventDetailsViewState.SegmentTypes) -> Unit,
    modifier: Modifier = Modifier
) {
    CapsuleSegmentedPicker(
        options = EventDetailsViewState.SegmentTypes.values().toList(),
        selectedOption = selectedSegment,
        onOptionSelected = onSegmentSelected,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun InfoTab(infoData: List<EventsDetails.Info>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
private fun InfoSubItem(subItem: EventsDetails.SubInfo) {
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