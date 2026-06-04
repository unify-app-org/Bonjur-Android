package com.bonjur.hangouts.presentation.create.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// MARK: - HangoutCreate input
data class HangoutCreateInputData(
    val hangoutId: String? = null
)

// MARK: - Side effects
sealed class HangoutCreateSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : HangoutCreateSideEffect()
    data class Error(val message: String) : HangoutCreateSideEffect()
}

// MARK: - View State
data class HangoutCreateViewState(
    val name: String = "",
    val about: String = "",
    val location: String = "",
    val ownerContact: String = "",
    val capacity: String = "",
    val rules: String = "",
    val isPublic: Boolean = true,
    val hangoutDate: String = "",
    val isLoading: Boolean = false,
    val isEdit: Boolean = false
) : FeatureState {
    val isValid: Boolean
        get() = name.isNotBlank()

    val topTitle: String
        get() = if (isEdit) "Edit hangout" else "Create new hangout"
}

// MARK: - Feature Action
sealed class HangoutCreateAction : FeatureAction {
    object FetchData : HangoutCreateAction()
    object BackTapped : HangoutCreateAction()
    object ContinueTapped : HangoutCreateAction()
    data class NameChanged(val value: String) : HangoutCreateAction()
    data class AboutChanged(val value: String) : HangoutCreateAction()
    data class LocationChanged(val value: String) : HangoutCreateAction()
    data class OwnerContactChanged(val value: String) : HangoutCreateAction()
    data class CapacityChanged(val value: String) : HangoutCreateAction()
    data class RulesChanged(val value: String) : HangoutCreateAction()
    data class VisibilityChanged(val isPublic: Boolean) : HangoutCreateAction()
    data class HangoutDateChanged(val value: String) : HangoutCreateAction()
}
