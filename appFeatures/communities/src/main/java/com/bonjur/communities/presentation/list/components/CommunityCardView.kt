//
//  CommunityCardView.kt
//  AppUIKit
//
//  Created by Huseyn Hasanov on 16.01.26
//

package com.bonjur.communities.presentation.list.components

import CardBackgroundView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bonjur.communities.presentation.list.model.CommunityCardMocks
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.pressTapModifier
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun CommunityCardView(
    model: CommunityCardModel,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    CardBackgroundView(
        modifier = modifier.pressTapModifier() { onTap() },
        cardType = AppUIEntities.ActivityType.COMMUNITY,
        bgColorType = model.bgType
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            TopView(model = model)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                MembersView(model = model)
            }
        }
    }
}

@Composable
private fun TopView(model: CommunityCardModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LogoView(logoUrl = model.logoURL)
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = model.name,
                style = AppTypography.TitleMd.bold,
                color = model.bgType.foregroundColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            
            Text(
                text = model.subTitle,
                style = AppTypography.TextMd.regular,
                color = model.bgType.foregroundColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
        
        // View all button
        Surface(
            shape = RoundedCornerShape(50),
            color = model.bgType.arrowBgColor
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "view all",
                    style = AppTypography.TextMd.medium,
                    color = model.bgType.arrowTint
                )
                
                Icon(
                    painter = Images.Icons.arrowLeft01(),
                    contentDescription = "View all",
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
private fun LogoView(logoUrl: String) {
    CachedAsyncImage(
        url = logoUrl,
        contentDescription = "Community Logo",
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
                    style = AppTypography.TitleSm.regular,
                    color = Palette.black
                )
            }
        }
    ) { imageBitmap ->
        Image(
            bitmap = imageBitmap,
            contentDescription = "Community Logo",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun MembersView(model: CommunityCardModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${model.memberCount} members",
            style = AppTypography.TextMd.regular,
            color = model.bgType.foregroundColor,
            textAlign = TextAlign.End
        )
        
        Box {
            model.members.take(3).forEachIndexed { index, member ->
                MemberAvatar(
                    profileImage = member.profileImage,
                    memberId = member.id,
                    index = index
                )
            }
        }
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
fun PreviewCommunityCardView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(CommunityCardMocks.mock.size) { index ->
            CommunityCardView(
                model = CommunityCardMocks.mock[index],
                onTap = {
                    println("Card tapped: ${CommunityCardMocks.mock[index].name}")
                }
            )
        }
    }
}