//
//  AppEmptyView.kt
//  AppUIKit
//
//  Created by Huseyn Hasanov on 20.01.26
//

package com.bonjur.designSystem.components.emptyView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.segmentView.CapsuleSegmentedPicker
import com.bonjur.designSystem.components.segmentView.CapsuleSegmentedPickerEnum
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun AppEmptyView(
    model: AppEmptyModel,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Palette.grayQuaternary,
                ambientColor = Palette.grayQuaternary
            )
    ) {
        Surface (
            shape = RoundedCornerShape(16.dp),
            color = Palette.white
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Palette.grayQuaternary
                ) {
                    Icon(
                        painter = model.icon,
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp)
                    )
                }

                // Text
                Text(
                    text = model.text,
                    style = AppTypography.BodyTextSm.medium,
                    color = Palette.blackMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Button
                AppButton(
                    title = model.buttonTitle,
                    model = AppButtonModel(
                        contentSize = ContentSize.Fill,
                        size = AppButtonSize.Small
                    ),
                    modifier = Modifier.padding(horizontal = 40.dp),
                    onClick = onButtonClick
                )
            }
        }

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(1.dp)
        ) {
            val strokeWidth = 1.dp.toPx()
            val dashEffect = PathEffect.dashPathEffect(
                floatArrayOf(6.dp.toPx(), 6.dp.toPx()),
                phase = 0f
            )

            drawRoundRect(
                color = Palette.grayTeritary.copy(alpha = 0.5f),
                topLeft = androidx.compose.ui.geometry.Offset(
                    strokeWidth / 2,
                    strokeWidth / 2
                ),
                size = androidx.compose.ui.geometry.Size(
                    width = size.width - strokeWidth,
                    height = size.height - strokeWidth
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = dashEffect
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppEmptyView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppEmptyView(
            model = AppEmptyModel(
                icon = Images.Icons.twoUsers(),
                text = "There are no clubs for this community yet. Be the pioneer and start the very first one now!",
                buttonTitle = "Create a club +"
            ),
            onButtonClick = {
                println("Button clicked")
            }
        )
    }
}