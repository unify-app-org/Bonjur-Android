//
//  ClubsListScreen.kt
//  Clubs
//
//  Created by Huseyn Hasanov on 17.01.26
//

package com.bonjur.clubs.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.clubs.presentation.list.components.ClubsListView
import com.bonjur.clubs.presentation.list.models.ClubsListInputData
import com.bonjur.clubs.presentation.list.models.ClubsListSideEffect
import com.bonjur.navigation.Navigator


@Composable
fun ClubsListScreen(
    inputData: ClubsListInputData = ClubsListInputData(),
    navigator: Navigator,
    viewModel: ClubsListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData, navigator)
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