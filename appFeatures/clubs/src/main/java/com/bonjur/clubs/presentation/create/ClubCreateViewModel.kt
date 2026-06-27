package com.bonjur.clubs.presentation.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.domain.models.ClubsDetails
import com.bonjur.clubs.domain.useCase.ClubFormData
import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.presentation.create.models.ClubCreateAction
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.create.models.ClubCreateSideEffect
import com.bonjur.clubs.presentation.create.models.ClubCreateViewState
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.cover
import com.bonjur.designSystem.components.fieldSchema.links
import com.bonjur.designSystem.components.fieldSchema.radio
import com.bonjur.designSystem.components.fieldSchema.tags
import com.bonjur.designSystem.components.fieldSchema.text
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ClubCreateViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ClubCreateViewState, ClubCreateAction, ClubCreateSideEffect>(
    ClubCreateViewState(
        values = mapOf(
            AppFieldSchema.FieldId.COVER to
                AppFieldSchema.FieldValue.Cover(AppUIEntities.BackgroundType.Primary),
            AppFieldSchema.FieldId.VISIBILITY to
                AppFieldSchema.FieldValue.Radio(AppUIEntities.AccessType.PUBLIC)
        )
    )
) {

    data class Dependencies @Inject constructor(
        val useCase: ClubsUseCase,
        @ApplicationContext val context: Context
    )

    private lateinit var inputData: ClubCreateInputData
    private lateinit var navigator: Navigator

    fun init(inputData: ClubCreateInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        if (inputData.clubId != null) {
            updateState(state.copy(isEdit = true))
        }
        inputData.prefill?.let { applyPrefill(it) }
        fetchData()
    }

    /** Pre-fills the form from existing club data on edit. Mirrors iOS `applyPrefillData`. */
    private fun applyPrefill(prefill: ClubsDetails.ClubEditPrefill) {
        updateState(
            state.copy(
                values = state.values + prefill.values,
                existingLogoUrl = prefill.logoUrl,
                existingCoverUrl = prefill.coverUrl
            )
        )
    }

    override fun handle(action: ClubCreateAction) {
        when (action) {
            ClubCreateAction.FetchData -> fetchData()
            ClubCreateAction.BackTapped -> navigateBack()
            ClubCreateAction.ContinueTapped -> continueTapped()
            ClubCreateAction.RequestVerificationTapped -> requestVerification()
            ClubCreateAction.DismissVerifyPrompt -> dismissVerifyPrompt()
            is ClubCreateAction.FieldChanged ->
                updateState(state.copy(values = state.values + (action.id to action.value)))
            is ClubCreateAction.LogoSelected -> updateState(state.copy(logoUri = action.uri))
            is ClubCreateAction.CoverSelected -> updateState(state.copy(coverUri = action.uri))
            ClubCreateAction.AddCategoryTapped -> updateState(state.copy(showCategoryPicker = true))
            ClubCreateAction.DismissCategoryPicker -> dismissCategoryPicker()
            ClubCreateAction.CategoryPickerDone -> dismissCategoryPicker()
            is ClubCreateAction.CategoryToggled -> toggleCategory(action.id)
            is ClubCreateAction.RemoveCategory -> removeCategory(action.id)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            runCatching { dependencies.useCase.getCategories() }
                .onFailure {
                    AppSnackBar.show(title = "Couldn't load categories", style = AppSnackBar.Style.ERROR)
                }
                .onSuccess { sections ->
                    // Mark categories already selected by the pre-fill (edit mode).
                    val selectedIds = state.values.tags(AppFieldSchema.FieldId.CATEGORY)
                        .map { it.id }.toSet()
                    val marked = sections.map { section ->
                        section.copy(categories = section.categories.map {
                            it.copy(selected = selectedIds.contains(it.id))
                        })
                    }
                    updateState(state.copy(categorySections = marked))
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    // MARK: - Categories

    private fun toggleCategory(id: Int) {
        updateState(state.copy(categorySections = state.categorySections.toggle(id)))
    }

    private fun removeCategory(id: Int) {
        val sections = state.categorySections.setSelected(id, false)
        updateState(state.copy(categorySections = sections, values = state.values.syncCategories(sections)))
    }

    private fun dismissCategoryPicker() {
        updateState(
            state.copy(
                showCategoryPicker = false,
                values = state.values.syncCategories(state.categorySections)
            )
        )
    }

    private fun List<CategorySection>.toggle(id: Int): List<CategorySection> = map { section ->
        section.copy(categories = section.categories.map {
            if (it.id == id) it.copy(selected = !it.selected) else it
        })
    }

    private fun List<CategorySection>.setSelected(id: Int, selected: Boolean): List<CategorySection> =
        map { section ->
            section.copy(categories = section.categories.map {
                if (it.id == id) it.copy(selected = selected) else it
            })
        }

    private fun Map<AppFieldSchema.FieldId, AppFieldSchema.FieldValue>.syncCategories(
        sections: List<CategorySection>
    ): Map<AppFieldSchema.FieldId, AppFieldSchema.FieldValue> {
        val tags = sections.flatMap { it.categories }
            .filter { it.selected }
            .map { AppFieldSchema.TagItem(id = it.id, label = it.title) }
        return this + (AppFieldSchema.FieldId.CATEGORY to AppFieldSchema.FieldValue.Tags(tags))
    }

    // MARK: - Submit

    private fun continueTapped() {
        viewModelScope.launch {
            val clubId = inputData.clubId
            if (clubId != null) editClub(clubId) else createClub()
        }
    }

    private suspend fun createClub() {
        postEffect(ClubCreateSideEffect.Loading(true))
        try {
            dependencies.useCase.createClub(buildForm())
            // New clubs start unverified; surface the verify gate before leaving
            // (no navigateUp here — the prompt's buttons pop). Mirrors iOS.
            updateState(state.copy(showVerifyPrompt = true))
        } catch (e: Exception) {
            postEffect(ClubCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(ClubCreateSideEffect.Loading(false))
        }
    }

    private suspend fun editClub(clubId: Int) = submit { form ->
        dependencies.useCase.editClub(clubId, form)
    }

    private suspend inline fun submit(crossinline call: suspend (ClubFormData) -> Unit) {
        postEffect(ClubCreateSideEffect.Loading(true))
        try {
            call(buildForm())
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(ClubCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(ClubCreateSideEffect.Loading(false))
        }
    }

    // MARK: - Verification prompt

    /**
     * Backend has no verify-request endpoint yet, so this is an optimistic stub:
     * dismiss, confirm, leave. Wire the real POST once it lands (see verify-gate
     * backend TODOs: POST verify-request + clubStatus in payloads).
     */
    private fun requestVerification() {
        updateState(state.copy(showVerifyPrompt = false))
        AppSnackBar.show(
            title = "Verification requested",
            subtitle = state.values.text(AppFieldSchema.FieldId.CLUB_NAME),
            style = AppSnackBar.Style.SUCCESS
        )
        navigateBack()
    }

    private fun dismissVerifyPrompt() {
        updateState(state.copy(showVerifyPrompt = false))
        navigateBack()
    }

    private suspend fun buildForm(): ClubFormData {
        val values = state.values
        return ClubFormData(
            name = values.text(AppFieldSchema.FieldId.CLUB_NAME),
            about = values.text(AppFieldSchema.FieldId.ABOUT),
            location = values.text(AppFieldSchema.FieldId.LOCATION),
            ownerContact = values.text(AppFieldSchema.FieldId.OWNER_CONTACT),
            capacity = values.text(AppFieldSchema.FieldId.CAPACITY).toIntOrNull(),
            rules = values.text(AppFieldSchema.FieldId.RULES),
            isPublic = values.radio(AppFieldSchema.FieldId.VISIBILITY) == AppUIEntities.AccessType.PUBLIC,
            background = values.cover(AppFieldSchema.FieldId.COVER),
            categoryIds = values.tags(AppFieldSchema.FieldId.CATEGORY).map { it.id },
            links = values.links(AppFieldSchema.FieldId.LINKS),
            logo = readBytes(state.logoUri),
            cover = readBytes(state.coverUri)
        )
    }

    private suspend fun readBytes(uriString: String?): ByteArray? {
        val uri = uriString?.let { Uri.parse(it) } ?: return null
        return withContext(Dispatchers.IO) {
            runCatching {
                dependencies.context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            }.getOrNull()
        }
    }
}
