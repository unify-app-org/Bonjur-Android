//
//  CapsuleSegmentedPicker.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 24.01.26
//

package com.bonjur.designSystem.components.segmentView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize

/**
 * Generic segmented picker with capsule shape
 * Best practice: Using a sealed interface for type-safe options
 */
interface SegmentedPickerOption {
    val title: String
    val id: String
}