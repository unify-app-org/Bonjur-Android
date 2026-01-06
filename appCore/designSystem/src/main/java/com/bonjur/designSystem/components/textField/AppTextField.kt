package com.bonjur.designSystem.components.textField

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun AppTextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeHolder: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    model: AppTextFieldModel = AppTextFieldModel()
) {
    var isFocused by remember { mutableStateOf(false) }
    
    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) 1.dp else 0.5.dp,
        animationSpec = tween(durationMillis = 250),
        label = "border_animation"
    )
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title
        model.title?.let { title ->
            Text(
                text = title,
                style = AppTypography.HeadingMd.medium,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // TextField
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text(placeHolder) },
            visualTransformation = when (model.type) {
                FieldType.Secure -> PasswordVisualTransformation()
                FieldType.Normal -> VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = model.keyboardType
            ),
            maxLines = maxLines,
            singleLine = maxLines == 1,
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .border(
                    width = borderWidth,
                    color = Palette.graySecondary,
                    shape = CircleShape
                )
                .padding(horizontal = 12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    
    AppTextField(
        text = text,
        onTextChange = { text = it },
        placeHolder = "Hello, World!",
        maxLines = Int.MAX_VALUE,
        model = AppTextFieldModel(title = "Email"),
        modifier = Modifier.padding(16.dp)
    )
}