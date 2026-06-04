package com.bonjur.communities.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.bonjur.communities.presentation.detail.CommunityDetailScreen
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.communities.presentation.facultyBrowse.FacultyBrowseScreen
import com.bonjur.communities.presentation.facultyBrowse.models.FacultyBrowseInputData
import com.bonjur.communities.presentation.facultySelection.FacultySelectionScreen
import com.bonjur.communities.presentation.facultySelection.models.FacultySelectionInputData
import com.bonjur.communities.presentation.facultyStudentList.FacultyStudentListScreen
import com.bonjur.communities.presentation.facultyStudentList.models.FacultyStudentListInputData
import com.bonjur.communities.presentation.facultyStudentSelectList.FacultyStudentSelectListScreen
import com.bonjur.communities.presentation.facultyStudentSelectList.models.FacultyStudentSelectListInputData
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

    composable<CommunitiesScreens.FacultyBrowse> { backStackEntry ->
        val route = backStackEntry.toRoute<CommunitiesScreens.FacultyBrowse>()
        FacultyBrowseScreen(
            inputData = FacultyBrowseInputData(
                communityId = route.communityId,
                title = route.title
            ),
            navigator = navigator
        )
    }

    composable<CommunitiesScreens.FacultySelection> { backStackEntry ->
        val route = backStackEntry.toRoute<CommunitiesScreens.FacultySelection>()
        FacultySelectionScreen(
            inputData = FacultySelectionInputData(
                communityId = route.communityId,
                title = route.title
            ),
            navigator = navigator
        )
    }

    composable<CommunitiesScreens.FacultyStudentList> { backStackEntry ->
        val route = backStackEntry.toRoute<CommunitiesScreens.FacultyStudentList>()
        FacultyStudentListScreen(
            inputData = FacultyStudentListInputData(
                communityId = route.communityId,
                facultyId = route.facultyId,
                title = route.title
            ),
            navigator = navigator
        )
    }

    composable<CommunitiesScreens.FacultyStudentSelectList> { backStackEntry ->
        val route = backStackEntry.toRoute<CommunitiesScreens.FacultyStudentSelectList>()
        FacultyStudentSelectListScreen(
            inputData = FacultyStudentSelectListInputData(
                communityId = route.communityId,
                facultyId = route.facultyId,
                title = route.title
            ),
            navigator = navigator
        )
    }
}
