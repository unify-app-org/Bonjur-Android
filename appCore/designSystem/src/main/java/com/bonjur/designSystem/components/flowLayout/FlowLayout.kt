package com.bonjur.designSystem.components.flowLayout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Wrapping row of chips/items. Compose equivalent of iOS `FlowLayout`.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> FlowLayout(
    items: List<T>,
    modifier: Modifier = Modifier,
    spacing: Int = 12,
    content: @Composable (T) -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.dp),
        verticalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        items.forEach { item -> content(item) }
    }
}
