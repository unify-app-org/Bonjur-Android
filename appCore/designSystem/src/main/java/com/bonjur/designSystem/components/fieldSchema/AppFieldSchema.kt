package com.bonjur.designSystem.components.fieldSchema

import androidx.compose.ui.text.input.KeyboardType
import com.bonjur.designSystem.commonModel.AppUIEntities

/**
 * Enum-based create-form schema. Kotlin equivalent of iOS `AppFieldSchema`.
 * A screen declares a list of [Field]; [FieldSchemaRouter] renders each one.
 */
object AppFieldSchema {

    enum class FieldId {
        COVER, VISIBILITY, CLUB_NAME, HANGOUT_NAME, OWNER_CONTACT,
        CATEGORY, CAPACITY, LINKS, LOCATION, HANGOUT_DATE, RULES, ABOUT
    }

    data class RadioOption(
        val value: AppUIEntities.AccessType,
        val label: String,
        val description: String
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

fun FieldValues.isValid(schema: List<AppFieldSchema.Field>): Boolean =
    schema.filter { it.required }.all { field ->
        when (field.type) {
            is AppFieldSchema.FieldType.Text,
            is AppFieldSchema.FieldType.TextArea -> text(field.id).trim().isNotEmpty()
            else -> true
        }
    }
