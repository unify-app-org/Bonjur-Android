//
//  EventsListScreen.kt
//  Events
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.events.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.events.presentation.list.components.EventsListView
import com.bonjur.events.presentation.list.models.EventsListInputData
import com.bonjur.events.presentation.list.models.EventsListSideEffect
import com.bonjur.navigation.Navigator

@Composable
fun EventsListScreen(
    inputData: EventsListInputData = EventsListInputData(),
    navigator: Navigator,
    viewModel: EventsListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is EventsListSideEffect.Loading -> {
                    // Handle loading effect
                }
                is EventsListSideEffect.Error -> {
                    // Handle error effect
                }
            }
        }
    ) { store ->
        EventsListView(store = store)
    }
}