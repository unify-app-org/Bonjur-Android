package com.bonjur.communities.presentation.membersList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.communities.presentation.membersList.models.MembersListAction
import com.bonjur.communities.presentation.membersList.models.MembersListSideEffect
import com.bonjur.communities.presentation.membersList.models.MembersListViewState
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.member.components.MemberCell
import com.bonjur.member.components.MemberCellAccessory
import com.bonjur.member.components.MemberSectionHeader

@Composable
fun MembersListView(
    store: FeatureStore<MembersListViewState, MembersListAction, MembersListSideEffect>
) {
    val listState = rememberLazyListState()

    // Trigger load-more once the user nears the end of the list.
    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && last >= total - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && store.state.hasMore && !store.state.isLoadingMore) {
            store.send(MembersListAction.LoadMore)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.white),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        store.state.sections.forEach { section ->
            item(key = "header_${section.title}") {
                MemberSectionHeader(
                    title = section.title,
                    memberCountText = "${section.memberCount}"
                )
                Box(modifier = Modifier.size(12.dp))
            }

            section.members.forEach { member ->
                item(key = "member_${member.id}") {
                    MemberCell(
                        member = member,
                        accessory = MemberCellAccessory.None,
                        onTap = { store.send(MembersListAction.MemberTapped(member)) }
                    )
                    Box(modifier = Modifier.size(10.dp))
                }
            }
        }

        if (store.state.isLoadingMore) {
            item(key = "loading_more") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                }
            }
        }

        if (store.state.sections.isEmpty() && !store.state.isLoadingMore) {
            item(key = "empty") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No members found",
                        style = AppTypography.TextL.medium,
                        color = Palette.blackMedium
                    )
                }
            }
        }
    }
}
