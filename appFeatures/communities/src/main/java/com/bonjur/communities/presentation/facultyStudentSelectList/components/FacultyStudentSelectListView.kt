package com.bonjur.communities.presentation.facultyStudentSelectList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.communities.presentation.facultyBrowse.models.MemberCellModel
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListAction
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListSideEffect
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListViewState
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun FacultyStudentSelectListView(
    store: FeatureStore<FacultyStudentSelectListViewState, FacultyStudentSelectListAction, FacultyStudentSelectListSideEffect>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            store.state.sections.forEach { section ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = section.title, style = AppTypography.HeadingMd.medium)
                        Text(
                            text = "${section.memberCount}",
                            style = AppTypography.TextSm.regular,
                            color = Palette.blackMedium
                        )
                    }
                }

                section.members.forEach { member ->
                    item {
                        SelectableMemberRow(
                            member = member,
                            isSelected = store.state.selectedMemberIds.contains(member.id),
                            onTap = { store.send(FacultyStudentSelectListAction.MemberToggled(member)) }
                        )
                    }
                }
            }

            if (store.state.sections.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No members found", style = AppTypography.TextL.medium, color = Palette.blackMedium)
                    }
                }
            }
        }

        if (store.state.selectedMemberIds.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                com.bonjur.designSystem.components.button.AppButton(
                    title = "Select (${store.state.selectedMemberIds.size})",
                    model = com.bonjur.designSystem.components.button.AppButtonModel(
                        contentSize = com.bonjur.designSystem.components.button.ContentSize.Fill
                    ),
                    onClick = { store.send(FacultyStudentSelectListAction.DoneTapped) }
                )
            }
        }
    }
}

@Composable
private fun SelectableMemberRow(
    member: MemberCellModel,
    isSelected: Boolean,
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
                Text(text = member.subtitle, style = AppTypography.TextSm.regular, color = Palette.blackMedium)
            }
        }

        Checkbox(
            checked = isSelected,
            onCheckedChange = { onTap() }
        )
    }
}
