package com.bonjur.designSystem.components.tabView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment

@Composable
fun AppTabView(
    pageCount: Int,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (page: Int) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { pageCount }
    )

    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
        if (pagerState.currentPage != currentPage) {
            onPageChange(pagerState.currentPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { newPage ->
                onPageChange(newPage)
            }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            content(page)
        }

        CustomPageIndicator(
            numberOfPages = pageCount,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
        )
    }
}
