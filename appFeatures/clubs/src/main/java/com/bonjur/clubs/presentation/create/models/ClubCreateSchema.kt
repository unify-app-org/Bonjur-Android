package com.bonjur.clubs.presentation.create.models

import androidx.compose.ui.text.input.KeyboardType
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.Field
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldId
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema.FieldType

/** Declarative club-create form. Mirrors iOS `clubsCreateSchema`. */
object ClubCreateSchema {

    val fields: List<Field> = listOf(
        Field(
            id = FieldId.COVER,
            label = "Cover",
            required = false,
            type = FieldType.CoverPicker(
                AppFieldSchema.CoverItem(
                    title = "Cover color",
                    description = "Pick a background colour for your club card.",
                    covers = AppFieldSchema.defaultCovers
                )
            )
        ),
        Field(
            id = FieldId.VISIBILITY,
            label = "Visibility",
            required = false,
            type = FieldType.RadioGroup(
                options = listOf(
                    AppFieldSchema.RadioOption(
                        value = AppUIEntities.AccessType.PUBLIC,
                        label = "Public",
                        description = "Anyone in the community can find and join this club."
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
            id = FieldId.CLUB_NAME,
            label = "Club name",
            required = true,
            type = FieldType.Text(placeholder = "Enter club name")
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
            id = FieldId.LOCATION,
            label = "Location",
            required = false,
            type = FieldType.Text(placeholder = "Enter location")
        ),
        Field(
            id = FieldId.CAPACITY,
            label = "Capacity",
            required = false,
            type = FieldType.Text(placeholder = "Enter capacity", keyboardType = KeyboardType.Number)
        ),
        Field(
            id = FieldId.RULES,
            label = "Rules",
            required = false,
            type = FieldType.TextArea(placeholder = "Rules", maxLength = 500)
        )
    )
}
