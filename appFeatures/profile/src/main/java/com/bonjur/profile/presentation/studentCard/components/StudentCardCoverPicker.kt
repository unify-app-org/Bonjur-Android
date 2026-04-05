package com.bonjur.profile.presentation.studentCard.components

import CardBackgroundView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.profile.presentation.studentCard.models.StudentCardViewState
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun StudentCardCoverPicker(
    selected: AppUIEntities.BackgroundType?,
    onCoverSelected: (AppUIEntities.BackgroundType?) -> Unit
) {
    val covers = StudentCardViewState.availableCovers
    val listState = rememberLazyListState()
    var hasCompletedInitialPositioning by remember { mutableStateOf(false) }
    var isTapSelectionInProgress by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val initialIndex = covers.indexOfFirst { compareCovers(it, selected) }
            .coerceAtLeast(0)
        listState.scrollToItem(initialIndex)
        hasCompletedInitialPositioning = true
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.isScrollInProgress to listState.layoutInfo
        }.collect { (isScrolling, layoutInfo) ->
            if (!hasCompletedInitialPositioning || isTapSelectionInProgress) return@collect
            if (isScrolling) return@collect

            val viewportCenter =
                (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
            val closestItem = layoutInfo.visibleItemsInfo.minByOrNull { item ->
                abs((item.offset + item.size / 2) - viewportCenter)
            }

            closestItem?.let { item ->
                if (item.index in covers.indices) {
                    val cover = covers[item.index]
                    if (!compareCovers(selected, cover)) {
                        onCoverSelected(cover)
                    }
                }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val screenWidth = maxWidth
        val itemWidth = screenWidth / 2.8f
        val horizontalPadding = screenWidth / 2 - 70.dp

        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
        ) {
            itemsIndexed(covers) { index, cover ->
                CoverItemView(
                    cover = cover,
                    isSelected = compareCovers(cover, selected),
                    width = itemWidth,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isTapSelectionInProgress = true
                        onCoverSelected(cover)
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                            isTapSelectionInProgress = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CoverItemView(
    cover: AppUIEntities.BackgroundType?,
    isSelected: Boolean,
    width: Dp,
    modifier: Modifier = Modifier
) {
    val cardHeight = width * 0.6f

    if (cover != null) {
        Box(
            modifier = modifier
                .width(width)
                .height(cardHeight + 30.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = if (isSelected) 2.5.dp else 0.dp,
                        color = if (isSelected) Palette.primary else Color.Transparent,
                        shape = RoundedCornerShape(13.dp)
                    )
                    .padding(5.dp)
            ) {
                CardBackgroundView(
                    bgColorType = cover,
                    cardType = AppUIEntities.ActivityType.CLUBS,
                    cornerRadius = 12.dp,
                    modifier = Modifier
                        .width(width - 10.dp)
                        .height(cardHeight)
                ) {}
            }
        }
    } else {
        Box(
            modifier = modifier
                .width(width)
                .height(cardHeight)
                .padding(5.dp)
                .border(
                    width = if (isSelected) 2.5.dp else 0.5.dp,
                    color = if (isSelected) Palette.primary else Palette.graySecondary,
                    shape = RoundedCornerShape(13.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Default",
                style = AppTypography.BodyTextMd.regular,
                color = if (isSelected) Palette.primary else Palette.graySecondary
            )
        }
    }
}

private fun compareCovers(
    first: AppUIEntities.BackgroundType?,
    second: AppUIEntities.BackgroundType?
): Boolean {
    if (first == null && second == null) return true
    if (first == null || second == null) return false
    return first.bgColor == second.bgColor
}
