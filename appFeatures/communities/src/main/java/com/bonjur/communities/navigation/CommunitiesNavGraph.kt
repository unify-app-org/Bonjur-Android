package com.bonjur.communities.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.communities.presentation.detail.CommunityDetailScreen
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.communities.presentation.list.CommunitiesListScreen
import com.bonjur.navigation.NavArgs
import com.bonjur.navigation.Navigator

fun NavGraphBuilder.communitiesNavGraph(
    navigator: Navigator
) {
    composable<CommunitiesScreens.List> {
        CommunitiesListScreen(navigator = navigator)
    }

    composable<CommunitiesScreens.Details> {
        val inputData = remember { NavArgs.get<CommunityDetailInputData>() ?: CommunityDetailInputData(communityId = 1) }
        CommunityDetailScreen(
            inputData = inputData,
            navigator = navigator
        )
    }
}
