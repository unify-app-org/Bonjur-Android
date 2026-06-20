package com.bonjur.communities.presentation.detail.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import CardBackgroundView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.list.components.ClubCardView
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.components.MemberListView
import com.bonjur.member.components.MemberOptionsInput
import com.bonjur.member.components.MemberOptionsSheet
import com.bonjur.member.policy.MemberOptionsPolicy
import com.bonjur.communities.presentation.detail.model.CommunityDetailAction
import com.bonjur.communities.presentation.detail.model.CommunityDetailSideEffect
import com.bonjur.communities.presentation.detail.model.CommunityDetailViewState
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
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun CommunityDetailView(
    store: FeatureStore<CommunityDetailViewState, CommunityDetailAction, CommunityDetailSideEffect>
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val pagerState = rememberPagerState(
        initialPage = store.state.selectedSegment.toIndex(),
        pageCount = { 3 }
    )
    val coroutineScope = rememberCoroutineScope()

    var isScrolled by remember { mutableStateOf(false) }
    var isNameVisible by remember { mutableStateOf(true) }
    var isSegmentSticky by remember { mutableStateOf(false) }
    var navBarHeight by remember { mutableStateOf(0.dp) }
    var isUpdatingFromPager by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { currentPage ->
                if (!isUpdatingFromPager) {
                    val segment = CommunityDetailViewState.SegmentTypes.fromIndex(index = currentPage)
                    if (store.state.selectedSegment != segment) {
                        store.send(CommunityDetailAction.SegmentChanged(segment))
                    }
                }
            }
    }

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
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
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

            item(key = "logo") {
                LogoView(logoUrl = store.state.uiModel?.logo)
            }

            item(key = "community_info") {
                CommunityInfoView(
                    uiModel = store.state.uiModel,
                    canCreateEvent = store.state.canCreateEvent,
                    onNamePositioned = { yPosition ->
                        val navBarBottom = with(density) { navBarHeight.toPx() }
                        isNameVisible = yPosition > navBarBottom
                    }
                )
            }

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
                                store.send(CommunityDetailAction.SegmentChanged(segment))
                            },
                            modifier = Modifier.offset(y = (-26).dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }

            item(key = "tabs") {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .offset(y = (-26).dp)
                        .fillMaxWidth()
                ) { page ->
                    val segment = CommunityDetailViewState.SegmentTypes.entries[page]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        when (segment) {
                            CommunityDetailViewState.SegmentTypes.ABOUT ->
                                InfoTab(store.state.uiModel?.infoData ?: emptyList())

                            CommunityDetailViewState.SegmentTypes.CLUBS ->
                                ClubsTab(
                                    clubs = store.state.clubsData,
                                    onClubTapped = { id ->
                                        store.send(CommunityDetailAction.ClubItemTapped(id))
                                    }
                                )

                            CommunityDetailViewState.SegmentTypes.MEMBERS ->
                                MembersTab(store = store)
                        }
                    }
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }

        NavigationOverlay(
            isScrolled = isScrolled,
            isNameVisible = isNameVisible,
            isSegmentSticky = isSegmentSticky,
            isEditable = store.state.isEditable,
            communityName = store.state.uiModel?.name ?: "",
            selectedSegment = store.state.selectedSegment,
            onBackClick = { store.send(CommunityDetailAction.BackTapped) },
            onEditClick = { store.send(CommunityDetailAction.EditTapped) },
            onSegmentSelected = { segment ->
                store.send(CommunityDetailAction.SegmentChanged(segment))
            },
            onNavBarPositioned = { height ->
                navBarHeight = height
            },
            modifier = Modifier.zIndex(1f)
        )
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
private fun LogoView(logoUrl: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-44).dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(Palette.grayQuaternary, RoundedCornerShape(20.dp)),
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
    }
}

@Composable
private fun CommunityInfoView(
    uiModel: CommunityDetails.UIModel?,
    canCreateEvent: Boolean,
    onNamePositioned: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .offset(y = (-26).dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = uiModel?.name ?: "",
            style = AppTypography.TitleL.extraBold,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onNamePositioned(coordinates.positionInRoot().y)
                }
        )

        Text(
            text = "${uiModel?.membersCount ?: 0} members",
            style = AppTypography.TextMd.regular,
            color = Palette.blackHigh
        )

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

        if (canCreateEvent) {
            AppButton(
                title = "Create new event +",
                model = AppButtonModel(
                    type = ButtonType.Secondary,
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Medium
                ),
                onClick = { }
            )
        }
    }
}

