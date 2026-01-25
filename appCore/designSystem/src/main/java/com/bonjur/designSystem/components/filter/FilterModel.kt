//
//  FilterViewModels.kt
//  AppFeature
//
//  Created by Huseyn Hasanov on 12.01.26
//

package com.bonjur.designSystem.components.filter

import java.util.UUID

object FilterView {
    
    data class Model(
        val id: UUID = UUID.randomUUID(),
        val title: String,
        val type: String,
        val items: List<Items>
    )
    
    data class Items(
        val uuid: UUID = UUID.randomUUID(),
        val title: String,
        val selected: Boolean = false,
        val id: Int
    )
}

// Mock data
object FilterViewMocks {
    val mockData = listOf(
        FilterView.Model(
            title = "sport",
            type = "SPORT",
            items = listOf(
                FilterView.Items(title = "Football", id = 1),
                FilterView.Items(title = "Basketball", id = 2),
                FilterView.Items(title = "VoleyBall", id = 3),
                FilterView.Items(title = "Football", id = 4),
                FilterView.Items(title = "Basketball", id = 5),
                FilterView.Items(title = "VoleyBall", id = 6),
                FilterView.Items(title = "Football", id = 7),
                FilterView.Items(title = "Basketball", id = 8),
                FilterView.Items(title = "VoleyBall", id = 9),
                FilterView.Items(title = "Football", id = 10),
                FilterView.Items(title = "Basketball", id = 11),
                FilterView.Items(title = "VoleyBall", id = 12),
                FilterView.Items(title = "Football", id = 13),
                FilterView.Items(title = "Basketball", id = 14),
                FilterView.Items(title = "VoleyBall", id = 15),
                FilterView.Items(title = "Football", id = 16),
                FilterView.Items(title = "Basketball", id = 17),
                FilterView.Items(title = "VoleyBall", id = 18)
            )
        ),
        FilterView.Model(
            title = "fashion",
            type = "FASHION",
            items = listOf(
                FilterView.Items(title = "Beauty", id = 19),
                FilterView.Items(title = "Stars", id = 20),
                FilterView.Items(title = "Celebrities", id = 21),
                FilterView.Items(title = "Dress", id = 22),
                FilterView.Items(title = "Beauty", id = 23),
                FilterView.Items(title = "Stars", id = 24),
                FilterView.Items(title = "Celebrities", id = 25),
                FilterView.Items(title = "Dress", id = 26),
                FilterView.Items(title = "Beauty", id = 27),
                FilterView.Items(title = "Stars", id = 28),
                FilterView.Items(title = "Celebrities", id = 29),
                FilterView.Items(title = "Dress", id = 30),
                FilterView.Items(title = "Beauty", id = 31),
                FilterView.Items(title = "Stars", id = 32),
                FilterView.Items(title = "Celebrities", id = 33),
                FilterView.Items(title = "Dress", id = 34)
            )
        ),
        FilterView.Model(
            title = "study",
            type = "STUDY",
            items = listOf(
                FilterView.Items(title = "Exams", id = 35),
                FilterView.Items(title = "Physics", id = 36),
                FilterView.Items(title = "Math", id = 37),
                FilterView.Items(title = "Chemistry", id = 38),
                FilterView.Items(title = "CS", id = 39),
                FilterView.Items(title = "OG", id = 40),
                FilterView.Items(title = "CE", id = 41),
                FilterView.Items(title = "Data science", id = 42),
                FilterView.Items(title = "Hackhaton", id = 43)
            )
        )
    )
}