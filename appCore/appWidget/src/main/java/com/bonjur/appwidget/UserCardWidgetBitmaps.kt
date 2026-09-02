package com.bonjur.appwidget

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.max
import kotlin.math.min

/**
 * Glance has no `Canvas`, and `GlanceModifier.cornerRadius` only works from API 31,
 * so the two shaped pieces of the card (the cover with its circle, and the rounded
 * avatar) are drawn into bitmaps here and handed to Glance as `ImageProvider`s.
 */
internal object UserCardWidgetBitmaps {

    /**
     * The cover: flat colour plus the single off-canvas ring that
     * `CardBackgroundView(cardType = CLUBS)` draws — first circle at
     * `x = width * 0.8`, `y = -width * 0.05`, radius `width * 0.4 / 2`.
     *
     * The app draws a fixed 40dp stroke on a full-width (~360dp) card; the widget
     * can be a third of that, so the stroke is taken as the same *fraction* of the
     * width instead, which keeps the ring looking identical at any widget size.
     */
    fun coverBackground(
        widthPx: Int,
        heightPx: Int,
        color: Color,
        circleStrokeColor: Color = Color.White.copy(alpha = 0.5f)
    ): Bitmap? {
        if (widthPx <= 0 || heightPx <= 0) return null

        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(color.toArgb())

        val width = widthPx.toFloat()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = width * STROKE_WIDTH_RATIO
            this.color = circleStrokeColor.toArgb()
        }
        canvas.drawCircle(width * 0.8f, -width * 0.05f, width * 0.4f / 2f, paint)
        return bitmap
    }

    /**
     * Centre-cropped, rounded, hairline-bordered avatar — the bitmap equivalent of
     * `UserAvatarImage`'s `clip` + `border` modifiers.
     */
    fun roundedAvatar(
        source: Bitmap,
        sidePx: Int,
        cornerRadiusPx: Float,
        borderColor: Color,
        borderWidthPx: Float
    ): Bitmap? {
        if (sidePx <= 0 || source.width <= 0 || source.height <= 0) return null

        val bitmap = Bitmap.createBitmap(sidePx, sidePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bounds = RectF(0f, 0f, sidePx.toFloat(), sidePx.toFloat())

        canvas.save()
        canvas.clipPath(
            android.graphics.Path().apply { addRoundRect(bounds, cornerRadiusPx, cornerRadiusPx, android.graphics.Path.Direction.CW) }
        )
        // Centre-crop: take the largest centred square-ish region of the source that
        // matches the destination aspect (1:1 here) so faces are not squashed.
        val scale = max(sidePx.toFloat() / source.width, sidePx.toFloat() / source.height)
        val cropWidth = min(source.width.toFloat(), sidePx / scale)
        val cropHeight = min(source.height.toFloat(), sidePx / scale)
        val src = Rect(
            ((source.width - cropWidth) / 2f).toInt(),
            ((source.height - cropHeight) / 2f).toInt(),
            ((source.width + cropWidth) / 2f).toInt(),
            ((source.height + cropHeight) / 2f).toInt()
        )
        canvas.drawBitmap(source, src, bounds, Paint(Paint.FILTER_BITMAP_FLAG))
        canvas.restore()

        if (borderWidthPx > 0f) {
            val inset = borderWidthPx / 2f
            canvas.drawRoundRect(
                RectF(inset, inset, sidePx - inset, sidePx - inset),
                cornerRadiusPx,
                cornerRadiusPx,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    strokeWidth = borderWidthPx
                    color = borderColor.toArgb()
                }
            )
        }
        return bitmap
    }

    /** Placeholder tile behind the person icon when there is no avatar yet. */
    fun roundedFill(
        sidePx: Int,
        cornerRadiusPx: Float,
        fill: Color,
        borderColor: Color,
        borderWidthPx: Float
    ): Bitmap? {
        if (sidePx <= 0) return null
        val bitmap = Bitmap.createBitmap(sidePx, sidePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(AndroidColor.TRANSPARENT)
        val bounds = RectF(0f, 0f, sidePx.toFloat(), sidePx.toFloat())
        canvas.drawRoundRect(
            bounds,
            cornerRadiusPx,
            cornerRadiusPx,
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = fill.toArgb() }
        )
        if (borderWidthPx > 0f) {
            val inset = borderWidthPx / 2f
            canvas.drawRoundRect(
                RectF(inset, inset, sidePx - inset, sidePx - inset),
                cornerRadiusPx,
                cornerRadiusPx,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    strokeWidth = borderWidthPx
                    color = borderColor.toArgb()
                }
            )
        }
        return bitmap
    }

    /** 40dp stroke on the ~360dp in-app card. */
    private const val STROKE_WIDTH_RATIO = 40f / 360f
}
