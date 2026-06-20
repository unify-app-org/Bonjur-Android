package com.bonjur.hangouts.presentation.create.models

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

    val fields: List<Field> = listOf(
        Field(
            id = FieldId.VISIBILITY,
            label = "Who can see your hangout?",
            type = FieldType.RadioGroup(
                options = listOf(
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PUBLIC,
                        label = "Public",
                        description = "This is a Public Hangout. Feel free to use the contact " +
                            "details below to get in touch with the organizers."
                    ),
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PRIVATE,
                        label = "Private",
                        description = "Contact details and group links are only visible to " +
                            "members. Join the hangout to access this information."
                    )
                )
            )
        ),
        Field(
            id = FieldId.HANGOUT_NAME,
            label = "Hangout name",
            type = FieldType.Text(placeholder = "Study night at cafe")
        ),
        Field(
            id = FieldId.OWNER_CONTACT,
            label = "Owner contact",
            type = FieldType.Text(placeholder = "+994 123 45 67")
        ),
        Field(
            id = FieldId.CATEGORY,
            label = "Category",
            type = FieldType.ChipInput(placeholder = "Add category")
        ),
        Field(
            id = FieldId.LINKS,
            label = "Add link",
            required = false,
            type = FieldType.LinkInput(placeholder = "Add link")
        ),
        Field(
            id = FieldId.CAPACITY,
            label = "Capacity",
            required = false,
            type = FieldType.Text(placeholder = "200", keyboardType = KeyboardType.Number)
        ),
        Field(
            id = FieldId.LOCATION,
            label = "Location",
            type = FieldType.Text(placeholder = "Library")
        ),
        Field(
            id = FieldId.HANGOUT_DATE,
            label = "Start date",
            type = FieldType.DateTime(placeholder = "dd/mm/yyyy")
        ),
        Field(
            id = FieldId.RULES,
            label = "Rules",
            type = FieldType.TextArea(placeholder = "", maxLength = 500)
        ),
        Field(
            id = FieldId.ABOUT,
            label = "About",
            type = FieldType.TextArea(placeholder = "", maxLength = 500)
        )
    )
}
