//
//  ClubCardView.kt
//  AppFoundation
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.clubs.presentation.list.components

import CardBackgroundView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bonjur.clubs.presentation.list.models.ClubCardMocks
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.pressTapModifier
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import kotlin.collections.take

@Composable
fun ClubCardView(
    model: ClubCardModel,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    CardBackgroundView(
        modifier = modifier.pressTapModifier { onTap() },
        cardType = AppUIEntities.ActivityType.CLUBS,
        bgColorType = model.bgType
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(27.dp)
        ) {
            TopLeftView(model = model)
            BottomView(model = model)
        }
    }
}

@Composable
private fun TopLeftView(model: ClubCardModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LogoImage(logoUrl = model.logoURL)
        
        Text(
            text = model.name,
            style = AppTypography.TitleMd.bold,
            color = model.bgType.foregroundColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        
        Text(
            text = model.communityName,
            style = AppTypography.TextMd.regular,
            color = model.bgType.foregroundColor,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun LogoImage(logoUrl: String) {
    CachedAsyncImage(
        url = logoUrl,
        contentDescription = "Club Logo",
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        placeholder = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Palette.white, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Palette.black
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Palette.white, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🤙",
                    fontSize = 20.sp,
                    color = Palette.black
                )
            }
        }
    ) { imageBitmap ->
        Image(
            bitmap = imageBitmap,
            contentDescription = "Club Logo",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun BottomView(model: ClubCardModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MembersView(
            members = model.members,
            memberCount = model.memberCount,
            foregroundColor = model.bgType.foregroundColor
        )
        
        Spacer(modifier = Modifier.width(20.dp))
        
        // Arrow icon
        Surface(
            shape = CircleShape,
            color = model.bgType.arrowBgColor,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = Images.Icons.arrowLeft01(),
                    contentDescription = "Navigate",
                    tint = model.bgType.arrowTint,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(135f)
                )
            }
        }
    }
}

@Composable
private fun MembersView(
    members: List<AppUIEntities.Member>,
    memberCount: Int,
    foregroundColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Overlapping member avatars
        Box {
            members.take(3).forEachIndexed { index, member ->
                MemberAvatar(
                    profileImage = member.profileImage,
                    memberId = member.id,
                    index = index
                )
            }
        }

        Spacer(modifier = Modifier.width((members.size.coerceAtMost(3) * 6).dp))

        Text(
            text = "$memberCount members",
            style = AppTypography.TextMd.regular,
            color = foregroundColor,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun MemberAvatar(
    profileImage: String?,
    memberId: Int,
    index: Int
) {
    Box(
        modifier = Modifier
            .offset(x = (index * 14).dp)
            .zIndex((3 - index).toFloat())
    ) {
        CachedAsyncImage(
            url = profileImage,
            contentDescription = "Member $memberId",
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Palette.grayQuaternary, CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = {
                memberPlaceHolder()
            },
            error = {
                memberPlaceHolder()
            }
        ) { imageBitmap ->
            Image(
                bitmap = imageBitmap,
                contentDescription = "Member $memberId",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun memberPlaceHolder() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(Palette.grayQuaternary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = Images.Icons.user(),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Palette.black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClubCardView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(ClubCardMocks.previewData.size) { index ->
            ClubCardView(
                model = ClubCardMocks.previewData[index],
                onTap = {
                    println("Tapped on ${ClubCardMocks.previewData[index].name}")
                }
            )
        }
    }
}