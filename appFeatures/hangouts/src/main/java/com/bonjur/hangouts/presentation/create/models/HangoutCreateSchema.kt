package com.bonjur.hangouts.presentation.create.models

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.hangouts.R
import androidx.compose.ui.text.input.KeyboardType
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.Field
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldId
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldType

/**
 * Declarative hangout-create form. Field-for-field mirror of iOS
 * `HangoutsDataSourceImpl.fetchCreate()`. Rendered by `FieldSchemaRouter`.
 * No club / reminder / attachment (hangouts have none).
 */
object HangoutCreateSchema {

    // `get()`, not a stored value: the labels resolve through LanguageManager and a
    // list built once at class load would freeze in whatever language was active then.
    val fields: List<Field> get() = listOf(
        Field(
            id = FieldId.VISIBILITY,
            label = LanguageManager.string(R.string.hangouts_visibility_q),
            type = FieldType.RadioGroup(
                options = listOf(
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PUBLIC,
                        label = LanguageManager.string(R.string.hangouts_public),
                        description = LanguageManager.string(R.string.hangouts_public_desc)
                    ),
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PRIVATE,
                        label = LanguageManager.string(R.string.hangouts_private),
                        description = LanguageManager.string(R.string.hangouts_private_desc)
                    )
                )
            )
        ),
        Field(
            id = FieldId.HANGOUT_NAME,
            label = LanguageManager.string(R.string.hangouts_name_label),
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.hangouts_name_ph))
        ),
        Field(
            id = FieldId.OWNER_CONTACT,
            label = LanguageManager.string(R.string.hangouts_owner_contact_label),
            type = FieldType.Text(placeholder = "+994 123 45 67")
        ),
        Field(
            id = FieldId.CATEGORY,
            label = LanguageManager.string(R.string.hangouts_category_label),
            type = FieldType.ChipInput(placeholder = LanguageManager.string(R.string.hangouts_add_category))
        ),
        Field(
            id = FieldId.LINKS,
            label = LanguageManager.string(R.string.hangouts_add_link),
            required = false,
            type = FieldType.LinkInput(placeholder = LanguageManager.string(R.string.hangouts_add_link))
        ),
        Field(
            id = FieldId.CAPACITY,
            label = LanguageManager.string(R.string.hangouts_capacity_label),
            required = false,
            type = FieldType.Text(placeholder = "200", keyboardType = KeyboardType.Number)
        ),
        Field(
            id = FieldId.LOCATION,
            label = LanguageManager.string(R.string.hangouts_location_label),
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.hangouts_location_ph))
        ),
        Field(
            id = FieldId.HANGOUT_DATE,
            label = LanguageManager.string(R.string.hangouts_start_date),
            type = FieldType.DateTime(placeholder = "dd/mm/yyyy")
        ),
        Field(
            id = FieldId.RULES,
            label = LanguageManager.string(R.string.hangouts_rules_label),
            type = FieldType.TextArea(placeholder = "", maxLength = 500)
        ),
        Field(
            id = FieldId.ABOUT,
            label = LanguageManager.string(R.string.hangouts_about_label),
            type = FieldType.TextArea(placeholder = "", maxLength = 500)
        )
    )
}
