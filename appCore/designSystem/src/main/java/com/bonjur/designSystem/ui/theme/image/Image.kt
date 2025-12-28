package com.bonjur.designSystem.ui.theme.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.bonjur.designsystem.R

object Images {
    object Icons {
        @Composable
        fun logoWithText() = painterResource(R.drawable.logo_with_text)

        @Composable
        fun arrowLeft01() = painterResource(R.drawable.arrow_left_01)

        @Composable
        fun bigPeopleGroups() = painterResource(R.drawable.big_people_groups)

        @Composable
        fun bigLamps() = painterResource(R.drawable.big_lamps)

        @Composable
        fun bigGraduationHat() = painterResource(R.drawable.big_lamps)

        @Composable
        fun selectedCheckBox() = painterResource(R.drawable.selected_check_box)

        @Composable
        fun notSelectedCheckBox() = painterResource(R.drawable.not_selected_check_box)

        @Composable
        fun xmark() = painterResource(R.drawable.xmark)

        @Composable
        fun resume() = painterResource(R.drawable.big_people_groups)

        @Composable
        fun search() = painterResource(R.drawable.ic_search)
    }
}