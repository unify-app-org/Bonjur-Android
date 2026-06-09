package com.bonjur.designSystem.components.links

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.dropdown.AppDropdownField
import com.bonjur.designSystem.components.dropdown.AppDropdownOption
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.components.textField.AppTextField
import com.bonjur.designSystem.components.textField.AppTextFieldModel
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

private const val CUSTOM_TYPE_ID = "other"

val defaultLinkTypeOptions: List<AppDropdownOption> = listOf(
    AppDropdownOption(id = "social_media", title = "Social media"),
    AppDropdownOption(id = "website", title = "Website"),
    AppDropdownOption(id = "other", title = "Other")
)

/**
 * Bottom-sheet form to add a single link. Kotlin equivalent of iOS `AddLinkView`.
 */
@Composable
fun AddLinkView(
    onAdd: (AppFieldSchema.LinkItem) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Add link",
    subtitle: String = "Add a link for your members to join or learn more. You can add max 4 links.",
    typeOptions: List<AppDropdownOption> = defaultLinkTypeOptions
) {
    var selectedType by remember { mutableStateOf<AppDropdownOption?>(null) }
    var linkName by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    val isCustomType = selectedType?.id == CUSTOM_TYPE_ID
    val isValidUrl = Patterns.WEB_URL.matcher(url.trim()).matches()
    val canAdd = selectedType != null &&
        isValidUrl &&
        (!isCustomType || linkName.trim().isNotEmpty())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = AppTypography.TitleL.extraBold,
                    color = Palette.blackHigh,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = Images.Icons.xmark(),
                    contentDescription = "Close",
                    tint = Palette.black,
                    modifier = Modifier.clickable { onClose() }
                )
            }
            Text(
                text = subtitle,
                style = AppTypography.BodyTextMd.regular,
                color = Palette.grayPrimary
            )
        }

        AppDropdownField(
            selection = selectedType,
            options = typeOptions,
            placeholder = "Select link",
            sheetTitle = "Title link",
            title = "Title link",
            onSelect = { selectedType = it }
        )

        if (isCustomType) {
            AppTextField(
                text = linkName,
                onTextChange = { linkName = it },
                placeHolder = "Enter link name",
                model = AppTextFieldModel(title = "Link name")
            )
        }

        AppTextField(
            text = url,
            onTextChange = { url = it },
            placeHolder = "https://",
            model = AppTextFieldModel(title = "Link", keyboardType = KeyboardType.Uri)
        )

        AppButton(
            title = "Confirm",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            enabled = canAdd,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val type = selectedType ?: return@AppButton
                val name = if (type.id == CUSTOM_TYPE_ID) linkName.trim() else type.title
                onAdd(
                    AppFieldSchema.LinkItem(
                        type = type.id,
                        name = name,
                        url = url.trim()
                    )
                )
            }
        )
    }
}
