package com.bonjur.auth.domain.models

import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel

data class AuthInterestsModel(
    val title: String,
    val interests: List<CategoriesChipModel>
)
