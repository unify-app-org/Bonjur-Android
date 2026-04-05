package com.bonjur.profile.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.navigation.NavArgs
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.detail.ProfileDetailScreen
import com.bonjur.profile.presentation.detail.models.ProfileDetailInputData
import com.bonjur.profile.presentation.studentCard.StudentCardScreen
import com.bonjur.profile.presentation.studentCard.models.StudentCardInputData

fun NavGraphBuilder.profileNavGraph(navigator: Navigator) {
    composable<ProfileScreens.ProfileDetail> {
        ProfileDetailScreen(
            inputData = ProfileDetailInputData(),
            navigator = navigator
        )
    }

    composable<ProfileScreens.StudentCard> {
        val inputData = remember { NavArgs.get<StudentCardInputData>() ?: StudentCardInputData() }
        StudentCardScreen(inputData = inputData, navigator = navigator)
    }
}
