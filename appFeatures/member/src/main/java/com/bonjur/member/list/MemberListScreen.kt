package com.bonjur.member.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.navigation.NavArgs
import com.bonjur.navigation.Navigator

@Composable
fun MemberListScreen(
    navigator: Navigator,
    viewModel: MemberListViewModel = hiltViewModel()
) {
    val inputData = remember { NavArgs.get<MemberListInputData>() }

    LaunchedEffect(inputData) {
        inputData?.let { viewModel.init(it, navigator) }
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is MemberListSideEffect.Loading -> { /* Show/hide loading */ }
            }
        }
    ) { store ->
        MemberListView(store = store)
    }
}
