package com.bonjur.auth.presentation.chooseUniversity.model

import com.bonjur.appfoundation.*
import com.bonjur.auth.domain.models.ChooseUniversityUIModel

data class ChooseUniversityInputData(
    val example: String = ""
)

data class ChooseUniversityViewState(
    val uiModel: List<ChooseUniversityUIModel> = emptyList(),
    val enabled: Boolean = false
) : FeatureState

sealed class ChooseUniversityAction : FeatureAction {
    object FetchData : ChooseUniversityAction()
    data class SelectedCell(val index: Int) : ChooseUniversityAction()
    object Dismiss : ChooseUniversityAction()
    object NextTapped: ChooseUniversityAction()
}

sealed class ChooseUniversitySideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ChooseUniversitySideEffect()
}
