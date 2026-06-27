package com.bonjur.designSystem.components.loading

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global blocking loading overlay. Presents above everything via [AppLoadingOverlay]
 * (mounted once next to AppSnackBarOverlay), so it can be driven from anywhere
 * (view models / side-effect handlers) without a view reference. Mirrors iOS `AppLoadingUI`.
 *
 * Uses a reference count so overlapping operations don't dismiss each other early:
 * the overlay is visible while at least one [show] has no matching [dismiss].
 *
 *     AppLoadingUI.show()
 *     try { ... } finally { AppLoadingUI.dismiss() }
 */
object AppLoadingUI {

    private val _count = MutableStateFlow(0)
    private val _isVisible = MutableStateFlow(false)
    internal val visible: StateFlow<Boolean> = _isVisible.asStateFlow()

    fun show() {
        _count.value += 1
        _isVisible.value = _count.value > 0
    }

    fun dismiss() {
        _count.value = maxOf(0, _count.value - 1)
        _isVisible.value = _count.value > 0
    }

    /** Force-clears any pending loading (e.g. on a hard navigation reset). */
    fun reset() {
        _count.value = 0
        _isVisible.value = false
    }
}

// MARK: - Overlay (mount once at app root)

@Composable
fun AppLoadingOverlay() {
    val isVisible by AppLoadingUI.visible.collectAsState()

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(150)),
        exit = fadeOut(animationSpec = tween(150))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Palette.black.copy(alpha = 0.25f))
                // Swallow all touches while loading so the UI underneath stays inert.
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent().changes.forEach { it.consume() }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Palette.white),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = Palette.appBlue,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
