package com.bonjur.designSystem.components.imagePreview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.designsystem.R
import kotlin.math.abs

private const val MAX_SCALE = 4f
private const val DOUBLE_TAP_SCALE = 2.5f
private const val DISMISS_DISTANCE = 300f

/**
 * Long-press-to-preview wrapper for any remote photo (avatars, covers).
 *
 * Mirrors the iOS `.imagePreview(url:)` modifier: the long press is a separate
 * gesture from whatever click the wrapped content already has, so a normal tap
 * still navigates. No-op when [url] is null or blank.
 */
@Composable
fun ImagePreviewable(
    url: String?,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    var isPreviewOpen by remember { mutableStateOf(false) }
    val canPreview = !url.isNullOrBlank()

    Box(
        modifier = modifier.pointerInput(url) {
            if (!canPreview) return@pointerInput
            detectTapGestures(onLongPress = { isPreviewOpen = true })
        },
        contentAlignment = contentAlignment
    ) {
        content()
    }

    if (isPreviewOpen && url != null) {
        ImagePreviewDialog(url = url, onDismiss = { isPreviewOpen = false })
    }
}

/**
 * Full-screen photo viewer: pinch / double-tap to zoom, pan while zoomed, drag
 * down to dismiss. Mirrors the iOS `ImagePreviewView`.
 */
@Composable
fun ImagePreviewDialog(
    url: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var dragDown by remember { mutableFloatStateOf(0f) }

    val isZoomed = scale > 1.01f
    // Fades the backdrop as the drag-to-dismiss travels, so the photo visibly
    // detaches from the screen underneath it.
    val backdropAlpha by animateFloatAsState(
        targetValue = 1f - (abs(dragDown) / (DISMISS_DISTANCE * 2f)).coerceAtMost(0.6f),
        label = "imagePreviewBackdrop"
    )

    fun resetZoom() {
        scale = 1f
        offsetX = 0f
        offsetY = 0f
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backdropAlpha))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, MAX_SCALE)
                        if (scale > 1.01f) {
                            offsetX += pan.x
                            offsetY += pan.y
                        } else {
                            resetZoom()
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (scale > 1.01f) resetZoom() else scale = DOUBLE_TAP_SCALE
                        }
                    )
                }
                .pointerInput(Unit) {
                    // At 1x a vertical drag arms dismiss; while zoomed the
                    // transform gesture above already handles panning.
                    detectDragGestures(
                        onDragEnd = {
                            if (abs(dragDown) > DISMISS_DISTANCE) onDismiss() else dragDown = 0f
                        },
                        onDragCancel = { dragDown = 0f }
                    ) { change, dragAmount ->
                        if (scale <= 1.01f) {
                            dragDown += dragAmount.y
                            change.consume()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            CachedAsyncImage(
                url = url,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offsetX
                        translationY = offsetY + dragDown
                    },
                contentScale = ContentScale.Fit,
                placeholder = { CircularProgressIndicator(color = Palette.white) },
                error = { CircularProgressIndicator(color = Palette.white) }
            )

            if (!isZoomed) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                ) {
                    Icon(
                        painter = Images.Icons.xmark(),
                        contentDescription = LanguageManager.string(R.string.common_close),
                        tint = Palette.white,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
