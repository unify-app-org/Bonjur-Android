package com.bonjur.designSystem.components.fieldSchema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.textField.AppTextField
import com.bonjur.designSystem.components.textField.AppTextFieldModel
import com.bonjur.designSystem.components.textView.TextView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * Renders one [AppFieldSchema.Field] based on its type. Kotlin equivalent of iOS `FieldSchemaRouter`.
 */
@Composable
fun FieldSchemaRouter(
    field: AppFieldSchema.Field,
    values: FieldValues,
    onChange: (AppFieldSchema.FieldId, AppFieldSchema.FieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    when (val type = field.type) {
        is AppFieldSchema.FieldType.CoverPicker -> CoverPickerField(
            item = type.item,
            selected = values.cover(field.id),
            onChange = { onChange(field.id, AppFieldSchema.FieldValue.Cover(it)) },
            modifier = modifier
        )

        is AppFieldSchema.FieldType.RadioGroup -> RadioGroupField(
            field = field,
            options = type.options,
            selected = values.radio(field.id),
            onChange = { onChange(field.id, AppFieldSchema.FieldValue.Radio(it)) },
            modifier = modifier
        )

        is AppFieldSchema.FieldType.Text -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FieldLabel(field)
            AppTextField(
                text = values.text(field.id),
                onTextChange = { onChange(field.id, AppFieldSchema.FieldValue.TextValue(it)) },
                placeHolder = type.placeholder,
                model = AppTextFieldModel(keyboardType = type.keyboardType)
            )
        }

        is AppFieldSchema.FieldType.TextArea -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FieldLabel(field)
            TextView(
                text = values.text(field.id),
                onTextChange = { onChange(field.id, AppFieldSchema.FieldValue.TextValue(it)) },
                characterLimit = type.maxLength,
                placeholder = type.placeholder,
                modifier = Modifier.height(120.dp)
            )
        }

        is AppFieldSchema.FieldType.Date -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FieldLabel(field)
            AppTextField(
                text = values.date(field.id),
                onTextChange = { onChange(field.id, AppFieldSchema.FieldValue.DateValue(it)) },
                placeHolder = type.placeholder,
                model = AppTextFieldModel()
            )
        }
    }
}

@Composable
private fun CoverPickerField(
    item: AppFieldSchema.CoverItem,
    selected: AppUIEntities.BackgroundType,
    onChange: (AppUIEntities.BackgroundType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = item.title, style = AppTypography.HeadingMd.medium, color = Palette.blackHigh)
        Text(
            text = item.description,
            style = AppTypography.BodyTextSm.regular,
            color = Palette.blackMedium
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item.covers.forEach { cover ->
                val isSelected = cover == selected
                Box(
                    modifier = Modifier
                        .size(width = 114.dp, height = 66.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cover.bgColor)
                        .border(
                            width = if (isSelected) 2.5.dp else 0.dp,
                            color = if (isSelected) Palette.blackHigh else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onChange(cover) }
                )
            }
        }
    }
}

@Composable
private fun RadioGroupField(
    field: AppFieldSchema.Field,
    options: List<AppFieldSchema.RadioOption>,
    selected: AppUIEntities.AccessType,
    onChange: (AppUIEntities.AccessType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        FieldLabel(field)
        options.forEach { option ->
            val isSelected = option.value == selected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChange(option.value) },
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(20.dp)
                        .border(
                            2.dp,
                            if (isSelected) Palette.green900 else Palette.grayTeritary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Palette.green900, CircleShape)
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = option.label,
                        style = AppTypography.BodyTextMd.semiBold,
                        color = Palette.blackHigh
                    )
                    Text(
                        text = option.description,
                        style = AppTypography.BodyTextSm.regular,
                        color = Palette.blackMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(field: AppFieldSchema.Field) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = field.label, style = AppTypography.HeadingMd.medium, color = Palette.blackHigh)
        if (field.required) {
            Text(text = "*", style = AppTypography.HeadingMd.medium, color = Palette.green900)
        } else {
            Text(
                text = "(optional)",
                style = AppTypography.BodyTextSm.regular,
                color = Palette.blackMedium
            )
        }
    }
}
