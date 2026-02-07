//
//  ClubsListScreen.kt
//  Clubs
//
//  Created by Huseyn Hasanov on 17.01.26
//

package com.bonjur.clubs.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.clubs.presentation.list.components.ClubsListView
import com.bonjur.clubs.presentation.list.models.ClubsListInputData
import com.bonjur.clubs.presentation.list.models.ClubsListSideEffect

@Preview(showBackground = true)
@Composable
fun ClubsListScreen(
    inputData: ClubsListInputData = ClubsListInputData(),
    viewModel: ClubsListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ClubsListSideEffect.Loading -> {
                    // Handle loading effect
                }
                is ClubsListSideEffect.Error -> {
                    // Handle error effect
                }
            }
        }
    ) { store ->
        ClubsListView(store = store)
    }
}