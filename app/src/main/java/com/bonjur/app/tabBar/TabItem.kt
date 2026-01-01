package com.bonjur.app.tabBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.bonjur.designSystem.ui.theme.image.Images

sealed class TabItem(
    val label: String,
    val icon: @Composable () -> Painter
) {
    object Discover : TabItem("Discover", { Images.Icons.home() })
    object Clubs : TabItem("Clubs", { Images.Icons.userGroups() })
    object MyPlans : TabItem("My plans", { Images.Icons.clipboardList() })
    object Profile : TabItem("Profile", { Images.Icons.userGroups() })
}
