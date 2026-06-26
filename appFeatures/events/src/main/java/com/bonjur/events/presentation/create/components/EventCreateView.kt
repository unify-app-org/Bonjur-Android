package com.bonjur.events.presentation.create.components

import CardBackgroundView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.categorieChips.SelectCategoryView
import com.bonjur.designSystem.components.fieldSchema.FieldSchemaRouter
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.events.presentation.create.models.EventCreateAction
import com.bonjur.events.presentation.create.models.EventCreateClubsPhase
import com.bonjur.events.presentation.create.models.EventCreateSideEffect
import com.bonjur.events.presentation.create.models.EventCreateViewState
import com.bonjur.events.presentation.create.models.EventSelectableClub

@Composable
fun EventCreateView(
    store: FeatureStore<EventCreateViewState, EventCreateAction, EventCreateSideEffect>
) {
    val state = store.state
    val scrollState = rememberScrollState()
    val isScrolled by remember { derivedStateOf { scrollState.value > 30 } }

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.clubsPhase) {
            EventCreateClubsPhase.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Palette.green900)
                }

            EventCreateClubsPhase.Failed ->
                StateCardSlot {
                    AppEmptyView(
                        model = AppEmptyModel(
                            icon = Images.Icons.helpCircle(),
                            text = "Couldn't load your clubs",
                            buttonTitle = "Retry"
                        ),
                        onButtonClick = { store.send(EventCreateAction.RetryClubsTapped) }
                    )
                }

            EventCreateClubsPhase.Empty ->
                StateCardSlot {
                    NoEligibleClubsCard(
                        onCreateClub = { store.send(EventCreateAction.CreateClubTapped) },
                        onBrowseClubs = { store.send(EventCreateAction.BrowseClubsTapped) }
                    )
                }

            EventCreateClubsPhase.Loaded ->
                FormBody(store = store, state = state, scrollState = scrollState)
        }

        AppTopBar(
            isScrolled = isScrolled,
            onBack = { store.send(EventCreateAction.BackTapped) }
        )
    }

    // Pickers only make sense once the form (and a club list) is on screen.
    if (state.clubsPhase == EventCreateClubsPhase.Loaded) {
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
}

@Composable
private fun BoxScope.FormBody(
    store: FeatureStore<EventCreateViewState, EventCreateAction, EventCreateSideEffect>,
    state: EventCreateViewState,
    scrollState: androidx.compose.foundation.ScrollState
) {
    val coverHeight = (LocalConfiguration.current.screenHeightDp / 4).dp
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            CoverHeader(
                coverUrl = state.coverUrl,
                background = state.coverBackground,
                height = coverHeight
            )

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
                    selectedClub = state.selectedClub,
                    isDisabled = state.isEdit,
                    onTap = { store.send(EventCreateAction.SelectClubTapped) }
                )

                state.schema.forEach { field ->
                    // Club + event name are immutable once the event exists (mirrors iOS).
                    val isLocked = state.isEdit && field.id == AppFieldSchema.FieldId.EVENT_NAME
                    FieldSchemaRouter(
                        field = field,
                        values = state.values,
                        onChange = { id, value ->
                            if (!isLocked) store.send(EventCreateAction.FieldChanged(id, value))
                        },
                        onAddCategory = { store.send(EventCreateAction.AddCategoryTapped) },
                        onRemoveCategory = { id -> store.send(EventCreateAction.RemoveCategory(id)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (isLocked) 0.5f else 1f)
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
}

/** Centers a state card below the top bar, mirroring the iOS empty/error layout. */
@Composable
private fun StateCardSlot(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * `AppEmptyView` only supports one button; the empty state needs two funnel entries
 * (create / browse), so it's a bespoke card mirroring iOS `EventCreateStateViews`.
 */
@Composable
private fun NoEligibleClubsCard(
    onCreateClub: () -> Unit,
    onBrowseClubs: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.white)
            .border(0.5.dp, Palette.graySecondary, RoundedCornerShape(16.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(shape = RoundedCornerShape(16.dp), color = Palette.grayQuaternary) {
            Icon(
                painter = Images.Icons.twoUsers(),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.padding(12.dp).size(24.dp)
            )
        }
        Text(
            text = "No clubs to post to. To create an event you must be a president, " +
                "vice president, or organizer of a verified club.",
            style = AppTypography.BodyTextSm.medium,
            color = Palette.blackMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        AppButton(
            title = "Create a club",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = onCreateClub,
            modifier = Modifier.fillMaxWidth()
        )
        AppButton(
            title = "Browse clubs",
            model = AppButtonModel(type = ButtonType.Secondary, contentSize = ContentSize.Fill),
            onClick = onBrowseClubs,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CoverHeader(
    coverUrl: String?,
    background: AppUIEntities.BackgroundType,
    height: androidx.compose.ui.unit.Dp
) {
    // Read-only cover: the selected club's official cover photo (mirrors iOS coverContent).
    CardBackgroundView(
        bgColorType = background,
        cornerRadius = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = "Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ClubSelector(
    selectedClub: EventSelectableClub?,
    isDisabled: Boolean,
    onTap: () -> Unit
) {
    val selectedName = selectedClub?.clubName
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.alpha(if (isDisabled) 0.5f else 1f)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = "Choose club", style = AppTypography.HeadingMd.medium, color = Palette.blackHigh)
            Text(text = "*", style = AppTypography.HeadingMd.medium, color = Palette.green900)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(Palette.white)
                .border(0.5.dp, Palette.graySecondary, CircleShape)
                .clickable(enabled = !isDisabled) { onTap() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClubAvatar(url = selectedClub?.profileUrl, size = 32.dp)
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
private fun ClubAvatar(url: String?, size: androidx.compose.ui.unit.Dp) {
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size).clip(CircleShape).background(Palette.grayQuaternary)
        )
    } else {
        Box(modifier = Modifier.size(size).clip(CircleShape).background(Palette.grayQuaternary))
    }
}

/** Bottom-sheet picker. Mirrors iOS `SelectClubView`. */
@Composable
private fun ClubPicker(
    clubs: List<EventSelectableClub>,
    selectedClubId: Int?,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Select club",
            style = AppTypography.HeadingXL.bold,
            color = Palette.blackHigh,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Select the club where this event will be created.",
            style = AppTypography.BodyTextSm.regular,
            color = Palette.blackMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            clubs.forEach { club ->
                ClubPickerRow(
                    club = club,
                    isSelected = club.clubId == selectedClubId,
                    onSelect = { onSelect(club.clubId) }
                )
            }
        }
    }
}

@Composable
private fun ClubPickerRow(
    club: EventSelectableClub,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.grayQuaternary.copy(alpha = 0.4f))
            .clickable { onSelect() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClubAvatar(url = club.profileUrl, size = 48.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = club.clubName,
                style = AppTypography.BodyTextMd.regular,
                color = Palette.blackHigh
            )
            Text(
                text = club.role.title,
                style = AppTypography.BodyTextSm.regular,
                color = Palette.blackMedium
            )
        }
        Box(
            modifier = Modifier
                .size(22.dp)
                .border(
                    2.dp,
                    if (isSelected) Palette.blackHigh else Palette.grayTeritary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(modifier = Modifier.size(12.dp).background(Palette.blackHigh, CircleShape))
            }
        }
    }
}
