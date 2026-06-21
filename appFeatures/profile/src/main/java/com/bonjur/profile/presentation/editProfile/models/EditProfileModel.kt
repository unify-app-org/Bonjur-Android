package com.bonjur.profile.presentation.editProfile.models

import android.net.Uri
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.profile.presentation.detail.models.ProfileDetail

// MARK: - EditProfile input
// Mirrors iOS EditProfileInputData { profileData: ProfileDetail.UIModel } — the
// edit form is prefilled from the profile that opened it, not a fresh fetch.
data class EditProfileInputData(
    val profileData: ProfileDetail.UIModel? = null,
    val onSaved: () -> Unit = {}
)

// MARK: - Side effects
sealed class EditProfileSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : EditProfileSideEffect()
    data class Error(val title: String, val message: String?) : EditProfileSideEffect()
}

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    companion object {
        fun from(raw: String?): Gender? = when (raw?.uppercase()) {
            "MALE" -> MALE
            "FEMALE" -> FEMALE
            "OTHER" -> OTHER
            else -> null
        }
    }
}

// MARK: - View State
data class EditProfileViewState(
    val name: String = "-",
    val faculty: String = "-",
    val community: String = "-",
    val entry: String = "-",
    val course: String = "-",
    val about: String = "",
    val birthDateText: String = "",
    val showDatePicker: Boolean = false,
    val selectedGender: Gender = Gender.MALE,
    val avatarUrl: String? = null,
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    // Category / language option lists carry their own selection (mirrors iOS).
    val categorySections: List<CategorySection> = emptyList(),
    val languageOptions: List<SelectableListItemModel> = emptyList(),
    val background: AppUIEntities.BackgroundType? = null,
    val draftBackground: AppUIEntities.BackgroundType? = null,
    val showCategoryPicker: Boolean = false,
    val showLanguagePicker: Boolean = false,
    val showCoverPicker: Boolean = false
) : FeatureState {
    val selectedCategoryIds: List<Int>
        get() = categorySections.flatMap { it.categories }.filter { it.selected }.map { it.id }
    val selectedLanguageIds: List<Int>
        get() = languageOptions.filter { it.selected }.map { it.id }
    val selectedCategoryTitles: List<String>
        get() = categorySections.flatMap { it.categories }.filter { it.selected }.map { it.title }
    val selectedLanguageTitles: List<String>
        get() = languageOptions.filter { it.selected }.map { it.title }
}

// MARK: - Feature Action
sealed class EditProfileAction : FeatureAction {
    object FetchData : EditProfileAction()
    object SaveTapped : EditProfileAction()
    object BackTapped : EditProfileAction()
    object BirthdayTapped : EditProfileAction()
    object CloseDatePicker : EditProfileAction()
    data class AboutChanged(val value: String) : EditProfileAction()
    data class BirthDateChanged(val value: String) : EditProfileAction()
    data class GenderSelected(val gender: Gender) : EditProfileAction()
    data class ImageSelected(val uri: Uri) : EditProfileAction()

    // Category picker
    object AddCategoryTapped : EditProfileAction()
    object DismissCategoryPicker : EditProfileAction()
    object CategoryPickerDone : EditProfileAction()
    data class CategoryToggled(val id: Int) : EditProfileAction()

    // Language picker
    object AddLanguageTapped : EditProfileAction()
    object DismissLanguagePicker : EditProfileAction()
    object LanguagePickerDone : EditProfileAction()
    data class LanguageToggled(val id: Int) : EditProfileAction()

    // Cover picker
    object CoverTapped : EditProfileAction()
    object DismissCoverPicker : EditProfileAction()
    object CoverPickerDone : EditProfileAction()
    data class CoverSelected(val background: AppUIEntities.BackgroundType?) : EditProfileAction()
}
