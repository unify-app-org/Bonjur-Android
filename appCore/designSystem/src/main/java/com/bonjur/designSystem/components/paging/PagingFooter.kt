package com.bonjur.designSystem.components.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/** End-of-list spinner shown while a further page exists. Mirrors iOS `PagingFooterView`. */
@Composable
fun PagingFooter(
    hasMore: Boolean,
    modifier: Modifier = Modifier
) {
    if (!hasMore) return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = Palette.appBlue,
            strokeWidth = 2.dp
        )
    }
}

/**
 * End-of-list footer for a genuine `LazyColumn`: it is only composed once the user has
 * scrolled to the end, so composing it *is* the "load the next page" signal. Re-fires
 * after each page because [loadedCount] changes. Use [LoadMoreOnScrollToEnd] instead when
 * the rows are not themselves lazy items.
 */
fun LazyListScope.pagingFooterItem(
    hasMore: Boolean,
    loadedCount: Int,
    onLoadMore: () -> Unit
) {
    if (!hasMore) return
    item(key = "paging_footer") {
        LaunchedEffect(loadedCount) { onLoadMore() }
        PagingFooter(hasMore = true)
    }
}

/**
 * Calls [onLoadMore] whenever the list is scrolled within [threshold] items of its end.
 *
 * Use this instead of an `onAppear`-style hook on the last row when the rows live in an
 * eager `Column` inside one lazy item (the profile/detail tab pagers do): there every row
 * is composed as soon as the screen appears, so a per-row callback would fire on entry
 * and pull every page at once. Scroll position is the only honest signal. The caller's
 * view model is still expected to drop repeat calls while a page is in flight.
 */
@Composable
fun LoadMoreOnScrollToEnd(
    listState: LazyListState,
    enabled: Boolean,
    threshold: Int = 1,
    onLoadMore: () -> Unit
) {
    val reachedEnd = remember(listState, threshold) {
        derivedStateOf {
            val layout = listState.layoutInfo
            val lastVisible = layout.visibleItemsInfo.lastOrNull()
            lastVisible != null && lastVisible.index >= layout.totalItemsCount - 1 - threshold
        }
    }

    LaunchedEffect(listState, enabled) {
        if (!enabled) return@LaunchedEffect
        snapshotFlow { reachedEnd.value }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadMore() }
    }
}
