package com.bonjur.member.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bonjur.navigation.Navigator

/** Shared members-list (see-all) graph. Registered once at app level, reused by every activity. */
fun NavGraphBuilder.memberNavGraph(navigator: Navigator) {
    composable<MemberListScreens.MembersList> {
        MemberListScreen(navigator = navigator)
    }
}
