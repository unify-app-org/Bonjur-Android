package com.bonjur.profile.presentation.detail.widget

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.bonjur.appwidget.UserCardWidgetSnapshot
import com.bonjur.appwidget.UserCardWidgetStore
import com.bonjur.appwidget.reloadUserCardWidget
import com.bonjur.profile.presentation.detail.models.UserCardModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max

/**
 * Mirrors the signed-in user's card into the store the home-screen widget reads.
 *
 * Called on every own-profile load and on every cover change; the widget never
 * calls the API, so this is the only way its content ever changes. Mirrors iOS
 * `UserCardWidgetPublisher`.
 */
@Singleton
class UserCardWidgetPublisher @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Outlives the caller's ViewModel so a slow avatar fetch is not cancelled by a pop. */
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun publish(card: UserCardModel, userId: String?) {
        UserCardWidgetStore.save(
            context,
            UserCardWidgetSnapshot(
                userId = userId.orEmpty(),
                nameSurname = card.nameSurname,
                speciality = card.speciality,
                course = card.course,
                community = card.community,
                degree = card.degree,
                entryYear = card.entryYear,
                email = card.email,
                background = card.backgroundCover?.apiValue
            )
        )
        reloadUserCardWidget(context)

        val url = card.imageUrl?.takeIf { it.isNotBlank() } ?: return
        scope.launch {
            val avatar = downscaledAvatar(url) ?: return@launch
            UserCardWidgetStore.saveAvatar(context, avatar)
            reloadUserCardWidget(context)
        }
    }

    suspend fun clear() {
        UserCardWidgetStore.clear(context)
        reloadUserCardWidget(context)
    }

    /**
     * Goes through the singleton Coil loader, so the minio→API host rewrite and the
     * existing disk cache both apply. Downscaled first: the launcher caps how large a
     * bitmap a widget may hand it, and the avatar is only ever drawn at ~52dp.
     */
    private suspend fun downscaledAvatar(url: String): Bitmap? {
        val request = ImageRequest.Builder(context)
            .data(url)
            .size(AVATAR_SIDE)
            // Hardware bitmaps cannot be read back by Canvas or compressed to JPEG.
            .allowHardware(false)
            .build()

        val drawable = runCatching { context.imageLoader.execute(request).drawable }
            .getOrNull() ?: return null
        val bitmap = runCatching { drawable.toBitmap() }.getOrNull() ?: return null

        val longestSide = max(bitmap.width, bitmap.height)
        if (longestSide <= AVATAR_SIDE) return bitmap

        val scale = AVATAR_SIDE.toFloat() / longestSide
        return runCatching {
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * scale).toInt().coerceAtLeast(1),
                (bitmap.height * scale).toInt().coerceAtLeast(1),
                true
            )
        }.getOrNull()
    }

    private companion object {
        const val AVATAR_SIDE = 180
    }
}
