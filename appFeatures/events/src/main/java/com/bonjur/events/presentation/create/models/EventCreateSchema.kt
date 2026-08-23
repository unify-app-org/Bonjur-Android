package com.bonjur.events.presentation.create.models

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.events.R
import androidx.compose.ui.text.input.KeyboardType
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.Field
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldId
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldType

/**
 * Declarative event-create form. Field-for-field mirror of iOS `EventsCreate.schema`;
 * rendered by `FieldSchemaRouter`.
 *
 * Same canonical field order and `required` flags as the club and hangout forms —
 * what → when → where → how many → describe → extras → contact. The club picker and
 * `VISIBILITY` are the fixed top block (the picker lives in the screen, above this schema).
 */
object EventCreateSchema {

    // `get()`, not a stored value: the labels resolve through LanguageManager and a
    // list built once at class load would freeze in whatever language was active then.
    val fields: List<Field> get() = listOf(
        // Top block (fixed)
        Field(
            id = FieldId.VISIBILITY,
            label = LanguageManager.string(R.string.events_visibility),
            required = true,
            type = FieldType.RadioGroup(
                options = listOf(
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PUBLIC,
                        label = LanguageManager.string(R.string.events_public),
                        description = LanguageManager.string(R.string.events_public_desc)
                    ),
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PRIVATE,
                        label = LanguageManager.string(R.string.events_private),
                        description = LanguageManager.string(R.string.events_private_desc)
                    )
                )
            )
        ),
        // Body (canonical order)
        Field(
            id = FieldId.EVENT_NAME,
            label = LanguageManager.string(R.string.events_name_label),
            required = true,
            hint = LanguageManager.string(R.string.events_name_locked_hint),
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.events_name_ph))
        ),
        Field(
            id = FieldId.CATEGORY,
            label = LanguageManager.string(R.string.events_category_label),
            required = true,
            type = FieldType.ChipInput(placeholder = LanguageManager.string(R.string.events_add_category))
        ),
        Field(
            id = FieldId.EVENT_DATE,
            label = LanguageManager.string(R.string.events_date_label),
            required = true,
            type = FieldType.DateTime(placeholder = LanguageManager.string(R.string.events_pick_datetime))
        ),
        Field(
            id = FieldId.REMINDER,
            label = LanguageManager.string(R.string.events_reminder),
            required = false,
            type = FieldType.Reminder(placeholder = LanguageManager.string(R.string.events_none))
        ),
        Field(
            id = FieldId.LOCATION,
            label = LanguageManager.string(R.string.events_location_label),
            required = true,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.events_location_ph))
        ),
        Field(
            id = FieldId.CAPACITY,
            label = LanguageManager.string(R.string.events_capacity_label),
            required = false,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.events_capacity_ph), keyboardType = KeyboardType.Number)
        ),
        Field(
            id = FieldId.ABOUT,
            label = LanguageManager.string(R.string.events_about_label),
            required = true,
            type = FieldType.TextArea(placeholder = LanguageManager.string(R.string.events_about_label), maxLength = 500)
        ),
        Field(
            id = FieldId.RULES,
            label = LanguageManager.string(R.string.events_rules_label),
            required = true,
            type = FieldType.TextArea(placeholder = LanguageManager.string(R.string.events_rules_label), maxLength = 500)
        ),
        Field(
            id = FieldId.ATTACHMENT,
            label = LanguageManager.string(R.string.events_attachment),
            required = false,
            type = FieldType.Attachment(
                placeholder = LanguageManager.string(R.string.events_add),
                description = LanguageManager.string(R.string.events_attachment_hint)
            )
        ),
        Field(
            id = FieldId.LINKS,
            label = LanguageManager.string(R.string.events_add_link),
            required = false,
            type = FieldType.LinkInput(placeholder = LanguageManager.string(R.string.events_add_link))
        ),
        Field(
            id = FieldId.OWNER_CONTACT,
            label = LanguageManager.string(R.string.events_owner_contact_label),
            required = true,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.events_owner_contact_ph))
        )
    )
}
