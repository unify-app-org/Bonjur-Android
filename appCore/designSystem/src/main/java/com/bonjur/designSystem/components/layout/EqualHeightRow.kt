package com.bonjur.designSystem.components.layout

import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

/**
 * Shared "tallest item so far" for one horizontal card row.
 *
 * A `LazyRow` sizes every item independently, so a club card with a category chip
 * ends up taller than one without and the row's bottom edge zig-zags. SwiftUI gets
 * equal heights for free (the card background is greedy inside a `LazyHStack`); on
 * Compose the tallest measured item has to be tracked by hand.
 *
 * The height only ever grows, so this converges after one extra layout pass instead
 * of oscillating.
 */
@Stable
class EqualHeightRow internal constructor() {
    internal var maxHeightPx by mutableIntStateOf(0)
}

@Composable
fun rememberEqualHeightRow(): EqualHeightRow = remember { EqualHeightRow() }

/**
 * Stretches this item to the row's tallest height.
 *
 * Put it on a wrapper with `propagateMinConstraints = true` so the card inside
 * actually fills the extra space instead of floating at the top of a taller box.
 */
@Composable
fun Modifier.equalHeightItem(row: EqualHeightRow): Modifier {
    val minHeight = with(LocalDensity.current) { row.maxHeightPx.toDp() }
    return this
        .heightIn(min = minHeight)
        .onSizeChanged { if (it.height > row.maxHeightPx) row.maxHeightPx = it.height }
}
