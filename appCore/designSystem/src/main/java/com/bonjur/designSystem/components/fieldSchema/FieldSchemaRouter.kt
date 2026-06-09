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
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.categorieChips.CategoriesChipsView
import com.bonjur.designSystem.components.flowLayout.FlowLayout
import com.bonjur.designSystem.components.links.AppLinksField
import com.bonjur.designSystem.components.textField.AppTextField
import com.bonjur.designSystem.components.textField.AppTextFieldModel
import com.bonjur.designSystem.components.textView.TextView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Renders one [AppFieldSchema.Field] based on its type. Kotlin equivalent of iOS `FieldSchemaRouter`.
 */
@Composable
fun FieldSchemaRouter(
    field: AppFieldSchema.Field,
    values: FieldValues,
    onChange: (AppFieldSchema.FieldId, AppFieldSchema.FieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onAddCategory: () -> Unit = {},
    onRemoveCategory: (Int) -> Unit = {}
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

        is AppFieldSchema.FieldType.DateTime -> DateTimeField(
            field = field,
            value = values.date(field.id),
            placeholder = type.placeholder,
            onChange = { onChange(field.id, AppFieldSchema.FieldValue.DateValue(it)) },
            modifier = modifier
        )

        is AppFieldSchema.FieldType.Reminder -> ReminderField(
            field = field,
            placeholder = type.placeholder,
            options = type.options,
            selected = values.reminder(field.id),
            onChange = { onChange(field.id, AppFieldSchema.FieldValue.ReminderValue(it)) },
            modifier = modifier
        )

        is AppFieldSchema.FieldType.ChipInput -> CategorySelectionField(
            title = field.label,
            addTitle = type.placeholder,
            categories = values.tags(field.id),
            onAdd = onAddCategory,
            onRemove = onRemoveCategory,
            modifier = modifier
        )

        is AppFieldSchema.FieldType.LinkInput -> AppLinksField(
            title = field.label,
            addTitle = type.placeholder,
            links = values.links(field.id),
            onChange = { onChange(field.id, AppFieldSchema.FieldValue.Links(it)) },
            modifier = modifier
        )

        is AppFieldSchema.FieldType.Attachment -> AttachmentField(
            field = field,
            placeholder = type.placeholder,
            description = type.description,
            attachments = values.attachments(field.id),
            onChange = { onChange(field.id, AppFieldSchema.FieldValue.Attachments(it)) },
            modifier = modifier
        )
    }
}

