package com.bonjur.app.tabBar

import com.bonjur.app.R
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R as DesignR
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.bonjur.designSystem.ui.theme.image.Images

sealed class TabItem(
    @StringRes private val labelRes: Int,
    val icon: @Composable () -> Painter
) {
    // Resolved on read, not in the constructor: these objects initialize at class
    // load, before LanguageManager has a Context, and a captured value could never
    // follow a language switch.
    val label: String get() = LanguageManager.string(labelRes)

    object Discover : TabItem(R.string.dock_discover, { Images.Icons.home() })
    object Clubs : TabItem(DesignR.string.clubs, { Images.Icons.userGroups() })
    object MyPlans : TabItem(R.string.dock_my_activities, { Images.Icons.clipboardList() })
    object Profile : TabItem(R.string.dock_profile, { Images.Icons.profile() })
}

data class CreateMenuItem(
    val title: String,
    val icon: Painter,
    val type: CreateType
)

enum class CreateType {
    CLUB, EVENT, HANGOUT
}