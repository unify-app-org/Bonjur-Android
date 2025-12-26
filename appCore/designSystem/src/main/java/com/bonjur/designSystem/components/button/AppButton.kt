package com.bonjur.designSystem.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun AppButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    model: AppButtonModel = AppButtonModel(),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.then(
            if (model.contentSize == ContentSize.Fill) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.wrapContentWidth()
            }
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) model.backgroundColor else Palette.onBackground,
            contentColor = if (enabled) model.foregroundColor else Palette.blackDisabled,
            disabledContainerColor = Palette.onBackground,
            disabledContentColor = Palette.blackDisabled
        ),
        shape = RoundedCornerShape(50),
        border = if (model.borderWidth > 0) {
            BorderStroke(model.borderWidth.dp, model.borderColor)
        } else null,
        contentPadding = PaddingValues(
            horizontal = model.horizontalPadding.dp,
            vertical = model.verticalPadding.dp
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}