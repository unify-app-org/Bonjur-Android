package com.bonjur.clubs.presentation.create

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.presentation.create.models.ClubCreateAction
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.clubs.presentation.create.models.ClubCreateViewState
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.radio
import com.bonjur.designSystem.components.fieldSchema.text
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubCreateViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ClubCreateViewState, ClubCreateAction, ClubCreateSideEffect>(
    ClubCreateViewState(
        values = mapOf(
            AppFieldSchema.FieldId.COVER to
                AppFieldSchema.FieldValue.Cover(AppUIEntities.BackgroundType.Primary),
            AppFieldSchema.FieldId.VISIBILITY to
                AppFieldSchema.FieldValue.Radio(AppUIEntities.AccessType.PUBLIC)
        )
    )
) {

    data class Dependencies @Inject constructor(
        val useCase: ClubsUseCase
    )

    private lateinit var inputData: ClubCreateInputData
    private lateinit var navigator: Navigator

    fun init(inputData: ClubCreateInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        if (inputData.clubId != null) {
            updateState(state.copy(isEdit = true))
        }
        fetchData()
    }

    override fun handle(action: ClubCreateAction) {
        when (action) {
            ClubCreateAction.FetchData -> fetchData()
            ClubCreateAction.BackTapped -> navigateBack()
            ClubCreateAction.ContinueTapped -> continueTapped()
            is ClubCreateAction.FieldChanged ->
                updateState(state.copy(values = state.values + (action.id to action.value)))
            is ClubCreateAction.LogoSelected -> updateState(state.copy(logoUri = action.uri))
            is ClubCreateAction.CoverSelected -> updateState(state.copy(coverUri = action.uri))
        }
    }

    private fun fetchData() {
        // Prefill data if editing
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun continueTapped() {
        viewModelScope.launch {
            val clubId = inputData.clubId
            if (clubId != null) {
                editClub(clubId)
            } else {
                createClub()
            }
        }
    }

    private suspend fun createClub() {
        postEffect(ClubCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.createClub(
                name = state.values.text(AppFieldSchema.FieldId.CLUB_NAME),
                about = state.values.text(AppFieldSchema.FieldId.ABOUT),
                location = state.values.text(AppFieldSchema.FieldId.LOCATION),
                ownerContact = state.values.text(AppFieldSchema.FieldId.OWNER_CONTACT),
                capacity = state.values.text(AppFieldSchema.FieldId.CAPACITY).toIntOrNull(),
                rules = state.values.text(AppFieldSchema.FieldId.RULES),
                isPublic = state.values.radio(AppFieldSchema.FieldId.VISIBILITY) == AppUIEntities.AccessType.PUBLIC
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(ClubCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(ClubCreateSideEffect.Loading(false))
        }
    }

    private suspend fun editClub(clubId: Int) {
        postEffect(ClubCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.editClub(
                clubId = clubId,
                name = state.values.text(AppFieldSchema.FieldId.CLUB_NAME),
                about = state.values.text(AppFieldSchema.FieldId.ABOUT),
                location = state.values.text(AppFieldSchema.FieldId.LOCATION),
                ownerContact = state.values.text(AppFieldSchema.FieldId.OWNER_CONTACT),
                capacity = state.values.text(AppFieldSchema.FieldId.CAPACITY).toIntOrNull(),
                rules = state.values.text(AppFieldSchema.FieldId.RULES),
                isPublic = state.values.radio(AppFieldSchema.FieldId.VISIBILITY) == AppUIEntities.AccessType.PUBLIC
            )
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(ClubCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(ClubCreateSideEffect.Loading(false))
        }
    }
}
