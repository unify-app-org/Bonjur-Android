package com.bonjur.profile.presentation.editProfile

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.navigation.Navigator
import com.bonjur.profile.data.DTOs.ProfileUpdateRequest
import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.editProfile.models.EditProfileAction
import com.bonjur.profile.presentation.editProfile.models.EditProfileInputData
import com.bonjur.profile.presentation.editProfile.models.EditProfileSideEffect
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState
import com.bonjur.profile.presentation.editProfile.models.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<EditProfileViewState, EditProfileAction, EditProfileSideEffect>(
    EditProfileViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ProfileUseCase,
        @ApplicationContext val context: Context
    )

    private lateinit var inputData: EditProfileInputData
    private lateinit var navigator: Navigator

    fun init(inputData: EditProfileInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        prefill()
    }

    override fun handle(action: EditProfileAction) {
        when (action) {
            EditProfileAction.FetchData -> prefill()
            EditProfileAction.SaveTapped -> saveTapped()
            EditProfileAction.BackTapped -> viewModelScope.launch { navigator.navigateUp() }
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

            // Category picker
            EditProfileAction.AddCategoryTapped -> updateState(state.copy(showCategoryPicker = true))
            EditProfileAction.DismissCategoryPicker -> updateState(state.copy(showCategoryPicker = false))
            EditProfileAction.CategoryPickerDone -> updateState(state.copy(showCategoryPicker = false))
            is EditProfileAction.CategoryToggled -> toggleCategory(action.id)

            // Language picker
            EditProfileAction.AddLanguageTapped -> updateState(state.copy(showLanguagePicker = true))
            EditProfileAction.DismissLanguagePicker -> updateState(state.copy(showLanguagePicker = false))
            EditProfileAction.LanguagePickerDone -> updateState(state.copy(showLanguagePicker = false))
            is EditProfileAction.LanguageToggled -> toggleLanguage(action.id)

            // Cover picker
            EditProfileAction.CoverTapped ->
                updateState(state.copy(showCoverPicker = true, draftBackground = state.background))
            EditProfileAction.DismissCoverPicker -> updateState(state.copy(showCoverPicker = false))
            EditProfileAction.CoverPickerDone ->
                updateState(state.copy(background = state.draftBackground, showCoverPicker = false))
            is EditProfileAction.CoverSelected -> updateState(state.copy(draftBackground = action.background))
        }
    }

    /** Prefills the form from the profile that opened it (mirrors iOS — no refetch). */
    private fun prefill() {
        val profile = inputData.profileData ?: return
        val card = profile.userCardModel
        updateState(
            state.copy(
                name = card.nameSurname,
                faculty = card.speciality,
                community = card.community,
                entry = card.entryYear,
                course = card.course,
                about = profile.about ?: "",
                birthDateText = isoToDisplay(profile.birthday),
                selectedGender = Gender.from(profile.gender) ?: Gender.MALE,
                avatarUrl = card.imageUrl,
                background = card.backgroundCover
            )
        )
        loadOptions(profile)
    }

    /** Loads category + language options and marks the ones already on the profile. */
    private fun loadOptions(profile: ProfileDetail.UIModel) {
        viewModelScope.launch {
            val catIds = profile.tags.map { it.id }.toSet()
            val langIds = (profile.languages?.map { it.id } ?: emptyList()).toSet()

            val categories = runCatching { dependencies.useCase.getCategories() }
                .getOrDefault(emptyList())
                .map { section ->
                    section.copy(categories = section.categories.map { it.copy(selected = it.id in catIds) })
                }
            val languages = runCatching { dependencies.useCase.getLanguages() }
                .getOrDefault(emptyList())
                .map { it.copy(selected = it.id in langIds) }

            updateState(state.copy(categorySections = categories, languageOptions = languages))
        }
    }

    private fun toggleCategory(id: Int) {
        updateState(
            state.copy(
                categorySections = state.categorySections.map { section ->
                    section.copy(
                        categories = section.categories.map {
                            if (it.id == id) it.copy(selected = !it.selected) else it
                        }
                    )
                }
            )
        )
    }

    private fun toggleLanguage(id: Int) {
        updateState(
            state.copy(
                languageOptions = state.languageOptions.map {
                    if (it.id == id) it.copy(selected = !it.selected) else it
                }
            )
        )
    }

    private fun saveTapped() {
        viewModelScope.launch {
            postEffect(EditProfileSideEffect.Loading(true))
            try {
                val request = ProfileUpdateRequest(
                    birthDate = displayToIso(state.birthDateText),
                    gender = state.selectedGender.name,
                    about = state.about.ifBlank { null },
                    categoriesId = state.selectedCategoryIds,
                    languagesId = state.selectedLanguageIds,
                    backgroundColour = state.background?.toRequestString()
                )
                dependencies.useCase.editProfile(request, readImageBytes())
                inputData.onSaved()
                navigator.navigateUp()
                AppSnackBar.show(
                    title = "Profile updated successfully",
                    subtitle = "Your changes are saved",
                    style = AppSnackBar.Style.SUCCESS
                )
            } catch (e: Exception) {
                postEffect(EditProfileSideEffect.Error(e.message ?: "Error", null))
            } finally {
                postEffect(EditProfileSideEffect.Loading(false))
            }
        }
    }

    // Birthday: display is dd-MM-yyyy (matches iOS), backend wants yyyy-MM-dd.
    private fun isoToDisplay(iso: String?): String =
        reformat(iso, from = "yyyy-MM-dd", to = "dd-MM-yyyy") ?: (iso ?: "")

    private fun displayToIso(text: String): String? =
        reformat(text, from = "dd-MM-yyyy", to = "yyyy-MM-dd")

    private fun reformat(value: String?, from: String, to: String): String? {
        if (value.isNullOrBlank()) return null
        return runCatching {
            val parser = java.text.SimpleDateFormat(from, java.util.Locale.US)
            val printer = java.text.SimpleDateFormat(to, java.util.Locale.US)
            printer.format(parser.parse(value)!!)
        }.getOrNull()
    }

    // Re-encode the picked image to JPEG so it matches the "avatar.jpg"/image/jpeg part
    // (gallery may hand us PNG/HEIC/webp; server expects JPEG and 500s otherwise).
    private fun readImageBytes(): ByteArray? {
        val uri = state.selectedImageUri ?: return null
        val resolver = dependencies.context.contentResolver
        val bitmap = runCatching {
            resolver.openInputStream(uri)?.use { android.graphics.BitmapFactory.decodeStream(it) }
        }.getOrNull()
        if (bitmap != null) {
            return runCatching {
                java.io.ByteArrayOutputStream().use { out ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
                    out.toByteArray()
                }
            }.getOrNull()
        }
        // Fallback: raw bytes if decode failed.
        return runCatching {
            resolver.openInputStream(uri)?.use { it.readBytes() }
        }.getOrNull()
    }

    // Backend enum = colour names (iOS BackgroundType raw values), not Primary/Secondary.
    private fun AppUIEntities.BackgroundType.toRequestString(): String = when (this) {
        is AppUIEntities.BackgroundType.Primary -> "GREEN"
        is AppUIEntities.BackgroundType.Secondary -> "BLUE"
        is AppUIEntities.BackgroundType.Tertiary -> "PURPLE"
        is AppUIEntities.BackgroundType.CustomColor -> when (colorType) {
            is AppUIEntities.ColorType.Orange -> "ORANGE"
            is AppUIEntities.ColorType.Red -> "RED"
            is AppUIEntities.ColorType.Pink -> "PINK"
            is AppUIEntities.ColorType.Custom -> "GREEN"
        }
    }
}
