package com.bonjur.profile.presentation.studentCard.models

import androidx.compose.ui.graphics.Color
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.profile.presentation.detail.models.UserCardModel

// MARK: - Input Data
data class StudentCardInputData(
    val userCardModel: UserCardModel = UserCardModel(),
    val onSave: (AppUIEntities.BackgroundType?) -> Unit = {}
)

// MARK: - Side Effects
sealed class StudentCardSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : StudentCardSideEffect()
}

// MARK: - View State
data class StudentCardViewState(
    val previewCard: UserCardModel? = null,
    val savedCover: AppUIEntities.BackgroundType? = null,
    val draftCover: AppUIEntities.BackgroundType? = null,
    val isChooseColorSheetPresented: Boolean = false,
    val coverSheetDismissIntent: CoverSheetDismissIntent = CoverSheetDismissIntent.NONE,
    val isSaving: Boolean = false
) : FeatureState {

    enum class CoverSheetDismissIntent {
        NONE, CANCEL, SAVE
    }

    val displayedCover: AppUIEntities.BackgroundType?
        get() = if (isChooseColorSheetPresented) draftCover else savedCover

    val selectedColor: Color
        get() = displayedCover?.bgColor ?: Palette.white

    val shouldShowCollapsedSpacing: Boolean
        get() = !isChooseColorSheetPresented

    companion object {
        val availableCovers: List<AppUIEntities.BackgroundType?> = listOf(
            null,
            AppUIEntities.BackgroundType.Primary,
            AppUIEntities.BackgroundType.Secondary,
            AppUIEntities.BackgroundType.Tertiary,
            AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Orange),
            AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Red),
            AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Pink)
        )
    }
}

// MARK: - Actions
sealed class StudentCardAction : FeatureAction {
    object CloseTapped : StudentCardAction()
    object EditTapped : StudentCardAction()
    data class SetCoverSheetPresented(val isPresented: Boolean) : StudentCardAction()
    data class CoverSelected(val cover: AppUIEntities.BackgroundType?) : StudentCardAction()
    object CancelColorSelection : StudentCardAction()
    object SaveColorSelection : StudentCardAction()
    object CoverSheetDismissed : StudentCardAction()
}
