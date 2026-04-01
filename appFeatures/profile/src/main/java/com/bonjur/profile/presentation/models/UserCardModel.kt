package com.bonjur.profile.presentation.models

import com.bonjur.designSystem.commonModel.AppUIEntities

data class UserCardModel(
    val backgroundCover: AppUIEntities.BackgroundType? = null,
    val nameSurname: String = "",
    val speciality: String = "",
    val course: String = "",
    val community: String = "",
    val degree: String = "",
    val entryYear: String = "",
    val email: String = "",
    val imageUrl: String? = null
) {
    fun withBackground(cover: AppUIEntities.BackgroundType?): UserCardModel =
        copy(backgroundCover = cover)

    companion object {
        val mock = listOf(
            UserCardModel(
                backgroundCover = AppUIEntities.BackgroundType.Primary,
                nameSurname = "Huseyn Hasanov",
                speciality = "Oil-gas engineering",
                course = "1st year",
                community = "UFAZ",
                degree = "Master",
                entryYear = "2025",
                email = "h.hasanov@gmail.com",
                imageUrl = null
            ),
            UserCardModel(
                backgroundCover = null,
                nameSurname = "Huseyn Hasanov",
                speciality = "Oil-gas engineering",
                course = "1st year",
                community = "UFAZ",
                degree = "Master",
                entryYear = "2025",
                email = "h.hasanov@gmail.com",
                imageUrl = null
            )
        )
    }
}


