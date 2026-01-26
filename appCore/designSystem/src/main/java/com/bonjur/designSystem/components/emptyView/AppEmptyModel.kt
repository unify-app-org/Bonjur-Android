//
//  AppEmptyModel.kt
//  AppCore
//
//  Created by Huseyn Hasanov on 20.01.26
//

package com.bonjur.designSystem.components.emptyView

import androidx.compose.ui.graphics.painter.Painter

data class AppEmptyModel(
    val icon: Painter,
    val text: String,
    val buttonTitle: String
)