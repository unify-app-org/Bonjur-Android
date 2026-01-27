package com.bonjur.app.tabBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.discover.navigation.DiscoverScreens
import com.bonjur.discover.presentation.DiscoverScreen

@Composable
fun AppTabBar() {
    val items = listOf(
        TabItem.Discover,
        TabItem.Clubs,
        TabItem.MyPlans,
        TabItem.Profile
    )

    var selectedTab: TabItem by remember { mutableStateOf(TabItem.Discover) }
    var isMenuOpen by remember { mutableStateOf(false) }
    var plusButtonPosition by remember { mutableStateOf(Offset.Zero) }
    var plusButtonSize by remember { mutableStateOf(44.dp) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.White,
            bottomBar = {
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabBarItem(
                        item = items[0],
                        selected = selectedTab == items[0],
                        onClick = { selectedTab = items[0] },
                        modifier = Modifier.weight(1f)
                    )

                    TabBarItem(
                        item = items[1],
                        selected = selectedTab == items[1],
                        onClick = { selectedTab = items[1] },
                        modifier = Modifier.weight(1f)
                    )

                    FloatingPlusButton(
                        isMenuOpen = isMenuOpen,
                        onClick = {
                            isMenuOpen = !isMenuOpen
                        },
                        onPositionChanged = { position, size ->
                            plusButtonPosition = position
                            plusButtonSize = size
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )

                    TabBarItem(
                        item = items[2],
                        selected = selectedTab == items[2],
                        onClick = { selectedTab = items[2] },
                        modifier = Modifier.weight(1f)
                    )

                    TabBarItem(
                        item = items[3],
                        selected = selectedTab == items[3],
                        onClick = { selectedTab = items[3] },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                when (selectedTab) {
                    TabItem.Discover -> DiscoverScreen()
                    TabItem.Clubs -> Text("Clubs Tab", modifier = Modifier.padding(16.dp))
                    TabItem.MyPlans -> Text("My Plans Tab", modifier = Modifier.padding(16.dp))
                    TabItem.Profile -> Text("Profile Tab", modifier = Modifier.padding(16.dp))
                }
            }
        }

        DimOverlay(
            visible = isMenuOpen,
            highlightPosition = plusButtonPosition,
            highlightSize = plusButtonSize,
            onDismiss = { isMenuOpen = false }
        )

        CreateMenu(
            visible = isMenuOpen,
            onCreateTypeSelected = { type ->
                isMenuOpen = false
            }
        )
    }
}

@Composable
fun TabBarItem(
    item: TabItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = item.icon(),
            contentDescription = item.label,
            tint = if (selected) Palette.blackHigh else Palette.graySecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.label,
            color = if (selected) Palette.blackHigh else Palette.graySecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
fun FloatingPlusButton(
    isMenuOpen: Boolean,
    onClick: () -> Unit,
    onPositionChanged: (Offset, androidx.compose.ui.unit.Dp) -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isMenuOpen) 45f else 0f,
        animationSpec = tween(300),
        label = "rotation"
    )

    val backgroundColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isMenuOpen) Color.White else Color.Black,
        animationSpec = tween(300),
        label = "backgroundColor"
    )

    val iconColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isMenuOpen) Color.Black else Color.White,
        animationSpec = tween(300),
        label = "iconColor"
    )

    Box(
        modifier = modifier
            .size(44.dp)
            .zIndex(100f)
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow()
                onPositionChanged(position, 44.dp)
            }
            .clip(RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_input_add),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(28.dp)
                .rotate(rotation)
        )
    }
}

@Composable
fun DimOverlay(
    visible: Boolean,
    highlightPosition: Offset,
    highlightSize: androidx.compose.ui.unit.Dp,
    onDismiss: () -> Unit
) {
    if (!visible) return

    val density = LocalDensity.current
    val highlightSizePx = with(density) { highlightSize.toPx() }
    val highlightRadius = highlightSizePx / 2

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(50f)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val position = event.changes.first().position

                        val centerX = highlightPosition.x + highlightRadius
                        val centerY = highlightPosition.y + highlightRadius

                        val dx = position.x - centerX
                        val dy = position.y - centerY
                        val distance = kotlin.math.sqrt(dx * dx + dy * dy)

                        if (distance > highlightRadius) {
                            event.changes.forEach { it.consume() }
                            onDismiss()
                            break
                        }
                    }
                }
            }
    ) {
        val highlightRect = Rect(
            left = highlightPosition.x,
            top = highlightPosition.y,
            right = highlightPosition.x + highlightSizePx,
            bottom = highlightPosition.y + highlightSizePx
        )

        val cutoutPath = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    rect = highlightRect,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        x = highlightRadius,
                        y = highlightRadius
                    )
                )
            )
        }

        clipPath(cutoutPath, clipOp = androidx.compose.ui.graphics.ClipOp.Difference) {
            drawRect(color = Color.Black.copy(alpha = 0.4f))
        }
    }
}

@Composable
fun CreateMenu(
    visible: Boolean,
    onCreateTypeSelected: (CreateType) -> Unit
) {
    val menuItems = listOf(
        CreateMenuItem("Club", Images.Icons.twoUsers(), CreateType.CLUB),
        CreateMenuItem("Events", Images.Icons.calendar(), CreateType.EVENT),
        CreateMenuItem("Hangouts", Images.Icons.chat(), CreateType.HANGOUT)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(60f),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 75.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(vertical = 24.dp)
                    .padding(start = 24.dp)
            ) {
                Text(
                    text = "Create",
                    style = AppTypography.HeadingXL.bold,
                    color = Palette.blackHigh,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                menuItems.forEachIndexed { index, item ->
                    CreateMenuItem(
                        item = item,
                        onClick = { onCreateTypeSelected(item.type) }
                    )
                    if (index < menuItems.size - 1) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateMenuItem(
    item: CreateMenuItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Palette.grayQuaternary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = item.icon,
                    contentDescription = item.title,
                    tint = Palette.blackHigh,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier
                .width(16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = AppTypography.BodyTextSm.regular,
                    color = Palette.blackHigh,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    color = Palette.graySecondary.copy(alpha = 0.3f),
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppTabBarPreview() {
    AppTabBar()
}