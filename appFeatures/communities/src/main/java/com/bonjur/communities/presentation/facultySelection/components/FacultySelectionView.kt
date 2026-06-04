package com.bonjur.communities.presentation.facultySelection.components

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
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyRowModel
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionAction
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionSideEffect
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionViewState
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun FacultySelectionView(
    store: FeatureStore<FacultySelectionViewState, FacultySelectionAction, FacultySelectionSideEffect>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary.copy(alpha = 0.3f)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (store.state.faculties.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No faculties available", style = AppTypography.TextL.medium, color = Palette.blackMedium)
                }
            }
        } else {
            items(store.state.faculties) { faculty ->
                FacultySelectionRow(
                    faculty = faculty,
                    isSelected = store.state.selectedFacultyId == faculty.id,
                    onTap = { store.send(FacultySelectionAction.FacultySelected(faculty)) }
                )
            }
        }
    }
}

@Composable
private fun FacultySelectionRow(
    faculty: FacultyRowModel,
    isSelected: Boolean,
    onTap: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Palette.appBlue.copy(alpha = 0.1f) else Color.White
        ),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onTap)
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
                color = if (isSelected) Palette.appBlue else Palette.blackHigh,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(android.R.drawable.checkbox_on_background),
                    contentDescription = "Selected",
                    tint = Palette.appBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
