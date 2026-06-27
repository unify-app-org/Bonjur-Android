package com.bonjur.hangouts.presentation.create

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.date
import com.bonjur.designSystem.components.fieldSchema.links
import com.bonjur.designSystem.components.fieldSchema.radio
import com.bonjur.designSystem.components.fieldSchema.tags
import com.bonjur.designSystem.components.fieldSchema.text
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.hangouts.domain.useCase.HangoutFormData
import com.bonjur.hangouts.domain.useCase.HangoutsUseCase
import com.bonjur.hangouts.presentation.create.models.HangoutCreateAction
import com.bonjur.hangouts.presentation.create.models.HangoutCreateInputData
import com.bonjur.hangouts.presentation.create.models.HangoutCreateSideEffect
import com.bonjur.hangouts.presentation.create.models.HangoutCreateViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class HangoutCreateViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<HangoutCreateViewState, HangoutCreateAction, HangoutCreateSideEffect>(
    HangoutCreateViewState(
        values = mapOf(
            AppFieldSchema.FieldId.VISIBILITY to
                AppFieldSchema.FieldValue.Radio(AppUIEntities.AccessType.PUBLIC),
            // Mirrors iOS default `.hangoutDate: .date(Date())` (today).
            AppFieldSchema.FieldId.HANGOUT_DATE to
                AppFieldSchema.FieldValue.DateValue(nowPickerDate())
        )
    )
) {

    data class Dependencies @Inject constructor(
        val useCase: HangoutsUseCase
    )

    private lateinit var inputData: HangoutCreateInputData
    private lateinit var navigator: Navigator
    private var didApplyPrefill = false

    fun init(inputData: HangoutCreateInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        if (inputData.hangoutId != null) {
            updateState(state.copy(isEdit = true))
        }
        fetchData()
    }

    override fun handle(action: HangoutCreateAction) {
        when (action) {
            HangoutCreateAction.FetchData -> fetchData()
            HangoutCreateAction.BackTapped -> navigateBack()
            HangoutCreateAction.ContinueTapped -> continueTapped()
            is HangoutCreateAction.FieldChanged ->
                updateState(state.copy(values = state.values + (action.id to action.value)))
            HangoutCreateAction.AddCategoryTapped -> updateState(state.copy(showCategoryPicker = true))
            is HangoutCreateAction.CategoryToggled -> toggleCategory(action.id)
            is HangoutCreateAction.RemoveCategory -> removeCategory(action.id)
            HangoutCreateAction.DismissCategoryPicker -> dismissCategoryPicker()
            HangoutCreateAction.CategoryPickerDone -> dismissCategoryPicker()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            runCatching { dependencies.useCase.getCategories() }
                .onSuccess { updateState(state.copy(categorySections = it)) }
                .onFailure {
                    AppSnackBar.show(title = "Couldn't load categories", style = AppSnackBar.Style.ERROR)
                }
            applyPrefill()
        }
    }

    private fun applyPrefill() {
        if (didApplyPrefill) return
        val prefill = inputData.prefill ?: return
        didApplyPrefill = true
        val sections = state.categorySections.selectIds(prefill.values.tagIds())
        updateState(
            state.copy(
                values = state.values + prefill.values,
                categorySections = sections
            )
        )
    }

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }

    // MARK: - Categories

    private fun toggleCategory(id: Int) {
        val sections = state.categorySections.map { section ->
            section.copy(categories = section.categories.map {
                if (it.id == id) it.copy(selected = !it.selected) else it
            })
        }
        updateState(state.copy(categorySections = sections))
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

    private fun List<CategorySection>.setSelected(id: Int, selected: Boolean): List<CategorySection> =
        map { section ->
            section.copy(categories = section.categories.map {
                if (it.id == id) it.copy(selected = selected) else it
            })
        }

    private fun List<CategorySection>.selectIds(ids: Set<Int>): List<CategorySection> =
        map { section ->
            section.copy(categories = section.categories.map { it.copy(selected = it.id in ids) })
        }

    private fun Map<AppFieldSchema.FieldId, AppFieldSchema.FieldValue>.tagIds(): Set<Int> =
        (this[AppFieldSchema.FieldId.CATEGORY] as? AppFieldSchema.FieldValue.Tags)
            ?.value?.map { it.id }?.toSet() ?: emptySet()

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
            val hangoutId = inputData.hangoutId
            if (hangoutId != null) submit { editHangout(hangoutId, it) } else submit { createHangout(it) }
        }
    }

    private suspend fun createHangout(form: HangoutFormData) {
        dependencies.useCase.createHangout(form)
        AppSnackBar.show(
            title = "Hangout created successfully",
            subtitle = form.name,
            style = AppSnackBar.Style.SUCCESS
        )
    }

    private suspend fun editHangout(hangoutId: String, form: HangoutFormData) {
        dependencies.useCase.editHangout(hangoutId, form)
        AppSnackBar.show(
            title = "Hangout updated successfully",
            subtitle = form.name,
            style = AppSnackBar.Style.SUCCESS
        )
    }

    private suspend inline fun submit(crossinline call: suspend (HangoutFormData) -> Unit) {
        postEffect(HangoutCreateSideEffect.Loading(true))
        try {
            call(buildForm())
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(HangoutCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(HangoutCreateSideEffect.Loading(false))
        }
    }

    private fun buildForm(): HangoutFormData {
        val v = state.values
        return HangoutFormData(
            name = v.text(AppFieldSchema.FieldId.HANGOUT_NAME),
            about = v.text(AppFieldSchema.FieldId.ABOUT),
            location = v.text(AppFieldSchema.FieldId.LOCATION),
            ownerContact = v.text(AppFieldSchema.FieldId.OWNER_CONTACT),
            capacity = v.text(AppFieldSchema.FieldId.CAPACITY).toIntOrNull(),
            rules = v.text(AppFieldSchema.FieldId.RULES),
            isPublic = v.radio(AppFieldSchema.FieldId.VISIBILITY) == AppUIEntities.AccessType.PUBLIC,
            hangoutDate = v.date(AppFieldSchema.FieldId.HANGOUT_DATE),
            categoryIds = v.tags(AppFieldSchema.FieldId.CATEGORY).map { it.id },
            links = v.links(AppFieldSchema.FieldId.LINKS)
        )
    }

    companion object {
        private fun nowPickerDate(): String =
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(Date())
    }
}
