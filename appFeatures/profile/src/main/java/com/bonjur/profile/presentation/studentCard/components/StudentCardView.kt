package com.bonjur.profile.presentation.studentCard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.profile.presentation.detail.components.UserCardView
import com.bonjur.profile.presentation.studentCard.models.StudentCardAction
import com.bonjur.profile.presentation.studentCard.models.StudentCardSideEffect
import com.bonjur.profile.presentation.studentCard.models.StudentCardViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCardView(
    store: FeatureStore<StudentCardViewState, StudentCardAction, StudentCardSideEffect>
) {
    val state = store.state
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val animSpec = tween<Float>(durationMillis = 250)
    val collapsedWeight by animateFloatAsState(
        targetValue = if (state.shouldShowCollapsedSpacing) 1f else 0.001f,
        animationSpec = animSpec
    )

    val animatedGradientColor by animateColorAsState(
        targetValue = state.selectedColor.copy(alpha = 0.5f),
        animationSpec = tween(250)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(animatedGradientColor, Palette.white)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { store.send(StudentCardAction.CloseTapped) }) {
                    Icon(
                        painter = Images.Icons.xmark(),
                        contentDescription = "Close",
                        tint = Palette.blackHigh
                    )
                }
                IconButton(onClick = { store.send(StudentCardAction.EditTapped) }) {
                    Icon(
                        painter = Images.Icons.penLine(),
                        contentDescription = "Edit",
                        tint = Palette.blackHigh
                    )
                }
            }

            Text(
                text = "User Card",
                style = AppTypography.TitleL.extraBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(collapsedWeight))

            state.previewCard?.let { model ->
                UserCardView(model = model, onTap = {})
            }

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.weight(collapsedWeight))
        }
    }

    if (state.isChooseColorSheetPresented) {
        ModalBottomSheet(
            onDismissRequest = { store.send(StudentCardAction.CoverSheetDismissed) },
            sheetState = sheetState,
            containerColor = Palette.white,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            StudentCardCoverPickerSheet(
                selected = state.draftCover,
                onCoverSelected = { store.send(StudentCardAction.CoverSelected(it)) },
                onSave = { store.send(StudentCardAction.SaveColorSelection) },
                onCancel = { store.send(StudentCardAction.CancelColorSelection) }
            )
        }
    }
}
