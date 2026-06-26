package com.bonjur.profile.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.navigation.NavArgs
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.ProfileDetailNavArgs
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
        // Accept the module-local input OR the neutral cross-module payload (member tap
        // from clubs/events/hangouts, which can't depend on profile — would cycle).
        // Args present => pushed (member tap from clubs/events/hangouts). No args => root Profile tab.
        val inputData = remember {
            NavArgs.get<ProfileDetailInputData>()?.copy(isPushed = true)
                ?: NavArgs.get<ProfileDetailNavArgs>()?.let {
                    ProfileDetailInputData(userId = it.userId, isPushed = true)
                }
                ?: ProfileDetailInputData()
        }
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
        val inputData = remember { NavArgs.get<EditProfileInputData>() ?: EditProfileInputData() }
        EditProfileScreen(
            inputData = inputData,
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
