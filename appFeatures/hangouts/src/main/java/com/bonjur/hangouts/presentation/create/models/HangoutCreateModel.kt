package com.bonjur.hangouts.presentation.create.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.FieldValues
import com.bonjur.designSystem.components.fieldSchema.isValid
import com.bonjur.designSystem.components.fieldSchema.text

/** Prefill payload for edit mode. Mirrors iOS `HangoutsCreate.PrefillData`. */
data class HangoutCreatePrefillData(
    val values: FieldValues
)

// MARK: - HangoutCreate input
data class HangoutCreateInputData(
    val hangoutId: String? = null,
    val prefill: HangoutCreatePrefillData? = null
)

// MARK: - Side effects
sealed class HangoutCreateSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : HangoutCreateSideEffect()
    data class Error(val message: String) : HangoutCreateSideEffect()
}

// MARK: - View State
data class HangoutCreateViewState(
    val values: FieldValues = emptyMap(),
    val categorySections: List<CategorySection> = emptyList(),
    val showCategoryPicker: Boolean = false,
    val isLoading: Boolean = false,
    val isEdit: Boolean = false
) : FeatureState {

    val schema: List<AppFieldSchema.Field> get() = HangoutCreateSchema.fields

    val isValid: Boolean
        get() = values.isValid(schema) &&
            values.text(AppFieldSchema.FieldId.HANGOUT_NAME).isNotBlank()

    val topTitle: String
        get() = if (isEdit) "Edit hangout" else "Create new hangouts"
}

// MARK: - Feature Action
sealed class HangoutCreateAction : FeatureAction {
    object FetchData : HangoutCreateAction()
    object BackTapped : HangoutCreateAction()
    object ContinueTapped : HangoutCreateAction()
    data class FieldChanged(
        val id: AppFieldSchema.FieldId,
        val value: AppFieldSchema.FieldValue
    ) : HangoutCreateAction()
    object AddCategoryTapped : HangoutCreateAction()
    data class CategoryToggled(val id: Int) : HangoutCreateAction()
    data class RemoveCategory(val id: Int) : HangoutCreateAction()
    object DismissCategoryPicker : HangoutCreateAction()
    object CategoryPickerDone : HangoutCreateAction()
}
