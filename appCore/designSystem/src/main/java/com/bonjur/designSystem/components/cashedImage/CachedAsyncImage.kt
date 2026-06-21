//
//  CachedAsyncImage.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 24.01.26
//

package com.bonjur.designSystem.components.cashedImage

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

/// Async image with memory + disk caching.
///
/// Coil is the only place it is imported — call sites keep the same
/// `placeholder` / `error` / `content` API and never reference Coil directly.
/// The minio→baseURL host rewrite is applied once, app-side, via a Coil
/// `Interceptor` registered on the singleton `ImageLoader`.
///
/// NOTE: the request uses `Size.ORIGINAL`. Because the success branch renders
/// the decoded bitmap itself (not the painter), the painter is never drawn, so
/// Coil's default draw-time size resolver would otherwise stall forever. An
/// explicit size makes the request resolve immediately.
@Composable
fun CachedAsyncImage(
    url: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    placeholder: @Composable () -> Unit = {},
    error: @Composable () -> Unit = {},
    content: @Composable (Image: ImageBitmap) -> Unit = { image ->
        Image(
            bitmap = image,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
) {
    val context = LocalContext.current
    val request = remember(url) {
        ImageRequest.Builder(context)
            .data(url)
            .size(Size.ORIGINAL)
            .build()
    }
    val painter = rememberAsyncImagePainter(
        model = request,
        contentScale = contentScale
    )

    when (val state = painter.state) {
        is AsyncImagePainter.State.Success -> {
            val imageBitmap = remember(state.result) {
                state.result.drawable.toBitmap().asImageBitmap()
            }
            content(imageBitmap)
        }
        is AsyncImagePainter.State.Error -> error()
        else -> placeholder()
    }
}
