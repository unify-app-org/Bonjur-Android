//
//  GroupsListScreen.kt
//  Groups
//
//  Created by Huseyn Hasanov on 23.01.26
//

package com.bonjur.groups.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.groups.presentation.components.GroupsListView
import com.bonjur.groups.presentation.models.GroupsListInputData
import com.bonjur.groups.presentation.models.GroupsListSideEffect

@Preview(showBackground = true)
@Composable
fun GroupsListScreen(
    inputData: GroupsListInputData = GroupsListInputData(),
    viewModel: GroupsListViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is GroupsListSideEffect.Loading -> {
                    if (effect.isLoading) com.bonjur.designSystem.components.loading.AppLoadingUI.show()
                    else com.bonjur.designSystem.components.loading.AppLoadingUI.dismiss()
                }
                is GroupsListSideEffect.Error -> com.bonjur.designSystem.components.snackbar.AppSnackBar.show(
                    title = effect.error.message ?: "Something went wrong",
                    style = com.bonjur.designSystem.components.snackbar.AppSnackBar.Style.ERROR
                )
            }
        }
    ) { store ->
        GroupsListView(store = store)
    }
}