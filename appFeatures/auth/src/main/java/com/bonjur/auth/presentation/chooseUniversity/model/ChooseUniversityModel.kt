package com.bonjur.auth.presentation.chooseUniversity.model

import com.bonjur.appfoundation.*
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel

data class ChooseUniversityInputData(
    val example: String = ""
)

/** Load state of the community list. Mirrors iOS `ChooseUniversityViewState.phase`. */
enum class CommunitiesPhase { LOADING, LOADED, FAILED }

data class ChooseUniversityViewState(
    val uiModel: List<SelectableListItemModel> = emptyList(),
    val enabled: Boolean = false,
    val phase: CommunitiesPhase = CommunitiesPhase.LOADING
) : FeatureState

sealed class ChooseUniversityAction : FeatureAction {
    object FetchData : ChooseUniversityAction()
    data class SelectedCell(val index: Int) : ChooseUniversityAction()
    object Dismiss : ChooseUniversityAction()
    object NextTapped: ChooseUniversityAction()
}

sealed class ChooseUniversitySideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ChooseUniversitySideEffect()
    data class Error(val message: String?) : ChooseUniversitySideEffect()
    object LaunchMicrosoftSignIn : ChooseUniversitySideEffect()
}
