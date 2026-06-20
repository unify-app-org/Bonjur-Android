//
//  EventsCardView.kt
//  AppUIKit
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.events.presentation.list.components

import CardBackgroundView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.events.presentation.list.models.EventsCardMocks
import com.bonjur.events.presentation.list.models.EventsCardModel

@Composable
fun EventsCardView(
    model: EventsCardModel,
    onButtonTap: () -> Unit,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    showCover: Boolean = true,
    onClubTap: ((Int) -> Unit)? = null
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
            showCover = showCover,
            onClubTap = onClubTap
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
    showCover: Boolean,
    onClubTap: ((Int) -> Unit)?
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
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

            HostClubChip(
                model = model,
                onClubTap = onClubTap
            )

            MetaRow(
                time = model.time,
                location = model.location
            )
        }
    }
}

@Composable
private fun HostClubChip(
    model: EventsCardModel,
    onClubTap: ((Int) -> Unit)?
) {
    Surface(
        shape = CircleShape,
        color = Palette.greenLight,
        border = BorderStroke(
            width = 1.dp,
            color = Palette.secondary
        ),
        modifier = Modifier.pressTapModifier {
            onClubTap?.invoke(model.club.id)
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                painter = Images.Icons.twoUsers(),
                contentDescription = null,
                tint = Palette.green900,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = model.club.name,
                style = AppTypography.TextSm.medium,
                color = Palette.green900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (onClubTap != null) {
                Icon(
                    painter = Images.Icons.chevronRight(),
                    contentDescription = null,
                    tint = Palette.green900,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

@Composable
internal fun MetaRow(
    time: String?,
    location: String?
) {
    if (time != null || location != null) {
        Text(
            text = listOfNotNull(time, location).joinToString(" · "),
            style = AppTypography.TextSm.medium,
            color = Palette.graySecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
                    Image(
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
internal fun DateBadge(
    day: String,
    month: String,
    backgroundColor: Color = Color.White
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = month,
                style = AppTypography.CaptionMd.medium,
                color = Palette.cardBgRed
            )
            Text(
                text = day,
                style = AppTypography.HeadingXL.bold,
                color = Palette.blackHigh
            )
        }
    }
}

@Composable
private fun TopChipsView(model: EventsCardModel) {
    val isPrivate = model.accessType == AppUIEntities.AccessType.PRIVATE

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DateBadge(
            day = model.dateDay,
            month = model.dateMonth
        )
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
                style = AppTypography.TextSm.regular,
                color = if (isPrivate) Palette.blackHigh else Palette.whiteHigh,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        
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
        
        // Action: status label for settled states, button for actionable ones
        when (model.requestType) {
            AppUIEntities.RequestType.JOINED -> StatusLabel(
                text = "✓ Participating",
                foreground = Palette.green900,
                background = Palette.greenLight,
                borderColor = Palette.secondary
            )
            AppUIEntities.RequestType.PENDING -> StatusLabel(
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
internal fun StatusLabel(
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
fun PreviewEventsCardView() {
    LazyColumn(
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