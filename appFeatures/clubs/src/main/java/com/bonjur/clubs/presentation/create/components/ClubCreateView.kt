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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.clubs.presentation.create.models.ClubCreateAction
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.clubs.presentation.create.models.ClubCreateViewState
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.categorieChips.SelectCategoryView
import com.bonjur.designSystem.components.topBar.AppTopBar
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

    val scrollState = rememberScrollState()
    val isScrolled = scrollState.value > 30

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                CoverHeader(
                    coverUri = state.coverUri ?: state.existingCoverUrl,
                    height = coverHeight,
                    onTap = { coverPicker.launch(imagesOnly) }
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
                        onAddCategory = { store.send(ClubCreateAction.AddCategoryTapped) },
                        onRemoveCategory = { id -> store.send(ClubCreateAction.RemoveCategory(id)) },
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

        AppTopBar(
            isScrolled = isScrolled,
            onBack = { store.send(ClubCreateAction.BackTapped) }
        )
    }

    if (state.showCategoryPicker) {
        AppBottomSheet(
            onDismiss = { store.send(ClubCreateAction.DismissCategoryPicker) },
            showDragHandle = false,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            SelectCategoryView(
                sections = state.categorySections,
                onToggle = { id -> store.send(ClubCreateAction.CategoryToggled(id)) },
                onDone = { store.send(ClubCreateAction.CategoryPickerDone) },
                onClose = { store.send(ClubCreateAction.DismissCategoryPicker) }
            )
        }
    }

    if (state.showVerifyPrompt) {
        AppBottomSheet(
            onDismiss = { store.send(ClubCreateAction.DismissVerifyPrompt) }
        ) {
            ClubVerifyPromptView(
                onRequestVerification = { store.send(ClubCreateAction.RequestVerificationTapped) },
                onLater = { store.send(ClubCreateAction.DismissVerifyPrompt) }
            )
        }
    }
}

/**
 * Post-create sheet: a new club is unverified, and verification is the hard gate
 * to creating events in it. Optimistic request flow until the backend verify-request
 * endpoint lands. Mirrors iOS `ClubVerifyPromptView`.
 */
@Composable
private fun ClubVerifyPromptView(
    onRequestVerification: () -> Unit,
    onLater: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Palette.white)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = Images.Icons.selectedCheckBox(),
            contentDescription = null,
            tint = Palette.appBlue,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "Club created 🎉",
            style = AppTypography.TitleMd.extraBold,
            color = Palette.blackHigh,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Your club isn't verified yet. Request verification to earn the verified " +
                "badge and unlock event creation — you can't create events in an unverified club.",
            style = AppTypography.BodyTextSm.regular,
            color = Palette.blackMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                title = "Request verification",
                model = AppButtonModel(contentSize = ContentSize.Fill),
                onClick = onRequestVerification,
                modifier = Modifier.fillMaxWidth()
            )
            AppButton(
                title = "Later",
                model = AppButtonModel(type = ButtonType.Tertiary, contentSize = ContentSize.Fill),
                onClick = onLater,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CoverHeader(
    coverUri: String?,
    height: androidx.compose.ui.unit.Dp,
    onTap: () -> Unit
) {
    // Back button now lives in the overlaid AppTopBar (matches details).
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
