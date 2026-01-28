//
//  EventsListScreen.kt
//  Events
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.events.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.events.presentation.components.EventsListView
import com.bonjur.events.presentation.models.EventsListInputData
import com.bonjur.events.presentation.models.EventsListSideEffect

@Preview(showBackground = true)
@Composable
fun EventsListScreen(
    inputData: EventsListInputData = EventsListInputData(),
    viewModel: EventsListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
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