//
//  DiscoverSkeleton.kt
//  Discover
//
//  Created by Huseyn Hasanov on 04.09.26
//

package com.bonjur.discover.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.shimmer.ShimmerBox
import com.bonjur.designSystem.components.shimmer.rememberShimmerBrush

/**
 * Skeleton stand-in for the Discover content while the first load (or a filter
 * change) is in flight. It traces the real layout — filter chips, the community
 * pager, then the three card rows — so the screen keeps its shape and the content
 * lands in place instead of the whole dashboard sitting behind a blocking spinner.
 *
 * Card sizes are eyeballed against the real cards (which are content-sized, so
 * there is nothing exact to copy); they only have to read as the same rhythm.
 *
 * Mirrors iOS `DiscoverSkeletonView`.
 */
@Composable
fun DiscoverSkeleton(
    screenWidth: Dp,
    /** False once the real `FilterView` has chips of its own to draw. */
    showFilterChips: Boolean,
    modifier: Modifier = Modifier
) {
    // One brush for the whole screen: every block sweeps on the same clock.
    val brush = rememberShimmerBrush()

    Column(modifier = modifier.fillMaxWidth()) {
        if (showFilterChips) FilterChipsSkeleton(brush)

        // Community pager card + its page dots.
        ShimmerBox(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(COMMUNITY_CARD_HEIGHT),
            shape = RoundedCornerShape(20.dp),
            brush = brush
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
        ) {
            repeat(3) {
                ShimmerBox(
                    modifier = Modifier.size(width = 20.dp, height = 6.dp),
                    shape = CircleShape,
                    brush = brush
                )
            }
        }

        SectionSkeleton(brush, cardWidth = screenWidth - 60.dp, cardHeight = CLUB_CARD_HEIGHT)
        SectionSkeleton(brush, cardWidth = screenWidth - 90.dp, cardHeight = ACTIVITY_CARD_HEIGHT)
        SectionSkeleton(brush, cardWidth = screenWidth - 90.dp, cardHeight = ACTIVITY_CARD_HEIGHT)

        Spacer(Modifier.height(24.dp))
    }
}

/** Stands in for `FilterView`, which draws nothing until the categories arrive. */
@Composable
private fun FilterChipsSkeleton(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(72.dp, 96.dp, 84.dp, 110.dp).forEach { width ->
            ShimmerBox(
                modifier = Modifier
                    .width(width)
                    .height(34.dp),
                shape = RoundedCornerShape(100.dp),
                brush = brush
            )
        }
    }
}

/** Section header (title + "view all") over a row of cards. */
@Composable
private fun SectionSkeleton(
    brush: Brush,
    cardWidth: Dp,
    cardHeight: Dp
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBox(
                modifier = Modifier.size(width = 132.dp, height = 22.dp),
                brush = brush
            )
            ShimmerBox(
                modifier = Modifier.size(width = 64.dp, height = 16.dp),
                brush = brush
            )
        }

        // Not a LazyRow: the skeleton is a fixed handful of cards and must not
        // recycle or trigger the real list's load-more plumbing.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState(), enabled = false)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(2) {
                ShimmerBox(
                    modifier = Modifier
                        .width(cardWidth)
                        .height(cardHeight),
                    shape = RoundedCornerShape(20.dp),
                    brush = brush
                )
            }
        }
    }
}

private val COMMUNITY_CARD_HEIGHT = 184.dp
private val CLUB_CARD_HEIGHT = 168.dp
private val ACTIVITY_CARD_HEIGHT = 208.dp
