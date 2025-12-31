package com.bonjur.designSystem.components.textView

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun TextView(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    characterLimit: Int = 500,
    placeholder: String = "Write something"
) {
    var isFocused by remember { mutableStateOf(false) }
    
    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) 0.5.dp else 0.1.dp,
        animationSpec = tween(durationMillis = 250),
        label = "border_animation"
    )
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Palette.grayQuaternary)
                .border(
                    width = borderWidth,
                    color = Palette.blackHigh,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    if (newText.length <= characterLimit) {
                        onTextChange(newText)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Palette.blackHigh
                ),
                cursorBrush = SolidColor(Palette.blackHigh),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
            
            Text(
                text = "${text.length} / $characterLimit",
                fontSize = 12.sp,
                color = if (text.length >= characterLimit) Color.Red else Palette.black.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
                    .wrapContentWidth(Alignment.End)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextViewPreview() {
    var text by remember { mutableStateOf("") }

    TextView(
        text = text,
        onTextChange = { text = it },
        characterLimit = 200,
        modifier = Modifier.height(200.dp)
    )
}