//
//  CachedAsyncImage.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 24.01.26
//

package com.bonjur.designSystem.components.cashedImage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun CachedAsyncImage(
    url: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    placeholder: @Composable () -> Unit = {},
    error: @Composable () -> Unit = {},
    content: @Composable (Image: androidx.compose.ui.graphics.ImageBitmap) -> Unit = { image ->
        Image(
            bitmap = image,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    
    val opacity by animateFloatAsState(
        targetValue = if (bitmap != null) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "image opacity"
    )
    
    // Check cache on initial load
    LaunchedEffect(url) {
        if (url.isNullOrBlank()) {
            isLoading = false
            hasError = true
            return@LaunchedEffect
        }
        
        // Check cache first
        val cached = ImageCache.get(url)
        if (cached != null) {
            bitmap = cached
            isLoading = false
            return@LaunchedEffect
        }
        
        // Load from network
        loadImage(
            url = url,
            onSuccess = { loadedBitmap ->
                bitmap = loadedBitmap
                isLoading = false
                hasError = false
            },
            onError = {
                isLoading = false
                hasError = true
            }
        )
    }
    
    when {
        bitmap != null -> {
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.then(
                    androidx.compose.ui.Modifier.alpha(opacity)
                )
            ) {
                content(bitmap!!.asImageBitmap())
            }
        }
        hasError -> error()
        isLoading -> placeholder()
    }
}

private suspend fun loadImage(
    url: String,
    onSuccess: (Bitmap) -> Unit,
    onError: () -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            // Check cache again in case it was loaded while switching context
            val cached = ImageCache.get(url)
            if (cached != null) {
                withContext(Dispatchers.Main) {
                    onSuccess(cached)
                }
                return@withContext
            }
            
            // Download image
            val connection = URL(url).openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            
            if (bitmap != null) {
                // Cache the bitmap
                ImageCache.put(url, bitmap)
                
                withContext(Dispatchers.Main) {
                    onSuccess(bitmap)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onError()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onError()
            }
        }
    }
}