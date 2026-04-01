package com.bonjur.profile.presentation.components

import CardBackgroundView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.pressTapModifier
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.profile.presentation.models.UserCardModel

@Composable
fun UserCardView(
    model: UserCardModel,
    onTap: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)

    Surface(
        shape = shape,
        color = if (model.backgroundCover != null) Color.Transparent else Palette.white,
        border = BorderStroke(0.5.dp, Palette.grayTeritary),
        modifier = Modifier
            .pressTapModifier() { onTap() }
            .fillMaxWidth()
            .clip(shape)
    ) {
        val contentModifier = Modifier
            .fillMaxWidth()

        if (model.backgroundCover != null) {
            CardBackgroundView(
                bgColorType = model.backgroundCover,
                modifier = Modifier.fillMaxWidth()
            ) {
                UserInfoContent(model = model, modifier = contentModifier)
            }
        } else {
            UserInfoContent(model = model, modifier = contentModifier)
        }
    }
}

@Composable
private fun UserInfoContent(
    model: UserCardModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar + name/specialty + community badge
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            UserAvatarImage(imageUrl = model.imageUrl)

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Name and specialty
                Column {
                    Text(
                        text = model.nameSurname,
                        style = AppTypography.HeadingXL.bold,
                        color = Palette.blackHigh,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = model.speciality,
                        style = AppTypography.TextMd.medium,
                        color = Palette.blackHigh,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Community badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (model.backgroundCover == null) Palette.primary else Palette.whiteMedium,
                    border = BorderStroke(0.5.dp, Palette.grayTeritary)
                ) {
                    Text(
                        text = model.community,
                        style = AppTypography.TextMd.bold,
                        color = Palette.blackHigh,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Course / Degree / Entry year
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AdditionalInfoItem(title = "course", subtitle = model.course)
            AdditionalInfoItem(title = "degree", subtitle = model.degree)
            AdditionalInfoItem(title = "entry", subtitle = model.entryYear)
        }

        // Email footer
        EmailView(
            email = model.email,
            bgType = model.backgroundCover,
            foregroundColor = Palette.blackHigh
        )
    }
}

@Composable
private fun UserAvatarImage(imageUrl: String?) {
    CachedAsyncImage(
        url = imageUrl,
        modifier = Modifier
            .size(88.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(0.5.dp, Palette.blackHigh, RoundedCornerShape(20.dp))
            .background(Palette.grayQuaternary),
        contentScale = ContentScale.Crop,
        placeholder = {
            Icon(
                painter = Images.Icons.user(),
                contentDescription = null,
                tint = Palette.blackMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Palette.grayQuaternary)
                    .size(88.dp)
                    .padding(22.dp)
            )
        },
        error = {
            Icon(
                painter = Images.Icons.user(),
                contentDescription = null,
                tint = Palette.blackMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Palette.grayQuaternary)
                    .size(88.dp)
                    .padding(22.dp)
            )
        }
    )
}

@Composable
private fun AdditionalInfoItem(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            style = AppTypography.TextSm.regular,
            color = Palette.blackHigh
        )
        Text(
            text = subtitle,
            style = AppTypography.TextMd.medium,
            color = Palette.blackHigh
        )
    }
}

@Composable
private fun EmailView(
    email: String,
    bgType: AppUIEntities.BackgroundType?,
    foregroundColor: Color
) {
    Column(
        modifier = Modifier
            .background(if (bgType == null) Palette.primary else Color.Transparent)
            .fillMaxWidth()
    ) {
        Divider(color = foregroundColor.copy(alpha = 0.3f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = Images.Icons.user(),
                contentDescription = null,
                tint = foregroundColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = email,
                style = AppTypography.TextSm.regular,
                color = foregroundColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}