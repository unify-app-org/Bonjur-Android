package com.bonjur.designSystem.components.fieldSchema

import androidx.compose.ui.text.input.KeyboardType
import com.bonjur.designSystem.commonModel.AppUIEntities
import java.util.UUID

/**
 * Enum-based create-form schema. Kotlin equivalent of iOS `AppFieldSchema`.
 * A screen declares a list of [Field]; [FieldSchemaRouter] renders each one.
 */
object AppFieldSchema {

    enum class FieldId {
        COVER, VISIBILITY, CLUB_NAME, HANGOUT_NAME, EVENT_NAME, OWNER_CONTACT,
        CATEGORY, CAPACITY, LINKS, ATTACHMENT, LOCATION, HANGOUT_DATE, EVENT_DATE,
        REMINDER, RULES, ABOUT, CLUB
    }

    /** Reminder offsets offered by the reminder bottom sheet. */
    enum class ReminderOption(val label: String) {
        NONE("None"),
        AT_EVENT_TIME("At time of event"),
        FIFTEEN_MINUTES_BEFORE("15 minutes before"),
        THIRTY_MINUTES_BEFORE("30 minutes before"),
        ONE_HOUR_BEFORE("1 hour before"),
        ONE_DAY_BEFORE("1 day before")
    }

    data class RadioOption(
        val value: AppUIEntities.AccessType,
        val label: String,
        val description: String
    )

    /** Selected category chip. Kotlin equivalent of iOS `AppFieldSchema.TagItem`. */
    data class TagItem(
        val id: Int,
        val label: String
    )

    /** A link entry. Kotlin equivalent of iOS `AppFieldSchema.LinkItem`. */
    data class LinkItem(
        val id: String = UUID.randomUUID().toString(),
        val type: String,
        val name: String,
        val url: String
    )

    /** A picked attachment. Kotlin equivalent of iOS `AttachmentItem`. */
    data class AttachmentItem(
        val id: String = UUID.randomUUID().toString(),
        val name: String,
        val uri: String,
        val size: Long
    )

    data class CoverItem(
        val title: String,
        val description: String,
        val covers: List<AppUIEntities.BackgroundType>
    )

    sealed class FieldType {
        data class CoverPicker(val item: CoverItem) : FieldType()
        data class RadioGroup(val options: List<RadioOption>) : FieldType()
        data class Text(
            val placeholder: String,
            val keyboardType: KeyboardType = KeyboardType.Text
        ) : FieldType()
        data class TextArea(val placeholder: String, val maxLength: Int) : FieldType()
        data class Date(val placeholder: String) : FieldType()
        /** Combined date + time picker rendered as a single capsule field. */
        data class DateTime(val placeholder: String) : FieldType()
        /** Capsule field opening a single-select bottom sheet of [options]. */
        data class Reminder(
            val placeholder: String,
            val options: List<ReminderOption> = ReminderOption.entries
        ) : FieldType()
        data class ChipInput(val placeholder: String) : FieldType()
        data class LinkInput(val placeholder: String) : FieldType()
        /** File picker rendering a list of attachment rows plus an "Add" button. */
        data class Attachment(
            val placeholder: String,
            val description: String
        ) : FieldType()
    }

    data class Field(
        val id: FieldId,
        val label: String,
        val type: FieldType,
        val required: Boolean = true
    )

    sealed class FieldValue {
        data class TextValue(val value: String) : FieldValue()
        data class Cover(val value: AppUIEntities.BackgroundType) : FieldValue()
        data class Radio(val value: AppUIEntities.AccessType) : FieldValue()
        data class DateValue(val value: String) : FieldValue()
        data class ReminderValue(val value: ReminderOption) : FieldValue()
        data class Tags(val value: List<TagItem>) : FieldValue()
        data class Links(val value: List<LinkItem>) : FieldValue()
        data class Attachments(val value: List<AttachmentItem>) : FieldValue()
    }

    /** Default background colours offered by the cover picker (matches iOS order). */
    val defaultCovers: List<AppUIEntities.BackgroundType> = listOf(
        AppUIEntities.BackgroundType.Primary,
        AppUIEntities.BackgroundType.Secondary,
        AppUIEntities.BackgroundType.Tertiary,
        AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Orange),
        AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Red),
        AppUIEntities.BackgroundType.CustomColor(AppUIEntities.ColorType.Pink)
    )
}

// MARK: - Value accessors (mirror iOS dictionary helpers)

typealias FieldValues = Map<AppFieldSchema.FieldId, AppFieldSchema.FieldValue>

fun FieldValues.text(id: AppFieldSchema.FieldId): String =
    (this[id] as? AppFieldSchema.FieldValue.TextValue)?.value ?: ""

fun FieldValues.cover(id: AppFieldSchema.FieldId): AppUIEntities.BackgroundType =
    (this[id] as? AppFieldSchema.FieldValue.Cover)?.value ?: AppUIEntities.BackgroundType.Primary

fun FieldValues.radio(id: AppFieldSchema.FieldId): AppUIEntities.AccessType =
    (this[id] as? AppFieldSchema.FieldValue.Radio)?.value ?: AppUIEntities.AccessType.PUBLIC

fun FieldValues.date(id: AppFieldSchema.FieldId): String =
    (this[id] as? AppFieldSchema.FieldValue.DateValue)?.value ?: ""

fun FieldValues.reminder(id: AppFieldSchema.FieldId): AppFieldSchema.ReminderOption =
    (this[id] as? AppFieldSchema.FieldValue.ReminderValue)?.value ?: AppFieldSchema.ReminderOption.NONE

fun FieldValues.tags(id: AppFieldSchema.FieldId): List<AppFieldSchema.TagItem> =
    (this[id] as? AppFieldSchema.FieldValue.Tags)?.value ?: emptyList()

fun FieldValues.links(id: AppFieldSchema.FieldId): List<AppFieldSchema.LinkItem> =
    (this[id] as? AppFieldSchema.FieldValue.Links)?.value ?: emptyList()

fun FieldValues.attachments(id: AppFieldSchema.FieldId): List<AppFieldSchema.AttachmentItem> =
    (this[id] as? AppFieldSchema.FieldValue.Attachments)?.value ?: emptyList()

fun FieldValues.isValid(schema: List<AppFieldSchema.Field>): Boolean =
    schema.filter { it.required }.all { field ->
        when (field.type) {
            is AppFieldSchema.FieldType.Text,
            is AppFieldSchema.FieldType.TextArea -> text(field.id).trim().isNotEmpty()
            is AppFieldSchema.FieldType.Date,
            is AppFieldSchema.FieldType.DateTime -> date(field.id).isNotEmpty()
            is AppFieldSchema.FieldType.ChipInput -> tags(field.id).isNotEmpty()
            is AppFieldSchema.FieldType.LinkInput -> links(field.id).isNotEmpty()
            else -> true
        }
    }
