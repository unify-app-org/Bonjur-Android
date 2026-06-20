package com.bonjur.profile.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.navigation.NavArgs
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.detail.ProfileDetailScreen
import com.bonjur.profile.presentation.detail.models.ProfileDetailInputData
import com.bonjur.profile.presentation.editProfile.EditProfileScreen
import com.bonjur.profile.presentation.editProfile.models.EditProfileInputData
import com.bonjur.profile.presentation.settings.ProfileSettingsScreen
import com.bonjur.profile.presentation.settings.models.ProfileSettingsInputData
import com.bonjur.profile.presentation.studentCard.StudentCardScreen
import com.bonjur.profile.presentation.studentCard.models.StudentCardInputData

fun NavGraphBuilder.profileNavGraph(navigator: Navigator) {
    composable<ProfileScreens.ProfileDetail> {
        val inputData = remember { NavArgs.get<ProfileDetailInputData>() ?: ProfileDetailInputData() }
        ProfileDetailScreen(
            inputData = inputData,
            navigator = navigator
        )
    }

    composable<ProfileScreens.StudentCard> {
        val inputData = remember { NavArgs.get<StudentCardInputData>() ?: StudentCardInputData() }
        StudentCardScreen(inputData = inputData, navigator = navigator)
    }

    composable<ProfileScreens.EditProfile> {
        EditProfileScreen(
            inputData = EditProfileInputData(),
            navigator = navigator
        )
    }

    composable<ProfileScreens.Settings> {
        ProfileSettingsScreen(
            inputData = ProfileSettingsInputData(),
            navigator = navigator
        )
    }
}
