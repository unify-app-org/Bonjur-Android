package com.bonjur.profile.presentation.editProfile.models

import android.net.Uri
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// MARK: - EditProfile input
data class EditProfileInputData(
    val userId: String = ""
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
    val isLoading: Boolean = false
) : FeatureState

// MARK: - Feature Action
sealed class EditProfileAction : FeatureAction {
    object FetchData : EditProfileAction()
    object SaveTapped : EditProfileAction()
    object BirthdayTapped : EditProfileAction()
    object CloseDatePicker : EditProfileAction()
    data class AboutChanged(val value: String) : EditProfileAction()
    data class BirthDateChanged(val value: String) : EditProfileAction()
    data class GenderSelected(val gender: Gender) : EditProfileAction()
    data class ImageSelected(val uri: Uri) : EditProfileAction()
}
