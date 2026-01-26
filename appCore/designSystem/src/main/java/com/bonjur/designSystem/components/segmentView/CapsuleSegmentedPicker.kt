package com.bonjur.designSystem.components.segmentView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun <T : SegmentedPickerOption> CapsuleSegmentedPicker(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var itemSizes by remember { mutableStateOf(List(options.size) { IntSize.Zero }) }
    val density = LocalDensity.current

    Surface (
        modifier = modifier,
        shape = CircleShape,
        color = Palette.grayQuaternary
    ) {
        Row (
            modifier = Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption.id == option.id

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .onSizeChanged { size ->
                            itemSizes = itemSizes
                                .toMutableList()
                                .apply { this[index] = size }
                        }
                ) {
                    // Background pill
                    if (isSelected) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(),
                            shape = CircleShape,
                            color = Palette.white
                        ) {}
                    }

                    Text(
                        text = option.title,
                        style = if (isSelected) {
                            AppTypography.TextL.bold
                        } else {
                            AppTypography.TextL.medium
                        },
                        color = if (isSelected) {
                            Palette.blackHigh
                        } else {
                            Palette.blackDisabled
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onOptionSelected(option)
                            }
                            .wrapContentHeight(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

sealed interface TabOption : SegmentedPickerOption {
    data object Clubs : TabOption {
        override val title = "Clubs"
        override val id = "clubs"
    }

    data object Events : TabOption {
        override val title = "Events"
        override val id = "events"
    }

    data object Hangouts : TabOption {
        override val title = "Hangouts"
        override val id = "hangouts"
    }

    companion object {
        val all = listOf(Clubs, Events, Hangouts)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCapsuleSegmentedPicker() {
    var selectedTab by remember { mutableStateOf<TabOption>(
        TabOption.Clubs) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Sealed Interface Version (Best Practice)",
            style = AppTypography.TitleMd.bold
        )

        CapsuleSegmentedPicker(
            options = TabOption.all,
            selectedOption = selectedTab,
            onOptionSelected = { selectedTab = it }
        )

        Text(
            text = "Selected: ${selectedTab.title}",
            style = AppTypography.TextMd.regular,
            color = Palette.blackMedium
        )
    }
}