package com.bonjur.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.navigation.Navigator
import com.bonjur.notification.presentation.feed.NotificationFeedScreen
import com.bonjur.notification.presentation.needsAction.NeedsActionScreen
import com.bonjur.notification.presentation.verification.VerificationScreen

fun NavGraphBuilder.notificationNavGraph(navigator: Navigator) {
    composable<NotificationScreens.Feed> {
        NotificationFeedScreen(navigator = navigator)
    }
    composable<NotificationScreens.NeedsAction> {
        NeedsActionScreen(navigator = navigator)
    }
    composable<NotificationScreens.Verification> {
        VerificationScreen(navigator = navigator)
    }
}
