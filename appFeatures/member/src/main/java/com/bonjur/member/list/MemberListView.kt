package com.bonjur.member.list

import com.bonjur.designSystem.localization.LanguageManager
import androidx.compose.ui.res.stringResource
import com.bonjur.designsystem.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.serach.SearchView
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.member.components.MemberCell
import com.bonjur.member.components.MemberOptionsInput
import com.bonjur.member.components.MemberOptionsSheet
import com.bonjur.member.components.MemberSectionHeader
import com.bonjur.member.components.memberOptionsAccessory
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.policy.MemberOptionsPolicy

@Composable
fun MemberListView(
    store: FeatureStore<MemberListViewState, MemberListAction, MemberListSideEffect>
) {
    val listState = rememberLazyListState()
    val currentUserId = store.state.currentUserId
    val viewerRole = store.state.viewerRole
    val activityType = store.state.activityType

    var optionsMember by remember { mutableStateOf<MemberCellModel?>(null) }

    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && last >= total - 3
        }
    }
    // A search that collapses 40 rows to 1 leaves the viewport past the end of the
    // new content, so the screen looks empty. Snap back to the top on every reset.
    LaunchedEffect(store.state.listResetToken) {
        if (store.state.listResetToken > 0) listState.scrollToItem(0)
    }

    LaunchedEffect(shouldLoadMore, store.state.sections) {
        if (shouldLoadMore && store.state.hasMore && !store.state.isLoadingMore) {
            store.send(MemberListAction.LoadMore)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Palette.white)) {
        AppTopBar(
            isScrolled = isScrolled,
            onBack = { store.send(MemberListAction.BackTapped) },
            title = store.state.title,
            showTitle = true
        )

        SearchView(
            text = store.state.searchText,
            onTextChange = { store.send(MemberListAction.SearchTextChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            store.state.sections.forEachIndexed { sectionIndex, section ->
                item(key = "header_${sectionIndex}_${section.title}") {
                    // The last section is the one paging fills, so it carries the
                    // server total; earlier sections are already complete.
                    val isPagingSection = section === store.state.sections.last()
                    val count = if (isPagingSection) {
                        store.state.totalCount ?: section.memberCount
                    } else {
                        section.memberCount
                    }
                    MemberSectionHeader(title = section.title, memberCountText = "$count")
                    Box(modifier = Modifier.size(12.dp))
                }
                // Key includes the position: a member with no userId falls back to a
                // shared placeholder id, and two of those in one page would collide.
                section.members.forEachIndexed { memberIndex, member ->
                    item(key = "member_${sectionIndex}_${memberIndex}_${member.id}") {
                        MemberCell(
                            member = member,
                            accessory = memberOptionsAccessory(member, currentUserId),
                            onTap = { store.send(MemberListAction.MemberTapped(member)) },
                            onAccessoryTap = { optionsMember = member }
                        )
                        Box(modifier = Modifier.size(10.dp))
                    }
                }
            }

            if (store.state.isLoadingMore) {
                item(key = "loading_more") {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                    }
                }
            }

            if (store.state.sections.isEmpty() && !store.state.isLoadingMore) {
                item(key = "empty") {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.common_no_members_found), style = AppTypography.TextL.medium, color = Palette.blackMedium)
                    }
                }
            }
        }
    }

    optionsMember?.let { member ->
        val isSelf = member.id == currentUserId
        MemberOptionsSheet(
            input = MemberOptionsInput(
                memberName = member.name,
                currentRole = member.role,
                assignableRoles = MemberOptionsPolicy.assignableRoles(viewerRole),
                showChangeRole = MemberOptionsPolicy.canChangeRole(
                    viewer = viewerRole,
                    activity = activityType,
                    isSelf = isSelf
                ),
                showReport = MemberOptionsPolicy.canReport(isSelf),
                onAssignRole = { role -> store.send(MemberListAction.AssignRole(member.id, role)) },
                onReport = { AppSnackBar.show(title = LanguageManager.string(R.string.common_report_submitted), style = AppSnackBar.Style.SUCCESS) }
            ),
            onDismiss = { optionsMember = null }
        )
    }
}
