//
//  HangoutsCardView.kt
//  AppUIKit
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.hangouts.presentation.list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.pressTapModifier
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.hangouts.presentation.list.model.HangoutsCardMocks
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel

@Composable
fun HangoutsCardView(
    model: HangoutsCardModel,
    onButtonTap: () -> Unit,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .pressTapModifier() { onTap() }
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = Palette.grayTeritary,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopView(model = model)
        BottomView(
            model = model,
            onButtonTap = onButtonTap
        )
    }
}

@Composable
private fun TopView(model: HangoutsCardModel) {
    val isPrivate = model.accessType == AppUIEntities.AccessType.PRIVATE
    
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Chips row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date badge (gray variant — card background is white)
            if (model.dateDay != null && model.dateMonth != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Palette.grayQuaternary
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = model.dateMonth,
                            style = AppTypography.CaptionMd.medium,
                            color = Palette.cardBgRed
                        )
                        Text(
                            text = model.dateDay,
                            style = AppTypography.HeadingXL.bold,
                            color = Palette.blackHigh
                        )
                    }
                }
            }

            // Private/Public chip
            Surface(
                shape = CircleShape,
                color = if (isPrivate) Palette.white else Palette.blackHigh,
                border = BorderStroke(
                    width = 1.dp,
                    color = Palette.blackHigh
                )
            ) {
                Text(
                    text = if (isPrivate) "Private" else "Public",
                    style = AppTypography.TextSm.medium,
                    color = if (isPrivate) Palette.blackHigh else Palette.whiteHigh,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            
            // Member count chip
            Surface(
                shape = CircleShape,
                color = Palette.white,
                border = BorderStroke(
                    width = 1.dp,
                    color = Palette.blackHigh
                )
            ) {
                Text(
                    text = model.memberCountText,
                    style = AppTypography.TextSm.medium,
                    color = Palette.blackHigh,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
        
        // Title and description
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = model.name,
                style = AppTypography.HeadingXL.bold,
                color = Palette.blackHigh,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            
            Text(
                text = model.description,
                style = AppTypography.TextL.regular,
                color = Palette.blackHigh,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            if (model.time != null || model.location != null) {
                Text(
                    text = listOfNotNull(model.time, model.location).joinToString(" · "),
                    style = AppTypography.TextSm.medium,
                    color = Palette.graySecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BottomView(
    model: HangoutsCardModel,
    onButtonTap: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tags
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            model.tags.forEach { tag ->
                Surface(
                    shape = CircleShape,
                    color = Palette.grayQuaternary
                ) {
                    Text(
                        text = "#${tag.title.lowercase()}",
                        style = AppTypography.TextSm.regular,
                        color = Palette.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        
        // Action: status label for settled states, button for actionable ones
        when (model.requestType) {
            AppUIEntities.RequestType.JOINED -> HangoutStatusLabel(
                text = "✓ Going",
                foreground = Palette.green900,
                background = Palette.greenLight,
                borderColor = Palette.secondary
            )
            AppUIEntities.RequestType.PENDING -> HangoutStatusLabel(
                text = "Request sent",
                foreground = Palette.graySecondary,
                background = Palette.grayQuaternary,
                borderColor = Palette.grayTeritary
            )
            else -> AppButton(
                title = model.buttonTitle,
                model = AppButtonModel(
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Small
                ),
                onClick = onButtonTap
            )
        }
    }
}

@Composable
private fun HangoutStatusLabel(
    text: String,
    foreground: Color,
    background: Color,
    borderColor: Color
) {
    Surface(
        shape = CircleShape,
        color = background,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = AppTypography.TextMd.medium,
            color = foreground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHangoutsCardView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(HangoutsCardMocks.previewMock.size) { index ->
            HangoutsCardView(
                model = HangoutsCardMocks.previewMock[index],
                onButtonTap = {
                    println("Button tapped: ${HangoutsCardMocks.previewMock[index].name}")
                },
                onTap = {
                    println("Card tapped: ${HangoutsCardMocks.previewMock[index].name}")
                }
            )
        }
    }
}