package com.bonjur.app.tabBar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun AppTabBar() {
    val items = listOf(
        TabItem.Discover,
        TabItem.Clubs,
        TabItem.MyPlans,
        TabItem.Profile
    )

    var selectedTab: TabItem by remember { mutableStateOf(TabItem.Discover) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = { selectedTab = item },
                        icon = {
                            Icon(
                                painter = item.icon(),
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(text = item.label)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Palette.blackHigh,
                            selectedTextColor = Palette.blackHigh,
                            unselectedIconColor = Palette.graySecondary,
                            unselectedTextColor = Palette.graySecondary,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                TabItem.Discover -> Text("Tab 1")
                TabItem.Clubs -> Text("Tab 2")
                TabItem.MyPlans -> Text("Tab 3")
                TabItem.Profile -> Text("Tab 4")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppTabBarPreview() {
    AppTabBar()
}
