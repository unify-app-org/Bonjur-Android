package com.bonjur.auth.presentation.onboarding.model

import com.bonjur.appfoundation.*
import com.bonjur.auth.domain.models.OnboardingUIModel

data class OnboardingInputData(
    val example: String = ""
)

sealed class OnboardingSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : OnboardingSideEffect()
}

data class OnboardingViewState(
    val uiModel: List<OnboardingUIModel> = emptyList()
) : FeatureState

sealed class OnboardingAction : FeatureAction {
    object FetchData : OnboardingAction()
    object SkipClicked : OnboardingAction()
}
