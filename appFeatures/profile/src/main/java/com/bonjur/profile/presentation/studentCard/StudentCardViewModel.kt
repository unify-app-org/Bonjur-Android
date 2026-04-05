package com.bonjur.profile.presentation.studentCard

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.navigation.Navigator
import com.bonjur.profile.presentation.studentCard.models.StudentCardAction
import com.bonjur.profile.presentation.studentCard.models.StudentCardInputData
import com.bonjur.profile.presentation.studentCard.models.StudentCardSideEffect
import com.bonjur.profile.presentation.studentCard.models.StudentCardViewState
import com.bonjur.profile.presentation.studentCard.models.StudentCardViewState.CoverSheetDismissIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentCardViewModel @Inject constructor() : FeatureViewModel<StudentCardViewState, StudentCardAction, StudentCardSideEffect>(
    StudentCardViewState()
) {

    private lateinit var inputData: StudentCardInputData
    private lateinit var navigator: Navigator

    fun init(inputData: StudentCardInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator

        updateState(
            state.copy(
                previewCard = inputData.userCardModel,
                savedCover = inputData.userCardModel.backgroundCover,
                draftCover = inputData.userCardModel.backgroundCover
            )
        )
    }

    override fun handle(action: StudentCardAction) {
        when (action) {
            StudentCardAction.CloseTapped -> handleCloseTapped()
            StudentCardAction.EditTapped -> handleEditTapped()
            is StudentCardAction.SetCoverSheetPresented ->
                updateState(state.copy(isChooseColorSheetPresented = action.isPresented))
            is StudentCardAction.CoverSelected -> handleCoverSelected(action.cover)
            StudentCardAction.CancelColorSelection -> handleCancelColorSelection()
            StudentCardAction.SaveColorSelection -> handleSaveColorSelection()
            StudentCardAction.CoverSheetDismissed -> handleCoverSheetDismissed()
        }
    }

    private fun handleCloseTapped() {
        val committedCover = state.savedCover
        viewModelScope.launch {
            inputData.onSave(committedCover)
            navigator.navigateUp()
        }
    }

    private fun handleEditTapped() {
        updateState(
            state.copy(
                draftCover = state.savedCover,
                coverSheetDismissIntent = CoverSheetDismissIntent.NONE,
                isChooseColorSheetPresented = true
            )
        )
        applyPreview(state.savedCover)
    }

    private fun handleCoverSelected(cover: AppUIEntities.BackgroundType?) {
        updateState(state.copy(draftCover = cover))
        applyPreview(cover)
    }

    private fun handleCancelColorSelection() {
        updateState(
            state.copy(
                coverSheetDismissIntent = CoverSheetDismissIntent.CANCEL,
                draftCover = state.savedCover,
                isChooseColorSheetPresented = false
            )
        )
        applyPreview(state.savedCover)
    }

    private fun handleSaveColorSelection() {
        updateState(
            state.copy(coverSheetDismissIntent = CoverSheetDismissIntent.SAVE)
        )
        commitDraftCover(resetDismissIntent = false)
        updateState(state.copy(isChooseColorSheetPresented = false))
    }

    private fun handleCoverSheetDismissed() {
        updateState(state.copy(isChooseColorSheetPresented = false))

        when (state.coverSheetDismissIntent) {
            CoverSheetDismissIntent.CANCEL ->
                updateState(state.copy(coverSheetDismissIntent = CoverSheetDismissIntent.NONE))
            CoverSheetDismissIntent.SAVE ->
                updateState(state.copy(coverSheetDismissIntent = CoverSheetDismissIntent.NONE))
            CoverSheetDismissIntent.NONE ->
                commitDraftCover()
        }
    }

    private fun commitDraftCover(resetDismissIntent: Boolean = true) {
        updateState(state.copy(savedCover = state.draftCover))
        applyPreview(state.savedCover)

        if (resetDismissIntent) {
            updateState(state.copy(coverSheetDismissIntent = CoverSheetDismissIntent.NONE))
        }

        val committedCover = state.savedCover
        viewModelScope.launch {
            inputData.onSave(committedCover)
        }
    }

    private fun applyPreview(cover: AppUIEntities.BackgroundType?) {
        val card = state.previewCard ?: return
        updateState(state.copy(previewCard = card.withBackground(cover)))
    }
}
