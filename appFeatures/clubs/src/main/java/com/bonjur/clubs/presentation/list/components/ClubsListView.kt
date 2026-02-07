//
//  ClubsListView.kt
//  Clubs
//
//  Created by Huseyn Hasanov on 17.01.26
//

package com.bonjur.clubs.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.list.models.ClubsListAction
import com.bonjur.clubs.presentation.list.models.ClubsListSideEffect
import com.bonjur.clubs.presentation.list.models.ClubsListViewState
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.serach.SearchView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun ClubsListView(
    store: FeatureStore<ClubsListViewState, ClubsListAction, ClubsListSideEffect>
) {
    val state = store.state
    val density = LocalDensity.current

    var topViewHeight by remember { mutableStateOf(0.dp) }
    var searchAndTextHeight by remember { mutableStateOf(0.dp) }
    var filterViewHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(Unit) {
        store.send(ClubsListAction.FetchData)
    }

    LaunchedEffect(searchAndTextHeight, filterViewHeight) {
        topViewHeight = searchAndTextHeight + filterViewHeight
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(topViewHeight))

            // Scrollable content
            if (state.uiModel.clubs.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(state.uiModel.clubs, key = { it.uuid }) { club ->
                        ClubCardView(
                            model = club,
                            onTap = {
                                store.send(ClubsListAction.clubItemTapped(id = club.id))
                            }
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppEmptyView(
                        model = AppEmptyModel(
                            icon = Images.Icons.twoUsers(),
                            text = "There are no clubs for this community yet. Be the pioneer and start the very first one now!",
                            buttonTitle = "Create a club +"
                        ),
                        onButtonClick = {

                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .background(Palette.white)
                .onGloballyPositioned { coordinates ->
                    searchAndTextHeight = with(density) { coordinates.size.height.toDp() }
                }
        ) {
            TopView(
                searchText = state.uiModel.searchText,
                onSearchTextChanged = { text ->
                    store.send(ClubsListAction.SearchTextChanged(text))
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .zIndex(2f)
                .padding(top = searchAndTextHeight + 8.dp)
        ) {
            FilterView(
                model = state.uiModel.filters,
                selectedItems = { items ->
                    store.send(ClubsListAction.FilterSelected(items))
                },
                onChipsHeightChanged = { height ->
                    filterViewHeight = height
                }
            )
        }
    }
}

@Composable
private fun TopView(
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Palette.white),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Title
        Text(
            text = "Clubs",
            style = AppTypography.TitleL.extraBold,
            color = Palette.black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            textAlign = TextAlign.Start
        )

        SearchView(
            text = searchText,
            onTextChange = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}