package com.bonjur.clubs.presentation.create.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.FieldValues
import com.bonjur.designSystem.components.fieldSchema.isValid
import com.bonjur.designSystem.components.fieldSchema.text

// MARK: - ClubCreate input
data class ClubCreateInputData(
    val clubId: Int? = null,
    val prefill: ClubsDetails.ClubEditPrefill? = null
)

// MARK: - Side effects
sealed class ClubCreateSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ClubCreateSideEffect()
    data class Error(val message: String) : ClubCreateSideEffect()
}

// MARK: - View State
data class ClubCreateViewState(
    val values: FieldValues = emptyMap(),
    val logoUri: String? = null,
    val coverUri: String? = null,
    val existingLogoUrl: String? = null,
    val existingCoverUrl: String? = null,
    val categorySections: List<CategorySection> = emptyList(),
    val showCategoryPicker: Boolean = false,
    /**
     * Post-create prompt: a brand-new club is unverified, and verification is the
     * hard gate to creating events in it. Shown only on the create path. Mirrors iOS.
     */
    val showVerifyPrompt: Boolean = false,
    val isLoading: Boolean = false,
    val isEdit: Boolean = false
) : FeatureState {

    val schema: List<AppFieldSchema.Field> get() = ClubCreateSchema.fields

    val isValid: Boolean
        get() = values.isValid(schema) && values.text(AppFieldSchema.FieldId.CLUB_NAME).isNotBlank()

    val topTitle: String
        get() = if (isEdit) "Edit club" else "Create new club"
}

// MARK: - Feature Action
sealed class ClubCreateAction : FeatureAction {
    object FetchData : ClubCreateAction()
    object BackTapped : ClubCreateAction()
    object ContinueTapped : ClubCreateAction()
    data class FieldChanged(
        val id: AppFieldSchema.FieldId,
        val value: AppFieldSchema.FieldValue
    ) : ClubCreateAction()
    data class LogoSelected(val uri: String?) : ClubCreateAction()
    data class CoverSelected(val uri: String?) : ClubCreateAction()
    object RequestVerificationTapped : ClubCreateAction()
    object DismissVerifyPrompt : ClubCreateAction()
    object AddCategoryTapped : ClubCreateAction()
    object DismissCategoryPicker : ClubCreateAction()
    object CategoryPickerDone : ClubCreateAction()
    data class CategoryToggled(val id: Int) : ClubCreateAction()
    data class RemoveCategory(val id: Int) : ClubCreateAction()
}