/** File picker: shows attachment rows + an "Add" button. Mirrors iOS `AttachmentField`. */
@Composable
private fun AttachmentField(
    field: AppFieldSchema.Field,
    placeholder: String,
    description: String,
    attachments: List<AppFieldSchema.AttachmentItem>,
    onChange: (List<AppFieldSchema.AttachmentItem>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNullOrEmpty()) return@rememberLauncherForActivityResult
        val picked = uris.map { uri ->
            var name = "Attachment"
            var size = 0L
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
                if (cursor.moveToFirst()) {
                    if (nameIndex >= 0) name = cursor.getString(nameIndex) ?: name
                    if (sizeIndex >= 0) size = cursor.getLong(sizeIndex)
                }
            }
            AppFieldSchema.AttachmentItem(name = name, uri = uri.toString(), size = size)
        }
        onChange(attachments + picked)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FieldLabel(field)
        Text(
            text = description,
            style = AppTypography.BodyTextSm.regular,
            color = Palette.blackMedium
        )

        attachments.forEach { item ->
            com.bonjur.designSystem.components.attachments.AttachmentItemView(
                model = com.bonjur.designSystem.components.attachments.AttachmentItemModel(
                    id = item.id.hashCode(),
                    name = item.name,
                    size = formatBytes(item.size),
                    canEdit = true
                ),
                onDeleteClick = { onChange(attachments.filterNot { it.id == item.id }) }
            )
        }

        AppButton(
            title = placeholder,
            model = AppButtonModel(
                type = com.bonjur.designSystem.components.button.ButtonType.Secondary,
                contentSize = ContentSize.Fill
            ),
            onClick = { launcher.launch("*/*") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun formatBytes(bytes: Long): String = when {
    bytes <= 0 -> ""
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> "${bytes / 1024} KB"
    else -> "${bytes / (1024 * 1024)} MB"
}

@Composable
private fun CategorySelectionField(
    title: String,
    addTitle: String,
    categories: List<AppFieldSchema.TagItem>,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = title, style = AppTypography.HeadingMd.medium, color = Palette.blackHigh)

        if (categories.isNotEmpty()) {
            FlowLayout(items = categories, spacing = 12) { tag ->
                CategoriesChipsView(
                    model = CategoriesChipModel(id = tag.id, title = tag.label, selected = true),
                    onClick = { onRemove(tag.id) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(Palette.white)
                .border(0.5.dp, Palette.graySecondary, CircleShape)
                .clickable { onAdd() }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = addTitle,
                style = AppTypography.BodyTextMd.regular,
                color = Palette.blackHigh,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = Images.Icons.plus(),
                contentDescription = null,
                tint = Palette.blackHigh
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

/** Read-only capsule that shows [text] (or [placeholder]) and a trailing icon, tappable. */
@Composable
private fun CapsuleField(
    text: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.painter.Painter,
    onTap: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Palette.white)
            .border(0.5.dp, Palette.graySecondary, CircleShape)
            .clickable { onTap() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text.ifEmpty { placeholder },
            style = AppTypography.BodyTextMd.regular,
            color = if (text.isEmpty()) Palette.grayPrimary else Palette.blackHigh,
            modifier = Modifier.weight(1f)
        )
        Icon(painter = icon, contentDescription = null, tint = Palette.blackHigh)
    }
}

/** Combined date + time picker: date dialog then a time bottom sheet, stored as `yyyy-MM-dd HH:mm`. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeField(
    field: AppFieldSchema.Field,
    value: String,
    placeholder: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDate by remember { mutableStateOf(false) }
    var showTime by remember { mutableStateOf(false) }
    var pickedMillis by remember { mutableStateOf<Long?>(null) }
    val dateState = rememberDatePickerState()
    val timeState = rememberTimePickerState(is24Hour = true)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel(field)
        CapsuleField(
            text = value,
            placeholder = placeholder,
            icon = Images.Icons.calendar(),
            onTap = { showDate = true }
        )
    }

    if (showDate) {
        DatePickerDialog(
            onDismissRequest = { showDate = false },
            confirmButton = {
                TextButton(onClick = {
                    pickedMillis = dateState.selectedDateMillis
                    showDate = false
                    if (pickedMillis != null) showTime = true
                }) { Text(text = "Next") }
            },
            dismissButton = {
                TextButton(onClick = { showDate = false }) { Text(text = "Cancel") }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    if (showTime) {
        AppBottomSheet(onDismiss = { showTime = false }) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TimePicker(state = timeState)
                AppButton(
                    title = "Done",
                    model = AppButtonModel(contentSize = ContentSize.Fill),
                    onClick = {
                        val millis = pickedMillis
                        if (millis != null) {
                            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            cal.timeInMillis = millis
                            cal.set(Calendar.HOUR_OF_DAY, timeState.hour)
                            cal.set(Calendar.MINUTE, timeState.minute)
                            val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            fmt.timeZone = TimeZone.getTimeZone("UTC")
                            onChange(fmt.format(cal.time))
                        }
                        showTime = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/** Capsule that opens a single-select bottom sheet of reminder options. */
@Composable
private fun ReminderField(
    field: AppFieldSchema.Field,
    placeholder: String,
    options: List<AppFieldSchema.ReminderOption>,
    selected: AppFieldSchema.ReminderOption,
    onChange: (AppFieldSchema.ReminderOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSheet by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel(field)
        CapsuleField(
            text = if (selected == AppFieldSchema.ReminderOption.NONE) "" else selected.label,
            placeholder = placeholder,
            icon = Images.Icons.bell(),
            onTap = { showSheet = true }
        )
    }

    if (showSheet) {
        AppBottomSheet(onDismiss = { showSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(
                    text = field.label,
                    style = AppTypography.HeadingMd.medium,
                    color = Palette.blackHigh,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
                options.forEach { option ->
                    val isSelected = option == selected
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onChange(option)
                                showSheet = false
                            }
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option.label,
                            style = AppTypography.BodyTextMd.regular,
                            color = Palette.blackHigh
                        )
                        Box(
                            modifier = Modifier
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
                    }
                }
            }
        }
    }
}
