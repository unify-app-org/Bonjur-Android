package com.bonjur.designSystem.components.tabView

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette
import kotlin.math.abs

@Composable
fun CustomPageIndicator(
    numberOfPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    maxVisibleDots: Int = 5,
    activeColor: Color = Palette.primary,
    inactiveColor: Color = Color.Gray.copy(alpha = 0.3f)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until numberOfPages) {
            if (shouldShowDot(index, currentPage, numberOfPages, maxVisibleDots)) {
                val isActive = index == currentPage
                val distance = abs(currentPage - index)
                
                val width by animateDpAsState(
                    targetValue = dotWidth(index, currentPage, numberOfPages),
                    animationSpec = tween(300),
                    label = "width"
                )
                
                val height by animateDpAsState(
                    targetValue = dotHeight(distance, numberOfPages),
                    animationSpec = tween(300),
                    label = "height"
                )
                
                val opacity by animateFloatAsState(
                    targetValue = dotOpacity(distance, numberOfPages),
                    animationSpec = tween(300),
                    label = "opacity"
                )
                
                val scale by animateFloatAsState(
                    targetValue = dotScale(distance, numberOfPages),
                    animationSpec = tween(300),
                    label = "scale"
                )
                
                Box(
                    modifier = Modifier
                        .width(width)
                        .height(height)
                        .scale(scale)
                        .alpha(opacity)
                        .background(
                            color = if (isActive) activeColor else inactiveColor,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

private fun shouldShowDot(
    index: Int,
    currentPage: Int,
    numberOfPages: Int,
    maxVisibleDots: Int
): Boolean {
    if (numberOfPages <= maxVisibleDots) {
        return true
    }
    val distance = abs(currentPage - index)
    return distance <= 2
}

private fun dotWidth(index: Int, currentPage: Int, numberOfPages: Int): Dp {
    return if (numberOfPages >= 5) {
        when (abs(currentPage - index)) {
            0 -> 32.dp
            1 -> 14.dp
            2 -> 12.dp
            else -> 7.dp
        }
    } else {
        if (index == currentPage) 32.dp else 14.dp
    }
}

private fun dotHeight(distance: Int, numberOfPages: Int): Dp {
    return if (numberOfPages >= 5) {
        when (distance) {
            0, 1 -> 8.dp
            2 -> 6.dp
            else -> 4.dp
        }
    } else {
        8.dp
    }
}

private fun dotOpacity(distance: Int, numberOfPages: Int): Float {
    return if (numberOfPages >= 5) {
        when (distance) {
            0 -> 1.0f
            1 -> 0.6f
            2 -> 0.3f
            else -> 0.2f
        }
    } else {
        1.0f
    }
}

private fun dotScale(distance: Int, numberOfPages: Int): Float {
    return if (numberOfPages >= 5) {
        when {
            distance <= 1 -> 1.0f
            distance == 2 -> 0.8f
            else -> 0.6f
        }
    } else {
        1.0f
    }
}