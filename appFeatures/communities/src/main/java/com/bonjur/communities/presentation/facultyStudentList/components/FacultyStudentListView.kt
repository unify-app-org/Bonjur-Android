package com.bonjur.communities.presentation.facultyStudentList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.communities.presentation.facultyBrowse.models.MemberCellModel
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListAction
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListSideEffect
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListViewState
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun FacultyStudentListView(
    store: FeatureStore<FacultyStudentListViewState, FacultyStudentListAction, FacultyStudentListSideEffect>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        store.state.sections.forEach { section ->
            item {
                // Section header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = section.title,
                        style = AppTypography.HeadingMd.medium
                    )
                    Text(
                        text = "${section.memberCount}",
                        style = AppTypography.TextSm.regular,
                        color = Palette.blackMedium
                    )
                }
            }

            section.members.forEach { member ->
                item {
                    MemberRow(
                        member = member,
                        onTap = { store.send(FacultyStudentListAction.MemberTapped(member)) }
                    )
                }
            }
        }

        if (store.state.sections.isEmpty()) {
            item {
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

@Composable
private fun MemberRow(
    member: MemberCellModel,
    onTap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Palette.grayQuaternary, CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            CachedAsyncImage(
                url = member.avatarUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = {
                    Icon(
                        painter = Images.Icons.user(),
                        contentDescription = null,
                        tint = Palette.blackMedium,
                        modifier = Modifier.size(22.dp)
                    )
                }
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = member.name, style = AppTypography.TextMd.medium)
            if (member.subtitle.isNotEmpty()) {
                Text(
                    text = member.subtitle,
                    style = AppTypography.TextSm.regular,
                    color = Palette.blackMedium
                )
            }
        }
    }
}
