package com.bonjur.designSystem.components.categorieChips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.flowLayout.FlowLayout
import com.bonjur.designSystem.components.serach.SearchView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

/** A category group. Kotlin equivalent of iOS `SelectCategoryView.Section`. */
data class CategorySection(
    val type: String,
    val title: String,
    val categories: List<CategoriesChipModel>
)

/**
 * Category picker. Kotlin equivalent of iOS `SelectCategoryView`.
 * Selection state is owned by the caller; tapping a chip calls [onToggle] with its id.
 */
@Composable
fun SelectCategoryView(
    sections: List<CategorySection>,
    onToggle: (Int) -> Unit,
    onDone: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Select category",
    subtitle: String = "You must be choose at least 2 category",
    minimumSelectionCount: Int = 0
) {
    var searchText by remember { mutableStateOf("") }

    val filteredSections = remember(sections, searchText) {
        val query = searchText.trim()
        if (query.isEmpty()) {
            sections
        } else {
            sections.mapNotNull { section ->
                val matches = section.categories.filter {
                    it.title.contains(query, ignoreCase = true)
                }
                if (matches.isEmpty()) null else section.copy(categories = matches)
            }
        }
    }

    val selectedCount = sections.sumOf { section -> section.categories.count { it.selected } }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Icon(
                painter = Images.Icons.xmark(),
                contentDescription = "Close",
                tint = Palette.black,
                modifier = Modifier.clickable { onClose() }
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = AppTypography.TitleL.extraBold, color = Palette.black)
            Text(text = subtitle, style = AppTypography.BodyTextMd.regular, color = Palette.grayPrimary)
        }

        SearchView(text = searchText, onTextChange = { searchText = it })

        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredSections, key = { it.type + it.title }) { section ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = section.title,
                        style = AppTypography.BodyTextMd.semiBold,
                        color = Palette.blackHigh
                    )
                    FlowLayout(items = section.categories, spacing = 12) { item ->
                        CategoriesChipsView(model = item, onClick = onToggle)
                    }
                }
            }
        }

        AppButton(
            title = "Ok",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = onDone,
            enabled = selectedCount >= minimumSelectionCount,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
