//
//  FilterScreen.kt
//  DiscoverImpl
//
//  Created by Huseyn Hasanov on 13.01.26
//

package com.bonjur.designSystem.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategoriesChipsView
import com.bonjur.designSystem.components.serach.SearchView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.yourapp.discover.viewmodel.FilterViewModel

@Composable
fun FilterScreen(
    viewModel: FilterViewModel,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchFilterScreenData()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BackButton(onDismiss = onDismiss)
        TopView()
        SearchView(text = "", onTextChange =  { text ->

        })
        FilterList(
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )
        ActionButtons(
            viewModel = viewModel,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun BackButton(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = onDismiss) {
            Icon(
                painter = Images.Icons.arrowLeft01(),
                contentDescription = "Back",
                tint = Palette.black
            )
        }
    }
}

@Composable
private fun TopView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Filter",
            style = AppTypography.TitleL.extraBold,
            color = Palette.black,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Select your interests to find the perfect community events for you.",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FilterList(
    viewModel: FilterViewModel,
    modifier: Modifier = Modifier
) {
    val filterModel by viewModel.filterModel.collectAsState()
    
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        filterModel.forEach { section ->
            item {
                Text(
                    text = section.title.capitalizeFirstLetter(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Palette.blackHigh,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    section.items.forEach { item ->
                        CategoriesChipsView(
                            model = CategoriesChipModel(
                                id = item.id,
                                title = item.title,
                                selected = item.selected
                            ),
                            onClick = {
                                viewModel.toggleSubItemInFilterScreen(item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtons(
    viewModel: FilterViewModel,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        AppButton(
            title = "Remove",
            model = AppButtonModel(
                type = ButtonType.Secondary,
                contentSize = ContentSize.Fill
            ),
            modifier = Modifier.weight(1f),
            onClick = {
                viewModel.removeAllFilters()
                onDismiss()
            }
        )
        
        AppButton(
            title = "Apply",
            model = AppButtonModel(
                contentSize = ContentSize.Fill
            ),
            modifier = Modifier.weight(1f),
            onClick = {
                viewModel.confirmFilterScreen()
                onDismiss()
            }
        )
    }
}

private fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { 
        if (it.isLowerCase()) it.titlecase() else it.toString() 
    }
}