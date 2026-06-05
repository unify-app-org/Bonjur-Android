package com.bonjur.clubs.presentation.create.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.create.models.ClubCreateAction
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.clubs.presentation.create.models.ClubCreateViewState
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.fieldSchema.FieldSchemaRouter
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun ClubCreateView(
    store: FeatureStore<ClubCreateViewState, ClubCreateAction, ClubCreateSideEffect>
) {
    val state = store.state
    val coverHeight = (LocalConfiguration.current.screenHeightDp / 5).dp

    val coverPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> store.send(ClubCreateAction.CoverSelected(uri?.toString())) }

    val logoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> store.send(ClubCreateAction.LogoSelected(uri?.toString())) }

    val imagesOnly = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CoverHeader(
                coverUri = state.coverUri ?: state.existingCoverUrl,
                height = coverHeight,
                onTap = { coverPicker.launch(imagesOnly) },
                onBack = { store.send(ClubCreateAction.BackTapped) }
            )

            LogoRow(
                logoUri = state.logoUri ?: state.existingLogoUrl,
                onTap = { logoPicker.launch(imagesOnly) }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Fields marked with * are required.",
                    style = AppTypography.BodyTextMd.regular,
                    color = Palette.appBlue
                )

                state.schema.forEach { field ->
                    FieldSchemaRouter(
                        field = field,
                        values = state.values,
                        onChange = { id, value ->
                            store.send(ClubCreateAction.FieldChanged(id, value))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        AppButton(
            title = "Continue",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = { store.send(ClubCreateAction.ContinueTapped) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            enabled = state.isValid
        )
    }
}

@Composable
private fun CoverHeader(
    coverUri: String?,
    height: androidx.compose.ui.unit.Dp,
    onTap: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Palette.primary)
            .clickable { onTap() }
    ) {
        if (coverUri != null) {
            AsyncImage(
                model = coverUri,
                contentDescription = "Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(12.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Palette.whiteHigh.copy(alpha = 0.9f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = Images.Icons.arrowLeft01(),
                contentDescription = "Back",
                tint = Palette.blackHigh,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun LogoRow(
    logoUri: String?,
    onTap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-44).dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Palette.grayQuaternary)
                    .border(3.dp, Palette.grayTeritary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .clickable { onTap() },
                contentAlignment = Alignment.Center
            ) {
                if (logoUri != null) {
                    AsyncImage(
                        model = logoUri,
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp))
                    )
                } else {
                    Icon(
                        painter = Images.Icons.user(),
                        contentDescription = null,
                        tint = Palette.blackMedium,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            CameraBadge(onTap = onTap)
        }
    }
}

@Composable
private fun CameraBadge(onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Palette.grayQuaternary)
            .border(2.dp, Palette.whiteHigh, CircleShape)
            .clickable { onTap() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = Images.Icons.camera(),
            contentDescription = "Pick image",
            tint = Palette.blackMedium,
            modifier = Modifier.size(18.dp)
        )
    }
}
