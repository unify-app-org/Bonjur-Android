package com.bonjur.events.presentation.create.models

import androidx.compose.ui.text.input.KeyboardType
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.Field
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldId
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldType

/** Declarative event-create form. Mirrors the club-create schema; rendered by `FieldSchemaRouter`. */
object EventCreateSchema {

    val fields: List<Field> = listOf(
        Field(
            id = FieldId.VISIBILITY,
            label = "Visibility",
            required = false,
            type = FieldType.RadioGroup(
                options = listOf(
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PUBLIC,
                        label = "Public",
                        description = "Anyone in the community can find and join this event."
                    ),
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PRIVATE,
                        label = "Private",
                        description = "Only people you approve can join. Contact stays hidden."
                    )
                )
            )
        ),
        Field(
            id = FieldId.EVENT_NAME,
            label = "Event name",
            required = true,
            type = FieldType.Text(placeholder = "Enter event name")
        ),
        Field(
            id = FieldId.OWNER_CONTACT,
            label = "Owner contact",
            required = false,
            type = FieldType.Text(placeholder = "Enter owner contact")
        ),
        Field(
            id = FieldId.ABOUT,
            label = "About",
            required = false,
            type = FieldType.TextArea(placeholder = "About", maxLength = 500)
        ),
        Field(
            id = FieldId.CATEGORY,
            label = "Category",
            required = false,
            type = FieldType.ChipInput(placeholder = "Add category")
        ),
        Field(
            id = FieldId.LOCATION,
            label = "Location",
            required = false,
            type = FieldType.Text(placeholder = "Enter location")
        ),
        Field(
            id = FieldId.EVENT_DATE,
            label = "Date",
            required = true,
            type = FieldType.DateTime(placeholder = "Pick date & time")
        ),
        Field(
            id = FieldId.REMINDER,
            label = "Reminder",
            required = false,
            type = FieldType.Reminder(placeholder = "None")
        ),
        Field(
            id = FieldId.CAPACITY,
            label = "Capacity",
            required = false,
            type = FieldType.Text(placeholder = "Enter capacity", keyboardType = KeyboardType.Number)
        ),
        Field(
            id = FieldId.LINKS,
            label = "Add link",
            required = false,
            type = FieldType.LinkInput(placeholder = "Add link")
        ),
        Field(
            id = FieldId.ATTACHMENT,
            label = "Attachment",
            required = false,
            type = FieldType.Attachment(
                placeholder = "Add",
                description = "You can upload files up to 15 MB total for this event."
            )
        ),
        Field(
            id = FieldId.RULES,
            label = "Rules",
            required = false,
            type = FieldType.TextArea(placeholder = "Rules", maxLength = 500)
        )
    )
}
