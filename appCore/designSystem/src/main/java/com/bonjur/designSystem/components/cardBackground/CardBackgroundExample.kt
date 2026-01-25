package com.bonjur.designSystem.components.cardBackground

import CardBackgroundView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun CardBackgroundExample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Card Background Examples",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Primary Community Card
        CardBackgroundView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cardType = AppUIEntities.ActivityType.COMMUNITY,
            bgColorType = AppUIEntities.BackgroundType.Primary,
            circleStrokeColor = Palette.white.copy(alpha = 0.5f)
        ) {
            CardContent(
                title = "Primary Community",
                subtitle = "Green background",
                bgType = AppUIEntities.BackgroundType.Primary
            )
        }

        // Secondary Community Card
        CardBackgroundView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cardType = AppUIEntities.ActivityType.COMMUNITY,
            bgColorType = AppUIEntities.BackgroundType.Secondary,
            circleStrokeColor = Palette.white.copy(alpha = 0.5f)
        ) {
            CardContent(
                title = "Secondary Community",
                subtitle = "Blue background",
                bgType = AppUIEntities.BackgroundType.Secondary
            )
        }

        // Tertiary Community Card
        CardBackgroundView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cardType = AppUIEntities.ActivityType.COMMUNITY,
            bgColorType = AppUIEntities.BackgroundType.Tertiary,
            circleStrokeColor = Palette.white.copy(alpha = 0.5f)
        ) {
            CardContent(
                title = "Tertiary Community",
                subtitle = "Purple background",
                bgType = AppUIEntities.BackgroundType.Tertiary
            )
        }

        // Orange Club Card
        CardBackgroundView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cardType = AppUIEntities.ActivityType.CLUBS,
            bgColorType = AppUIEntities.BackgroundType.CustomColor(
                AppUIEntities.ColorType.Orange
            ),
            circleStrokeColor = Palette.white.copy(alpha = 0.5f)
        ) {
            CardContent(
                title = "Orange Club",
                subtitle = "Orange background",
                bgType = AppUIEntities.BackgroundType.CustomColor(
                    AppUIEntities.ColorType.Orange
                )
            )
        }

        // Red Club Card
        CardBackgroundView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cardType = AppUIEntities.ActivityType.CLUBS,
            bgColorType = AppUIEntities.BackgroundType.CustomColor(
                AppUIEntities.ColorType.Red
            ),
            circleStrokeColor = Palette.white.copy(alpha = 0.5f)
        ) {
            CardContent(
                title = "Red Club",
                subtitle = "Red background",
                bgType = AppUIEntities.BackgroundType.CustomColor(
                    AppUIEntities.ColorType.Red
                )
            )
        }

        // Pink Community Card
        CardBackgroundView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cardType = AppUIEntities.ActivityType.COMMUNITY,
            bgColorType = AppUIEntities.BackgroundType.CustomColor(
                AppUIEntities.ColorType.Pink
            ),
            circleStrokeColor = Palette.white.copy(alpha = 0.5f)
        ) {
            CardContent(
                title = "Pink Community",
                subtitle = "Pink background",
                bgType = AppUIEntities.BackgroundType.CustomColor(
                    AppUIEntities.ColorType.Pink
                )
            )
        }

        // Custom Color Card
        CardBackgroundView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cardType = AppUIEntities.ActivityType.COMMUNITY,
            bgColorType = AppUIEntities.BackgroundType.CustomColor(
                AppUIEntities.ColorType.Custom(
                    color = androidx.compose.ui.graphics.Color(0xFF00BCD4), // Cyan
                    foregroundColor = Palette.blackHigh,
                    arrowBgColor = Palette.white,
                    arrowTint = Palette.blackHigh
                )
            ),
            circleStrokeColor = Palette.white.copy(alpha = 0.5f)
        ) {
            CardContent(
                title = "Custom Color",
                subtitle = "Cyan custom background",
                bgType = AppUIEntities.BackgroundType.CustomColor(
                    AppUIEntities.ColorType.Custom(
                        color = androidx.compose.ui.graphics.Color(0xFF00BCD4),
                        foregroundColor = Palette.blackHigh
                    )
                )
            )
        }
    }
}

@Composable
private fun CardContent(
    title: String,
    subtitle: String,
    bgType: AppUIEntities.BackgroundType
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = bgType.foregroundColor
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = bgType.foregroundColor.copy(alpha = 0.7f)
            )

            // Arrow indicator
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = bgType.arrowBgColor,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "→",
                    fontSize = 20.sp,
                    color = bgType.arrowTint
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardBackgroundExample() {
    CardBackgroundExample()
}