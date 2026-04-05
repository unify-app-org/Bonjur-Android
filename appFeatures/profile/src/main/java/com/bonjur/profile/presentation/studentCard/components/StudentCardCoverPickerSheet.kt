package com.bonjur.profile.presentation.studentCard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.ui.theme.Typography.AppTypography

@Composable
fun StudentCardCoverPickerSheet(
    selected: AppUIEntities.BackgroundType?,
    onCoverSelected: (AppUIEntities.BackgroundType?) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Choose cover",
            style = AppTypography.TitleMd.extraBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(top = 25.dp)
        )

        StudentCardCoverPicker(
            selected = selected,
            onCoverSelected = onCoverSelected
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                title = "Cancel",
                onClick = onCancel,
                model = AppButtonModel(type = ButtonType.Tertiary)
            )
            AppButton(
                title = "Save",
                onClick = onSave,
                model = AppButtonModel(contentSize = ContentSize.Fill),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
