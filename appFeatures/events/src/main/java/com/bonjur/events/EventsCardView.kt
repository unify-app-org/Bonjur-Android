//
//  EventsCardView.kt
//  AppUIKit
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.events

import CardBackgroundView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.pressTapModifier
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun EventsCardView(
    model: EventsCardModel,
    onButtonTap: () -> Unit,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    showCover: Boolean = true
) {
    Column(
        modifier = modifier
            .pressTapModifier { onTap() }
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = Palette.grayTeritary,
                shape = RoundedCornerShape(20.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopView(
            model = model,
            showCover = showCover
        )
        BottomView(
            model = model,
            onButtonTap = onButtonTap
        )
    }
}

@Composable
private fun TopView(
    model: EventsCardModel,
    showCover: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CoverView(
            model = model,
            showCover = showCover
        )
        
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
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
                text = model.club.name,
                style = AppTypography.TextMd.regular,
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
private fun CoverView(
    model: EventsCardModel,
    showCover: Boolean
) {
    if (showCover) {
        CardBackgroundView(
            cardType = AppUIEntities.ActivityType.CLUBS,
            bgColorType = model.bgType,
            cornerRadius = 0.dp
        ) {
            Box(
                modifier = Modifier.height(103.dp)
            ) {
                // Cover image
                CachedAsyncImage(
                    url = model.coverImageURL,
                    contentDescription = "Event Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(103.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(103.dp)
                                .background(Color.Transparent)
                        )
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(103.dp)
                                .background(Color.Transparent)
                        )
                    }
                ) { imageBitmap ->
                    androidx.compose.foundation.Image(
                        bitmap = imageBitmap,
                        contentDescription = "Event Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(103.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                
                // Top chips overlay
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    TopChipsView(model = model)
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            TopChipsView(model = model)
        }
    }
}

@Composable
private fun TopChipsView(model: EventsCardModel) {
    val isPrivate = model.accessType == AppUIEntities.AccessType.PRIVATE
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
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
                style = AppTypography.TextSm.regular,
                color = if (isPrivate) Palette.blackHigh else Palette.whiteHigh,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        
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
                style = AppTypography.TextSm.regular,
                color = Palette.blackHigh,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BottomView(
    model: EventsCardModel,
    onButtonTap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tags
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
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
fun PreviewEventsCardView() {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(EventsCardMocks.previewMock.size) { index ->
            EventsCardView(
                model = EventsCardMocks.previewMock[index],
                onButtonTap = {
                    println("Button tapped: ${EventsCardMocks.previewMock[index].name}")
                },
                onTap = {
                    println("Card tapped: ${EventsCardMocks.previewMock[index].name}")
                }
            )
        }
    }
}