@Composable
private fun NavigationOverlay(
    isScrolled: Boolean,
    isNameVisible: Boolean,
    isSegmentSticky: Boolean,
    isEditable: Boolean,
    communityName: String,
    selectedSegment: CommunityDetailViewState.SegmentTypes,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onSegmentSelected: (CommunityDetailViewState.SegmentTypes) -> Unit,
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NavBarButton(
                        icon = Images.Icons.arrowLeft01(),
                        isScrolled = isScrolled,
                        onClick = onBackClick
                    )

                    if (isEditable) {
                        NavBarButton(
                            icon = Images.Icons.penLine(),
                            isScrolled = isScrolled,
                            onClick = onEditClick
                        )
                    }
                }

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
                            text = communityName,
                            style = AppTypography.HeadingXL.bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isSegmentSticky,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Surface(
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentPicker(
                    selectedSegment = selectedSegment,
                    onSegmentSelected = onSegmentSelected,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
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
    selectedSegment: CommunityDetailViewState.SegmentTypes,
    onSegmentSelected: (CommunityDetailViewState.SegmentTypes) -> Unit,
    modifier: Modifier = Modifier
) {
    CapsuleSegmentedPicker(
        options = CommunityDetailViewState.SegmentTypes.entries,
        selectedOption = selectedSegment,
        onOptionSelected = onSegmentSelected,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun InfoTab(infoData: List<CommunityDetails.Info>) {
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
private fun InfoSubItem(subItem: CommunityDetails.SubInfo) {
    val context = LocalContext.current
    val isActionable = subItem.isLink || subItem.phoneNumber != null

    val onTap: () -> Unit = {
        val phone = subItem.phoneNumber
        when {
            phone != null -> {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("contact", phone))
                AppSnackBar.show(title = "Copied to clipboard", style = AppSnackBar.Style.SUCCESS)
            }
            subItem.isLink -> runCatching {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(subItem.description)))
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .let { if (isActionable) it.clickable(onClick = onTap) else it }
    ) {
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
            color = if (isActionable) Palette.appBlue else Palette.blackHigh,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MembersTab(
    store: FeatureStore<CommunityDetailViewState, CommunityDetailAction, CommunityDetailSideEffect>
) {
    val sections = store.state.membersData?.sections ?: emptyList()
    val viewerRole = store.state.uiModel?.userActivity
        ?: AppUIEntities.UserActivityRole.NOT_JOINED
    val totalCount = store.state.uiModel?.membersCount
        ?: sections.sumOf { it.memberCount }
    val currentUserId = store.state.currentUserId

    var optionsMember by remember { mutableStateOf<MemberCellModel?>(null) }

    MemberListView(
        sections = sections,
        onRowTap = { member -> store.send(CommunityDetailAction.UserTapped(member.id)) },
        onOptionsTap = { member -> optionsMember = member },
        currentUserId = currentUserId,
        previewLimit = 5,
        totalCount = totalCount,
        onSeeAll = { store.send(CommunityDetailAction.SeeAllMembersTapped) }
    )

    optionsMember?.let { member ->
        val isSelf = member.id == currentUserId
        MemberOptionsSheet(
            input = MemberOptionsInput(
                memberName = member.name,
                currentRole = member.role,
                assignableRoles = MemberOptionsPolicy.assignableRoles(viewerRole),
                showChangeRole = MemberOptionsPolicy.canChangeRole(
                    viewer = viewerRole,
                    activity = AppUIEntities.ActivityType.COMMUNITY,
                    isSelf = isSelf
                ),
                showReport = MemberOptionsPolicy.canReport(isSelf),
                onAssignRole = { role ->
                    store.send(CommunityDetailAction.AssignRole(member.id, role))
                },
                onReport = {
                    AppSnackBar.show(title = "Report submitted", style = AppSnackBar.Style.SUCCESS)
                }
            ),
            onDismiss = { optionsMember = null }
        )
    }
}

@Composable
private fun ClubsTab(
    clubs: List<ClubCardModel>,
    onClubTapped: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (clubs.isNotEmpty()) {
            clubs.forEach { club ->
                ClubCardView(
                    model = club,
                    onTap = { onClubTapped(club.id) }
                )
            }
        } else {
            AppEmptyView(
                model = AppEmptyModel(
                    icon = Images.Icons.twoUsers(),
                    text = "There are no clubs for this community yet. Be the pioneer and start the very first one now!",
                    buttonTitle = "Create a club +"
                ),
                onButtonClick = { },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
