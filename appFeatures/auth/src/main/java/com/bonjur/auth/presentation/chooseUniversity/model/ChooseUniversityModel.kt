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
    val phase: CommunitiesPhase = CommunitiesPhase.LOADING,
    /**
     * App Store / Play UGC policy: users accept the terms before reaching any user
     * content, so sign-in (MSAL and credentials) is gated on this. Mirrors iOS.
     */
    val termsAccepted: Boolean = false,
    val showTerms: Boolean = false,
    val showPrivacy: Boolean = false
) : FeatureState

sealed class ChooseUniversityAction : FeatureAction {
    object FetchData : ChooseUniversityAction()
    data class SelectedCell(val index: Int) : ChooseUniversityAction()
    object Dismiss : ChooseUniversityAction()
    object NextTapped: ChooseUniversityAction()
    object TermsToggled : ChooseUniversityAction()
    object TermsTapped : ChooseUniversityAction()
    object DismissTerms : ChooseUniversityAction()
    object PrivacyTapped : ChooseUniversityAction()
    object DismissPrivacy : ChooseUniversityAction()
}

sealed class ChooseUniversitySideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ChooseUniversitySideEffect()
    data class Error(val message: String?) : ChooseUniversitySideEffect()
    object LaunchMicrosoftSignIn : ChooseUniversitySideEffect()
}
