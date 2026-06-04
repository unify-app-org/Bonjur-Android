package com.bonjur.clubs.presentation.create.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// MARK: - ClubCreate input
data class ClubCreateInputData(
    val clubId: Int? = null
)

// MARK: - Side effects
sealed class ClubCreateSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ClubCreateSideEffect()
    data class Error(val message: String) : ClubCreateSideEffect()
}

// MARK: - View State
data class ClubCreateViewState(
    val name: String = "",
    val about: String = "",
    val location: String = "",
    val ownerContact: String = "",
    val capacity: String = "",
    val rules: String = "",
    val isPublic: Boolean = true,
    val isLoading: Boolean = false,
    val isEdit: Boolean = false
) : FeatureState {
    val isValid: Boolean
        get() = name.isNotBlank()

    val topTitle: String
        get() = if (isEdit) "Edit club" else "Create new club"
}

// MARK: - Feature Action
sealed class ClubCreateAction : FeatureAction {
    object FetchData : ClubCreateAction()
    object BackTapped : ClubCreateAction()
    object ContinueTapped : ClubCreateAction()
    data class NameChanged(val value: String) : ClubCreateAction()
    data class AboutChanged(val value: String) : ClubCreateAction()
    data class LocationChanged(val value: String) : ClubCreateAction()
    data class OwnerContactChanged(val value: String) : ClubCreateAction()
    data class CapacityChanged(val value: String) : ClubCreateAction()
    data class RulesChanged(val value: String) : ClubCreateAction()
    data class VisibilityChanged(val isPublic: Boolean) : ClubCreateAction()
}
