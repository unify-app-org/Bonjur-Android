package com.bonjur.profile.presentation.editProfile

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.Navigator
import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.profile.presentation.editProfile.models.EditProfileAction
import com.bonjur.profile.presentation.editProfile.models.EditProfileInputData
import com.bonjur.profile.presentation.editProfile.models.EditProfileSideEffect
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<EditProfileViewState, EditProfileAction, EditProfileSideEffect>(
    EditProfileViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ProfileUseCase
    )

    private lateinit var inputData: EditProfileInputData
    private lateinit var navigator: Navigator

    fun init(inputData: EditProfileInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        handle(EditProfileAction.FetchData)
    }

    override fun handle(action: EditProfileAction) {
        when (action) {
            EditProfileAction.FetchData -> fetchData()
            EditProfileAction.SaveTapped -> saveTapped()
            EditProfileAction.BirthdayTapped -> updateState(state.copy(showDatePicker = true))
            EditProfileAction.CloseDatePicker -> updateState(state.copy(showDatePicker = false))
            is EditProfileAction.AboutChanged -> updateState(state.copy(about = action.value))
            is EditProfileAction.BirthDateChanged -> updateState(
                state.copy(birthDateText = action.value, showDatePicker = false)
            )
            is EditProfileAction.GenderSelected -> updateState(
                state.copy(selectedGender = action.gender, showDatePicker = false)
            )
            is EditProfileAction.ImageSelected -> updateState(state.copy(selectedImageUri = action.uri))
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            postEffect(EditProfileSideEffect.Loading(true))
            try {
                val profile = dependencies.useCase.fetchProfile()
                updateState(
                    state.copy(
                        name = profile?.name ?: "-",
                        faculty = profile?.faculty ?: "-",
                        community = profile?.community ?: "-",
                        entry = profile?.entry ?: "-",
                        course = profile?.course ?: "-",
                        about = profile?.about ?: "",
                        avatarUrl = profile?.avatarUrl
                    )
                )
            } catch (e: Exception) {
                // Keep default state
            } finally {
                postEffect(EditProfileSideEffect.Loading(false))
            }
        }
    }

    private fun saveTapped() {
        viewModelScope.launch {
            postEffect(EditProfileSideEffect.Loading(true))
            try {
                dependencies.useCase.editProfile(
                    about = state.about,
                    gender = state.selectedGender.name.lowercase(),
                    birthDate = state.birthDateText,
                    imageUri = state.selectedImageUri
                )
                navigator.navigateUp()
            } catch (e: Exception) {
                postEffect(
                    EditProfileSideEffect.Error(
                        e.message ?: "Error",
                        null
                    )
                )
            } finally {
                postEffect(EditProfileSideEffect.Loading(false))
            }
        }
    }
}
