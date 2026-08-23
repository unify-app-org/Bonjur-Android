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

/**
 * Declarative club-create form. Field-for-field mirror of iOS
 * `ClubsDataSourceImpl.fetchCreate()`.
 *
 * Field order and `required` flags are the canonical spine shared by all three create
 * forms (club / event / hangout) on both platforms. Body order is:
 * what → when → where → how many → describe → extras → contact. Everything above
 * `CLUB_NAME` (cover, visibility) is the fixed top block and is not part of that order.
 */
object ClubCreateSchema {

    // `get()`, not a stored value: the labels resolve through LanguageManager and a
    // list built once at class load would freeze in whatever language was active then.
    val fields: List<Field> get() = listOf(
        // Top block (fixed)
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
            required = true,
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
        // Body (canonical order)
        Field(
            id = FieldId.CLUB_NAME,
            label = LanguageManager.string(R.string.clubs_name_label),
            required = true,
            hint = LanguageManager.string(R.string.clubs_name_locked_hint),
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_name_placeholder))
        ),
        Field(
            id = FieldId.CATEGORY,
            label = LanguageManager.string(R.string.clubs_category_label),
            required = true,
            type = FieldType.ChipInput(placeholder = LanguageManager.string(R.string.clubs_add_category))
        ),
        Field(
            id = FieldId.LOCATION,
            label = LanguageManager.string(R.string.clubs_location_label),
            required = true,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_location_placeholder))
        ),
        Field(
            id = FieldId.CAPACITY,
            label = LanguageManager.string(R.string.clubs_capacity_label),
            required = false,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_capacity_placeholder), keyboardType = KeyboardType.Number)
        ),
        Field(
            id = FieldId.ABOUT,
            label = LanguageManager.string(R.string.clubs_about_label),
            required = true,
            type = FieldType.TextArea(placeholder = LanguageManager.string(R.string.clubs_about_label), maxLength = 500)
        ),
        Field(
            id = FieldId.RULES,
            label = LanguageManager.string(R.string.clubs_rules_label),
            required = true,
            type = FieldType.TextArea(placeholder = LanguageManager.string(R.string.clubs_rules_label), maxLength = 500)
        ),
        Field(
            id = FieldId.LINKS,
            label = LanguageManager.string(R.string.clubs_add_link),
            required = false,
            type = FieldType.LinkInput(placeholder = LanguageManager.string(R.string.clubs_add_link))
        ),
        Field(
            id = FieldId.OWNER_CONTACT,
            label = LanguageManager.string(R.string.clubs_owner_contact_label),
            required = true,
            type = FieldType.Text(placeholder = LanguageManager.string(R.string.clubs_owner_contact_placeholder))
        )
    )
}
