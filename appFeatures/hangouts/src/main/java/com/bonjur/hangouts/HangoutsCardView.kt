//
//  HangoutsCardView.kt
//  AppUIKit
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.hangouts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Private/Public chip
            Surface(
                shape = CircleShape,
                color = if (isPrivate) Palette.white else Palette.blackHigh,
                border = androidx.compose.foundation.BorderStroke(
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
                border = androidx.compose.foundation.BorderStroke(
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
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
        
        // Action button
        if (model.requestType != AppUIEntities.RequestType.JOINED) {
            AppButton(
                title = model.buttonTitle,
                model = AppButtonModel(
                    contentSize = ContentSize.Fill,
                    size = AppButtonSize.Small
                ),
                enabled = model.requestType != AppUIEntities.RequestType.PENDING,
                onClick = onButtonTap
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHangoutsCardView() {
    androidx.compose.foundation.lazy.LazyColumn(
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