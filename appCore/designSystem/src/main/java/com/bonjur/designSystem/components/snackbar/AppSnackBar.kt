package com.bonjur.designSystem.components.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global toast/snackbar. Presents above everything via [AppSnackBarOverlay]
 * (mounted once next to AppAlertOverlay), so it can be called from anywhere
 * (view models, etc.) without a view reference. Mirrors iOS `AppSnackBar`.
 *
 *     AppSnackBar.show(
 *         title = "Event created successfully",
 *         subtitle = "Football events · now active",
 *         style = AppSnackBar.Style.SUCCESS
 *     )
 */
object AppSnackBar {

    enum class Style {
        SUCCESS,
        WARNING,
        ERROR
    }

    internal data class Model(
        val title: String,
        val subtitle: String,
        val style: Style,
        // New value per show() call so an identical message still restarts the timer.
        val timestamp: Long = System.currentTimeMillis()
    )

    private val _current = MutableStateFlow<Model?>(null)
    internal val current: StateFlow<Model?> = _current.asStateFlow()

    internal const val VISIBLE_DURATION_MS = 2_500L

    fun show(
        title: String,
        subtitle: String = "",
        style: Style = Style.SUCCESS
    ) {
        _current.value = Model(title = title, subtitle = subtitle, style = style)
    }

    internal fun dismiss() {
        _current.value = null
    }
}

// MARK: - Style tokens (mirror iOS)

internal val AppSnackBar.Style.backgroundColor: Color
    get() = when (this) {
        AppSnackBar.Style.SUCCESS -> Palette.greenLight
        AppSnackBar.Style.WARNING -> Palette.cardBgOrange.copy(alpha = 0.18f)
        AppSnackBar.Style.ERROR -> Palette.cardBgRed.copy(alpha = 0.12f)
    }

internal val AppSnackBar.Style.iconColor: Color
    get() = when (this) {
        AppSnackBar.Style.SUCCESS -> Palette.green900
        AppSnackBar.Style.WARNING -> Palette.cardBgOrange
        AppSnackBar.Style.ERROR -> Palette.cardBgRed
    }

internal val AppSnackBar.Style.iconGlyph: String
    get() = when (this) {
        AppSnackBar.Style.SUCCESS -> "✓"
        AppSnackBar.Style.WARNING -> "!"
        AppSnackBar.Style.ERROR -> "✕"
    }

// MARK: - Overlay (mount once at app root)

@Composable
fun AppSnackBarOverlay() {
    val model by AppSnackBar.current.collectAsState()
    val isVisible = model != null

    LaunchedEffect(model?.timestamp) {
        if (model != null) {
            delay(AppSnackBar.VISIBLE_DURATION_MS)
            AppSnackBar.dismiss()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350)),
            exit = slideOutVertically(
                targetOffsetY = { it / 2 },
                animationSpec = tween(250)
            ) + fadeOut(animationSpec = tween(250))
        ) {
            model?.let { current ->
                AppSnackBarView(
                    title = current.title,
                    subtitle = current.subtitle,
                    style = current.style,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

// MARK: - Card view

@Composable
internal fun AppSnackBarView(
    title: String,
    subtitle: String,
    style: AppSnackBar.Style,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(style.backgroundColor)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(style.iconColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = style.iconGlyph,
                style = AppTypography.HeadingMd.bold,
                color = Palette.white
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = AppTypography.HeadingMd.bold,
                color = Palette.black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = AppTypography.TextMd.regular,
                    color = Palette.blackMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
