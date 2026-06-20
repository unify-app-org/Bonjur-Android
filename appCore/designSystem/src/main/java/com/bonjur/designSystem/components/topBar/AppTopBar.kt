package com.bonjur.designSystem.components.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

/**
 * Reusable scroll-aware top navigation bar. Compose equivalent of the iOS
 * native nav toolbar (`toolbarItemBackground` + `toolbarBackground`): the bar
 * is transparent over a hero/cover header and turns solid white with a shadow
 * once [isScrolled]. Leading back button + optional center title (fades in via
 * [showTitle]) + optional [trailing] actions.
 *
 * Use [AppNavBarButton] for trailing icons so they match the back button.
 */
@Composable
fun AppTopBar(
    isScrolled: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    showTitle: Boolean = false,
    trailing: @Composable () -> Unit = {}
) {
    Surface(
        color = if (isScrolled) Color.White else Color.Transparent,
        shadowElevation = if (isScrolled) 4.dp else 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppNavBarButton(
                    icon = Images.Icons.arrowLeft01(),
                    isScrolled = isScrolled,
                    onClick = onBack
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    trailing()
                }
            }

            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 80.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = AppTypography.HeadingXL.bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/** Pill icon button used inside [AppTopBar] (back + trailing). Matches the details nav buttons. */
@Composable
fun AppNavBarButton(
    icon: Painter,
    isScrolled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(44.dp)
            .background(
                color = if (isScrolled) Palette.grayQuaternary else Palette.whiteMedium,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Palette.blackHigh,
            modifier = Modifier.size(20.dp)
        )
    }
}
