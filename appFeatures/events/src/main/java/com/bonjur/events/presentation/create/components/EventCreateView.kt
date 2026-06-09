package com.bonjur.events.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.categorieChips.SelectCategoryView
import com.bonjur.designSystem.components.fieldSchema.FieldSchemaRouter
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.events.presentation.create.models.EventCreateAction
import com.bonjur.events.presentation.create.models.EventCreateSideEffect
import com.bonjur.events.presentation.create.models.EventCreateViewState
import com.bonjur.events.presentation.create.models.EventSelectableClub

@Composable
fun EventCreateView(
    store: FeatureStore<EventCreateViewState, EventCreateAction, EventCreateSideEffect>
) {
    val state = store.state
    val coverHeight = (LocalConfiguration.current.screenHeightDp / 4).dp

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            CoverHeader(coverUrl = state.coverUrl, height = coverHeight)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = state.topTitle, style = AppTypography.TitleL.extraBold)

                Text(
                    text = "Fields marked with * are required.",
                    style = AppTypography.BodyTextMd.regular,
                    color = Palette.appBlue
                )

                ClubSelector(
                    selectedName = state.selectedClub?.clubName,
                    onTap = { store.send(EventCreateAction.SelectClubTapped) }
                )

                state.schema.forEach { field ->
                    FieldSchemaRouter(
                        field = field,
                        values = state.values,
                        onChange = { id, value ->
                            store.send(EventCreateAction.FieldChanged(id, value))
                        },
                        onAddCategory = { store.send(EventCreateAction.AddCategoryTapped) },
                        onRemoveCategory = { id -> store.send(EventCreateAction.RemoveCategory(id)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        AppButton(
            title = "Continue",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = { store.send(EventCreateAction.ContinueTapped) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            enabled = state.isValid
        )
    }

    if (state.showCategoryPicker) {
        AppBottomSheet(
            onDismiss = { store.send(EventCreateAction.DismissCategoryPicker) },
            showDragHandle = false,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            SelectCategoryView(
                sections = state.categorySections,
                onToggle = { id -> store.send(EventCreateAction.CategoryToggled(id)) },
                onDone = { store.send(EventCreateAction.CategoryPickerDone) },
                onClose = { store.send(EventCreateAction.DismissCategoryPicker) }
            )
        }
    }

    if (state.showClubPicker) {
        AppBottomSheet(
            onDismiss = { store.send(EventCreateAction.DismissClubPicker) },
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            ClubPicker(
                clubs = state.clubs,
                selectedClubId = state.selectedClubId,
                onSelect = { store.send(EventCreateAction.SelectClub(it)) }
            )
        }
    }
}

@Composable
private fun CoverHeader(coverUrl: String?, height: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Palette.grayQuaternary),
        contentAlignment = Alignment.Center
    ) {
        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = "Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.25f)))
        }

        Text(
            text = "This event will use the official club cover photo.",
            style = AppTypography.BodyTextSm.regular,
            color = if (coverUrl != null) Palette.white else Palette.blackMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun ClubSelector(selectedName: String?, onTap: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "Club", style = AppTypography.HeadingMd.medium, color = Palette.blackHigh)
            Text(text = "*", style = AppTypography.HeadingMd.medium, color = Palette.green900)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(Palette.white)
                .border(0.5.dp, Palette.graySecondary, CircleShape)
                .clickable { onTap() }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedName ?: "Select club",
                style = AppTypography.BodyTextMd.regular,
                color = if (selectedName == null) Palette.grayPrimary else Palette.blackHigh,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = Images.Icons.chevronDown02(),
                contentDescription = null,
                tint = Palette.blackHigh
            )
        }
    }
}

@Composable
private fun ClubPicker(
    clubs: List<EventSelectableClub>,
    selectedClubId: Int?,
    onSelect: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "Select club",
            style = AppTypography.HeadingMd.medium,
            color = Palette.blackHigh,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        )
        clubs.forEach { club ->
            val isSelected = club.clubId == selectedClubId
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(club.clubId) }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (club.profileUrl != null) {
                    AsyncImage(
                        model = club.profileUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Palette.grayQuaternary)
                    )
                } else {
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Palette.grayQuaternary))
                }
                Text(
                    text = club.clubName,
                    style = AppTypography.BodyTextMd.regular,
                    color = Palette.blackHigh,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(
                            2.dp,
                            if (isSelected) Palette.green900 else Palette.grayTeritary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(modifier = Modifier.size(10.dp).background(Palette.green900, CircleShape))
                    }
                }
            }
        }
    }
}
