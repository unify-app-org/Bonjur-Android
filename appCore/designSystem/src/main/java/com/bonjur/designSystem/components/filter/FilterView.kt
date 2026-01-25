//
//  FilterView.kt
//  DiscoverImpl
//
//  Created by Huseyn Hasanov on 12.01.26
//

package com.bonjur.designSystem.components.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.yourapp.discover.viewmodel.FilterViewModel

@Composable
fun FilterView(
    model: List<FilterView.Model>,
    selectedItems: (List<FilterView.Items>) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: FilterViewModel = viewModel(
        factory = FilterViewModelFactory(
            initialModel = model,
            onItemsSelected = selectedItems
        )
    )

    var presentFilter by remember { mutableStateOf(false) }
    val modelState by viewModel.model.collectAsState()
    val selectedItem by viewModel.selectedItem.collectAsState()

    LaunchedEffect(model) {
        viewModel.updateModel(model)
    }

    LaunchedEffect(Unit) {
        viewModel.sortFilters()
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Background overlay (scrim) - MOVED TO BOTTOM LAYER
        if (selectedItem != null) {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable {
                        viewModel.selectItem(null)
                    }
                    .zIndex(0f)
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f), // Middle layer
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            ChipsView(
                viewModel = viewModel,
                modelState = modelState,
                onFilterClick = { presentFilter = true }
            )
        }

        // Overlay for sub-items selection - TOP LAYER
        AnimatedVisibility(
            visible = selectedItem != null,
            enter = fadeIn(animationSpec = tween(250)),
            exit = fadeOut(animationSpec = tween(250)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f) // Top layer - above everything
        ) {
            SelectSubItems(
                items = selectedItem?.items ?: emptyList(),
                viewModel = viewModel,
                modifier = Modifier
                    .offset(y = 64.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    )
            )
        }
    }

    // Full screen filter dialog - PROPER MODAL
    if (presentFilter) {
        Dialog(
            onDismissRequest = { presentFilter = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                FilterScreen(
                    viewModel = viewModel,
                    onDismiss = { presentFilter = false }
                )
            }
        }
    }
}

@Composable
private fun SelectSubItems(
    items: List<FilterView.Items>,
    viewModel: FilterViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(184.dp)
                .padding(top = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(items, key = { it.uuid }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleSubItem(item) },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = if (item.selected) {
                            Images.Icons.selectedCheckBox()
                        } else {
                            Images.Icons.notSelectedCheckBox()
                        },
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.title,
                        style = AppTypography.BodyTextSm.regular,
                        color = Palette.black
                    )
                }
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AppButton(
                title = "Remove",
                model = AppButtonModel(
                    type = ButtonType.Secondary,
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Small
                ),
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.removeSelection()
                }
            )

            AppButton(
                title = "Apply",
                model = AppButtonModel(
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Small
                ),
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.confirmSelection()
                }
            )
        }
    }
}

@Composable
private fun ChipsView(
    viewModel: FilterViewModel,
    modelState: List<FilterView.Model>,
    onFilterClick: () -> Unit
) {
    if (modelState.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FilterChip(
                    viewModel = viewModel,
                    onFilterClick = onFilterClick
                )
            }

            items(modelState, key = { it.id }) { item ->
                ChipItem(
                    item = item,
                    viewModel = viewModel,
                    isLast = modelState.lastOrNull()?.id == item.id
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    viewModel: FilterViewModel,
    onFilterClick: () -> Unit
) {
    val modelState by viewModel.model.collectAsState()
    val selectedCount = remember(modelState) {
        viewModel.getSelectedCount()
    }

    Box(
        modifier = Modifier
            .padding(start = 16.dp)
            .padding(vertical = 14.dp)
    ) {
        Surface(
            onClick = onFilterClick,
            shape = CircleShape,
            color = if (selectedCount > 0) {
                Palette.primary.copy(alpha = 0.4f)
            } else {
                Palette.grayQuaternary
            },
            border = BorderStroke(
                width = if (selectedCount > 0) 1.dp else 0.dp,
                color = if (selectedCount > 0) Palette.border else Color.Transparent
            ),
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = Images.Icons.filter(),
                    contentDescription = "Filter",
                    tint = Palette.black
                )
                Text(
                    text = "Filter",
                    style = AppTypography.TextL.regular,
                    color = Palette.black
                )
            }
        }

        if (selectedCount > 0) {
            Surface(
                shape = CircleShape,
                color = Palette.border,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-2).dp)
            ) {
                Text(
                    text = "$selectedCount",
                    style = AppTypography.TextSm.regular,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(vertical = 1.dp, horizontal = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun ChipItem(
    item: FilterView.Model,
    viewModel: FilterViewModel,
    isLast: Boolean
) {
    val selectedItem by viewModel.selectedItem.collectAsState()
    val isSelected = viewModel.isSelected(item)
    val hasSelectedSubItems = viewModel.hasSelectedSubItems(item)

    val rotationAngle by animateFloatAsState(
        targetValue = if (selectedItem?.id == item.id) 180f else 0f,
        animationSpec = tween(250),
        label = "chevron rotation"
    )

    Surface(
        onClick = {
            viewModel.selectItem(item)
        },
        shape = CircleShape,
        color = when {
            isSelected -> Color.Transparent
            hasSelectedSubItems -> Palette.primary.copy(alpha = 0.4f)
            else -> Palette.grayQuaternary
        },
        border = if (isSelected || hasSelectedSubItems) {
            androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = when {
                    hasSelectedSubItems && isSelected -> Palette.black
                    hasSelectedSubItems -> Palette.border
                    else -> Palette.black
                }
            )
        } else {
            null
        },
        modifier = Modifier.padding(
            top = 12.dp,
            bottom = 12.dp,
            end = if (isLast) 16.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                style = AppTypography.TextL.regular,
                color = Palette.black
            )
            Icon(
                painter = Images.Icons.chevronDown02(),
                contentDescription = null,
                tint = Palette.black,
                modifier = Modifier.rotate(rotationAngle)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterView() {
    Column(modifier = Modifier.fillMaxSize()) {
        FilterView(
            model = FilterViewMocks.mockData,
            selectedItems = { items ->
                println("Selected items: ${items.map { it.title }}")
            }
        )
    }
}

class FilterViewModelFactory(
    private val initialModel: List<FilterView.Model>,
    private val onItemsSelected: (List<FilterView.Items>) -> Unit
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel(initialModel, onItemsSelected) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}