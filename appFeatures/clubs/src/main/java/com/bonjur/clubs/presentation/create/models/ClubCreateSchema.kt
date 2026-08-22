package com.bonjur.clubs.presentation.create.models

import com.bonjur.designsystem.R as DesignR
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.clubs.R
import androidx.compose.ui.text.input.KeyboardType
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.Field
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldId
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldType

/** Declarative club-create form. Mirrors iOS `clubsCreateSchema`. */
object ClubCreateSchema {

    // `get()`, not a stored value: the labels resolve through LanguageManager and a
    // list built once at class load would freeze in whatever language was active then.
    val fields: List<Field> get() = listOf(
        Field(
            id = FieldId.COVER,
            label = LanguageManager.string(DesignR.string.common_cover),
            required = false,
            type = FieldType.CoverPicker(
                AppFieldSchema.CoverItem(
                    title = LanguageManager.string(R.string.clubs_cover_color_title),
                    description = LanguageManager.string(R.string.clubs_cover_color_desc),
                    covers = AppFieldSchema.defaultCovers
                )
            )
        ),
        Field(
            id = FieldId.VISIBILITY,
            label = LanguageManager.string(R.string.clubs_visibility_label),
            required = false,
            type = FieldType.RadioGroup(
                options = listOf(
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PUBLIC,
                        label = LanguageManager.string(R.string.clubs_public),
                        description = LanguageManager.string(R.string.clubs_public_desc_short)
                    ),
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PRIVATE,
                        label = LanguageManager.string(R.string.clubs_private),
                        description = LanguageManager.string(R.string.clubs_private_desc_short)
                    )
                )
            )
        ),
        Field(
            id = FieldId.CLUB_NAME,
            label = LanguageManager.string(R.string.clubs_name_label),
            required = true,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_name_placeholder))
        ),
        Field(
            id = FieldId.OWNER_CONTACT,
            label = LanguageManager.string(R.string.clubs_owner_contact_label),
            required = false,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_owner_contact_placeholder))
        ),
        Field(
            id = FieldId.CATEGORY,
            label = LanguageManager.string(R.string.clubs_category_label),
            required = true,
            type = FieldType.ChipInput(placeholder = LanguageManager.string(R.string.clubs_add_category))
        ),
        Field(
            id = FieldId.LINKS,
            label = LanguageManager.string(R.string.clubs_add_link),
            required = false,
            type = FieldType.LinkInput(placeholder = LanguageManager.string(R.string.clubs_add_link))
        ),
        Field(
            id = FieldId.ABOUT,
            label = LanguageManager.string(R.string.clubs_about_label),
            required = false,
            type = FieldType.TextArea(placeholder = LanguageManager.string(R.string.clubs_about_label), maxLength = 500)
        ),
        Field(
            id = FieldId.LOCATION,
            label = LanguageManager.string(R.string.clubs_location_label),
            required = false,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_location_placeholder))
        ),
        Field(
            id = FieldId.CAPACITY,
            label = LanguageManager.string(R.string.clubs_capacity_label),
            required = false,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_capacity_placeholder), keyboardType = KeyboardType.Number)
        ),
        Field(
            id = FieldId.RULES,
            label = LanguageManager.string(R.string.clubs_rules_label),
            required = false,
            type = FieldType.TextArea(placeholder = LanguageManager.string(R.string.clubs_rules_label), maxLength = 500)
        )
    )
}
