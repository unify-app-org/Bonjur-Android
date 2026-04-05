package com.bonjur.communities.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.communities.presentation.list.model.CommunitiesListAction
import com.bonjur.communities.presentation.list.model.CommunitiesListSideEffect
import com.bonjur.communities.presentation.list.model.CommunitiesListViewState
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.serach.SearchView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun CommunitiesListView(
    store: FeatureStore<CommunitiesListViewState, CommunitiesListAction, CommunitiesListSideEffect>
) {
    val state = store.state
    val density = LocalDensity.current

    var topViewHeight by remember { mutableStateOf(0.dp) }
    var searchAndTextHeight by remember { mutableStateOf(0.dp) }
    var filterViewHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(Unit) {
        store.send(CommunitiesListAction.FetchData)
    }

    LaunchedEffect(searchAndTextHeight, filterViewHeight) {
        topViewHeight = searchAndTextHeight + filterViewHeight
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(topViewHeight))

            if (state.uiModel.communities.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(state.uiModel.communities, key = { it.uuid }) { community ->
                        CommunityCardView(
                            model = community,
                            onTap = {
                                store.send(CommunitiesListAction.CommunityItemTapped(id = community.id))
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
                            text = "There are no communities yet. Be the pioneer and start the very first one now!",
                            buttonTitle = "Create a community +"
                        ),
                        onButtonClick = { }
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
                .statusBarsPadding()
        ) {
            TopView(
                searchText = state.uiModel.searchText,
                onSearchTextChanged = { text ->
                    store.send(CommunitiesListAction.SearchTextChanged(text))
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
                    store.send(CommunitiesListAction.FilterSelected(items))
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
        Text(
            text = "Communities",
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
