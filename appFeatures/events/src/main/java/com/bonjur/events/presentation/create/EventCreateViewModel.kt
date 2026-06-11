package com.bonjur.events.presentation.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.categorieChips.CategorySection
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.attachments
import com.bonjur.designSystem.components.fieldSchema.date
import com.bonjur.designSystem.components.fieldSchema.links
import com.bonjur.designSystem.components.fieldSchema.radio
import com.bonjur.designSystem.components.fieldSchema.reminder
import com.bonjur.designSystem.components.fieldSchema.tags
import com.bonjur.designSystem.components.fieldSchema.text
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.events.data.dataSource.EventAttachmentFile
import com.bonjur.events.domain.useCase.EventFormData
import com.bonjur.events.domain.useCase.EventsUseCase
import com.bonjur.events.presentation.create.models.EventCreateAction
import com.bonjur.events.presentation.create.models.EventCreateInputData
import com.bonjur.events.presentation.create.models.EventCreateSideEffect
import com.bonjur.events.presentation.create.models.EventCreateViewState
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<EventCreateViewState, EventCreateAction, EventCreateSideEffect>(
    EventCreateViewState(
        values = mapOf(
            AppFieldSchema.FieldId.VISIBILITY to
                AppFieldSchema.FieldValue.Radio(AppUIEntities.AccessType.PUBLIC),
            AppFieldSchema.FieldId.REMINDER to
                AppFieldSchema.FieldValue.ReminderValue(AppFieldSchema.ReminderOption.NONE)
        )
    )
) {

    data class Dependencies @Inject constructor(
        val useCase: EventsUseCase,
        @ApplicationContext val context: Context
    )

    private lateinit var inputData: EventCreateInputData
    private lateinit var navigator: Navigator
    private var didApplyPrefill = false

    fun init(inputData: EventCreateInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        if (inputData.eventId != null) {
            updateState(state.copy(isEdit = true))
        }
        fetchData()
    }

    override fun handle(action: EventCreateAction) {
        when (action) {
            EventCreateAction.FetchData -> fetchData()
            EventCreateAction.BackTapped -> navigateBack()
            EventCreateAction.ContinueTapped -> continueTapped()
            is EventCreateAction.FieldChanged ->
                updateState(state.copy(values = state.values + (action.id to action.value)))
            EventCreateAction.SelectClubTapped -> updateState(state.copy(showClubPicker = true))
            EventCreateAction.DismissClubPicker -> updateState(state.copy(showClubPicker = false))
            is EventCreateAction.SelectClub ->
                updateState(state.copy(selectedClubId = action.clubId, showClubPicker = false))
            EventCreateAction.AddCategoryTapped -> updateState(state.copy(showCategoryPicker = true))
            is EventCreateAction.CategoryToggled -> toggleCategory(action.id)
            is EventCreateAction.RemoveCategory -> removeCategory(action.id)
            EventCreateAction.DismissCategoryPicker -> dismissCategoryPicker()
            EventCreateAction.CategoryPickerDone -> dismissCategoryPicker()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            runCatching { dependencies.useCase.fetchClubsForEvents() }
                .onSuccess { clubs ->
                    updateState(
                        state.copy(
                            clubs = clubs,
                            // Mirror iOS: default to the first club when nothing is selected.
                            selectedClubId = state.selectedClubId ?: clubs.firstOrNull()?.clubId
                        )
                    )
                }
            runCatching { dependencies.useCase.getCategories() }
                .onSuccess { updateState(state.copy(categorySections = it)) }
            applyPrefill()
        }
    }

    private fun applyPrefill() {
        if (didApplyPrefill) return
        val prefill = inputData.prefill ?: return
        didApplyPrefill = true
        val sections = state.categorySections.selectIds(
            state.values.tags(AppFieldSchema.FieldId.CATEGORY).map { it.id }.toSet()
                .ifEmpty { prefill.values.tagIds() }
        )
        updateState(
            state.copy(
                selectedClubId = prefill.selectedClubId,
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
            val eventId = inputData.eventId
            if (eventId != null) submit { editEvent(eventId, it) } else submit { createEvent(it) }
        }
    }

    private suspend fun createEvent(form: EventFormData) {
        dependencies.useCase.createEvent(form)
        AppSnackBar.show(
            title = "Event created successfully",
            subtitle = "${form.name} · now active",
            style = AppSnackBar.Style.SUCCESS
        )
    }

    private suspend fun editEvent(eventId: String, form: EventFormData) {
        dependencies.useCase.editEvent(eventId, form)
        AppSnackBar.show(
            title = "Event updated successfully",
            subtitle = form.name,
            style = AppSnackBar.Style.SUCCESS
        )
    }

    private suspend inline fun submit(crossinline call: suspend (EventFormData) -> Unit) {
        val clubId = state.selectedClubId ?: return
        postEffect(EventCreateSideEffect.Loading(true))
        try {
            // Cover is optional, mirroring iOS: upload it only when the club has one.
            val background = downloadBytes(state.coverUrl)
            call(buildForm(clubId, background))
            navigator.navigateUp()
        } catch (e: Exception) {
            postEffect(EventCreateSideEffect.Error(e.message ?: "Unknown error"))
        } finally {
            postEffect(EventCreateSideEffect.Loading(false))
        }
    }

    private suspend fun buildForm(clubId: Int, background: ByteArray?): EventFormData {
        val v = state.values
        return EventFormData(
            clubId = clubId,
            name = v.text(AppFieldSchema.FieldId.EVENT_NAME),
            about = v.text(AppFieldSchema.FieldId.ABOUT),
            location = v.text(AppFieldSchema.FieldId.LOCATION),
            ownerContact = v.text(AppFieldSchema.FieldId.OWNER_CONTACT),
            capacity = v.text(AppFieldSchema.FieldId.CAPACITY).toIntOrNull(),
            rule = v.text(AppFieldSchema.FieldId.RULES),
            isPublic = v.radio(AppFieldSchema.FieldId.VISIBILITY) == AppUIEntities.AccessType.PUBLIC,
            eventDate = v.date(AppFieldSchema.FieldId.EVENT_DATE),
            reminderTime = v.reminder(AppFieldSchema.FieldId.REMINDER).name,
            categoryIds = v.tags(AppFieldSchema.FieldId.CATEGORY).map { it.id },
            links = v.links(AppFieldSchema.FieldId.LINKS),
            background = background,
            attachments = readAttachments(v.attachments(AppFieldSchema.FieldId.ATTACHMENT))
        )
    }

    private suspend fun readAttachments(
        items: List<AppFieldSchema.AttachmentItem>
    ): List<EventAttachmentFile> = withContext(Dispatchers.IO) {
        items.mapNotNull { item ->
            runCatching {
                val uri = Uri.parse(item.uri)
                val bytes = dependencies.context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: return@runCatching null
                val mime = dependencies.context.contentResolver.getType(uri) ?: "application/octet-stream"
                EventAttachmentFile(name = item.name, mimeType = mime, bytes = bytes)
            }.getOrNull()
        }
    }

    private suspend fun downloadBytes(url: String?): ByteArray? {
        if (url.isNullOrBlank()) return null
        return withContext(Dispatchers.IO) {
            runCatching { URL(url).openStream().use { it.readBytes() } }.getOrNull()
        }
    }
}
