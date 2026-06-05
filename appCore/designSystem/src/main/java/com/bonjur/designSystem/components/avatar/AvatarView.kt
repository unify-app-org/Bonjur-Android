package com.bonjur.designSystem.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * 88dp rounded avatar with gray placeholder. Mirrors iOS `AvatarView`.
 */
@Composable
fun AvatarView(
    url: String?,
    modifier: Modifier = Modifier,
    size: Int = 88,
    cornerRadius: Int = 20,
    placeholder: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(Palette.grayQuaternary)
            .border(3.dp, Palette.grayTeritary.copy(alpha = 0.3f), RoundedCornerShape(cornerRadius.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNullOrBlank()) {
            placeholder()
        } else {
            CachedAsyncImage(
                url = url,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = placeholder
            )
        }
    }
}
