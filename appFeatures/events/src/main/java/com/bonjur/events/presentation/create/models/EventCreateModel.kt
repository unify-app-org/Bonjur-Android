package com.bonjur.events.presentation.create.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.FieldValues
import com.bonjur.designSystem.components.fieldSchema.isValid
import com.bonjur.designSystem.components.fieldSchema.text

/** A club the event can be created under (`GET api/cs/v1/clubs/forEvents`). */
data class EventSelectableClub(
    val clubId: Int,
    val clubName: String,
    val profileUrl: String?,
    val backgroundUrl: String? = null,
    val role: AppUIEntities.UserActivityRole = AppUIEntities.UserActivityRole.MEMBER,
    val background: AppUIEntities.BackgroundType = AppUIEntities.BackgroundType.Primary
)

/** Prefill payload for edit mode. Mirrors iOS `EventsCreate.PrefillData`. */
data class EventCreatePrefillData(
    val selectedClubId: Int,
    val values: FieldValues,
    /**
     * The event's own club display info, so the selector + cover render in edit even
     * when the club isn't in `clubsForEvents` (the detail endpoint is the only source).
     */
    val clubName: String? = null,
    val clubProfileUrl: String? = null,
    val clubCoverUrl: String? = null,
    val clubBackground: AppUIEntities.BackgroundType = AppUIEntities.BackgroundType.Primary
)

// MARK: - EventCreate input
data class EventCreateInputData(
    val eventId: String? = null,
    val prefill: EventCreatePrefillData? = null
)

// MARK: - Side effects
sealed class EventCreateSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : EventCreateSideEffect()
    data class Error(val message: String) : EventCreateSideEffect()
}

// MARK: - View State
data class EventCreateViewState(
    val values: FieldValues = emptyMap(),
    val clubs: List<EventSelectableClub> = emptyList(),
    val selectedClubId: Int? = null,
    val showClubPicker: Boolean = false,
    val categorySections: List<CategorySection> = emptyList(),
    val showCategoryPicker: Boolean = false,
    val isLoading: Boolean = false,
    val isEdit: Boolean = false
) : FeatureState {

    val schema: List<AppFieldSchema.Field> get() = EventCreateSchema.fields

    val selectedClub: EventSelectableClub?
        get() = clubs.firstOrNull { it.clubId == selectedClubId }

    /** Read-only cover: the selected club's official cover photo (mirrors iOS). */
    val coverUrl: String?
        get() = selectedClub?.backgroundUrl

    val coverBackground: AppUIEntities.BackgroundType
        get() = selectedClub?.background ?: AppUIEntities.BackgroundType.Primary

    val isValid: Boolean
        get() = values.isValid(schema) &&
            values.text(AppFieldSchema.FieldId.EVENT_NAME).isNotBlank() &&
            selectedClubId != null

    val topTitle: String
        get() = if (isEdit) "Edit event" else "Create new event"
}

// MARK: - Feature Action
sealed class EventCreateAction : FeatureAction {
    object FetchData : EventCreateAction()
    object BackTapped : EventCreateAction()
    object ContinueTapped : EventCreateAction()
    data class FieldChanged(
        val id: AppFieldSchema.FieldId,
        val value: AppFieldSchema.FieldValue
    ) : EventCreateAction()
    object SelectClubTapped : EventCreateAction()
    object DismissClubPicker : EventCreateAction()
    data class SelectClub(val clubId: Int) : EventCreateAction()
    object AddCategoryTapped : EventCreateAction()
    data class CategoryToggled(val id: Int) : EventCreateAction()
    data class RemoveCategory(val id: Int) : EventCreateAction()
    object DismissCategoryPicker : EventCreateAction()
    object CategoryPickerDone : EventCreateAction()
}
