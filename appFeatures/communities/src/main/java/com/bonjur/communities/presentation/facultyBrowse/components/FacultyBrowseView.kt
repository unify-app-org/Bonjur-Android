package com.bonjur.communities.presentation.facultyBrowse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseAction
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseSideEffect
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseViewState
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun FacultyBrowseView(
    store: FeatureStore<FacultyBrowseViewState, FacultyBrowseAction, FacultyBrowseSideEffect>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary.copy(alpha = 0.3f)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = store.state.sectionTitle,
                style = AppTypography.HeadingXL.medium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (store.state.faculties.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No faculties found",
                        style = AppTypography.HeadingMd.regular,
                        color = Palette.blackMedium
                    )
                }
            }
        } else {
            items(store.state.faculties) { faculty ->
                FacultyRowItem(
                    faculty = faculty,
                    onTap = { store.send(FacultyBrowseAction.FacultyTapped(faculty)) }
                )
            }
        }
    }
}

@Composable
private fun FacultyRowItem(
    faculty: FacultyRowModel,
    onTap: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTap)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faculty.title,
                style = AppTypography.TextMd.medium,
                modifier = Modifier.weight(1f)
            )
            if (faculty.memberCount > 0) {
                Text(
                    text = "${faculty.memberCount}",
                    style = AppTypography.TextSm.regular,
                    color = Palette.blackMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                painter = Images.Icons.chevronRight(),
                contentDescription = null,
                tint = Palette.blackMedium,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
