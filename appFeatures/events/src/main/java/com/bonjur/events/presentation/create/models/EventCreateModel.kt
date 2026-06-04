package com.bonjur.events.presentation.create.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// MARK: - EventCreate input
data class EventCreateInputData(
    val eventId: String? = null
)

// MARK: - Side effects
sealed class EventCreateSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : EventCreateSideEffect()
    data class Error(val message: String) : EventCreateSideEffect()
}

// MARK: - View State
data class EventCreateViewState(
    val name: String = "",
    val about: String = "",
    val location: String = "",
    val ownerContact: String = "",
    val capacity: String = "",
    val rules: String = "",
    val isPublic: Boolean = true,
    val eventDate: String = "",
    val isLoading: Boolean = false,
    val isEdit: Boolean = false
) : FeatureState {
    val isValid: Boolean
        get() = name.isNotBlank()

    val topTitle: String
        get() = if (isEdit) "Edit event" else "Create new event"
}

// MARK: - Feature Action
sealed class EventCreateAction : FeatureAction {
    object FetchData : EventCreateAction()
    object BackTapped : EventCreateAction()
    object ContinueTapped : EventCreateAction()
    data class NameChanged(val value: String) : EventCreateAction()
    data class AboutChanged(val value: String) : EventCreateAction()
    data class LocationChanged(val value: String) : EventCreateAction()
    data class OwnerContactChanged(val value: String) : EventCreateAction()
    data class CapacityChanged(val value: String) : EventCreateAction()
    data class RulesChanged(val value: String) : EventCreateAction()
    data class VisibilityChanged(val isPublic: Boolean) : EventCreateAction()
    data class EventDateChanged(val value: String) : EventCreateAction()
}
