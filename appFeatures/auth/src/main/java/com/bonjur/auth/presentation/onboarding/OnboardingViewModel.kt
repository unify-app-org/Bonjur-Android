package com.bonjur.auth.presentation.onboarding

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.auth.presentation.onboarding.model.OnboardingAction
import com.bonjur.auth.presentation.onboarding.model.OnboardingInputData
import com.bonjur.auth.presentation.onboarding.model.OnboardingSideEffect
import com.bonjur.auth.presentation.onboarding.model.OnboardingViewState
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val dependencies: Dependencies,
    private val navigator: Navigator
) : FeatureViewModel<OnboardingViewState, OnboardingAction, OnboardingSideEffect>(
    OnboardingViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: AuthUseCase
    )

    fun init(inputData: OnboardingInputData) {
        fetchData()
    }

    override fun handle(action: OnboardingAction) {
        when (action) {
            OnboardingAction.FetchData -> fetchData()
            OnboardingAction.SkipClicked -> skipClicked()
        }
    }

    private fun skipClicked() {
        viewModelScope.launch {
            navigator.navigateTo(AuthScreens.ChooseUniversity.route)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateState(
                state.copy(
                    uiModel = dependencies.useCase.onboarding()
                )
            )
        }
    }
}
