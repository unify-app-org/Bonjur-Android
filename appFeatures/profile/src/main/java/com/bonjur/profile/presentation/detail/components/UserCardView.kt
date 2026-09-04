package com.bonjur.profile.presentation.detail.components

import androidx.compose.ui.res.stringResource
import com.bonjur.profile.R
import CardBackgroundView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.bonjur.profile.presentation.detail.models.UserCardModel

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
                cardType = AppUIEntities.ActivityType.CLUBS,
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
    // The cover decides the text/icon colour, exactly like iOS
    // (`model.backgroundCover?.foregroundColor ?? blackHigh`). Hardcoding blackHigh
    // left the name, the info row and the email unreadable on the darker covers —
    // blue and red both pair with whiteHigh.
    val foreground = model.backgroundCover?.foregroundColor ?: Palette.blackHigh

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
                        color = foreground,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = model.speciality,
                        style = AppTypography.TextMd.medium,
                        color = foreground,
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
            AdditionalInfoItem(
                title = stringResource(R.string.profile_card_course),
                subtitle = model.course,
                foregroundColor = foreground
            )
            AdditionalInfoItem(
                title = stringResource(R.string.profile_card_degree),
                subtitle = model.degree,
                foregroundColor = foreground
            )
            AdditionalInfoItem(
                title = stringResource(R.string.profile_card_entry),
                subtitle = model.entryYear,
                foregroundColor = foreground
            )
        }

        // Email footer
        EmailView(
            email = model.email,
            bgType = model.backgroundCover,
            foregroundColor = foreground
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
private fun AdditionalInfoItem(
    title: String,
    subtitle: String,
    foregroundColor: Color
) {
    Column {
        Text(
            text = title,
            style = AppTypography.TextSm.regular,
            color = foregroundColor
        )
        Text(
            text = subtitle,
            style = AppTypography.TextMd.medium,
            color = foregroundColor
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
            // iOS paints the strip with the cover's own colour and falls back to green
            // on the plain white card. Leaving it transparent let the card's decorative
            // rings run through the strip, which the iOS card never shows.
            .background(bgType?.bgColor ?: Palette.primary)
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
            // FIXME: iOS draws `UIImage.Icons.email` here; the design system has no
            // mail glyph yet (only `appWidget`'s ic_widget_mail), so this shows the
            // person icon.
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