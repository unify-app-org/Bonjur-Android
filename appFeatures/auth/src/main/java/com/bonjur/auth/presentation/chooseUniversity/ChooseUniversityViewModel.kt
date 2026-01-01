package com.bonjur.auth.presentation.chooseUniversity

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.ForgotPass
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityAction
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityInputData
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversitySideEffect
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityViewState
import com.bonjur.auth.presentation.welcome.model.AuthWelcomeInputData
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseUniversityViewModel @Inject constructor(
    private val dependencies: Dependencies,
    private val navigator: Navigator
) : FeatureViewModel<ChooseUniversityViewState, ChooseUniversityAction, ChooseUniversitySideEffect>(
    ChooseUniversityViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: AuthUseCase
    )

    private lateinit var inputData: ChooseUniversityInputData

    fun init(inputData: ChooseUniversityInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        fetchData()
    }

    override fun handle(action: ChooseUniversityAction) {
        when (action) {
            is ChooseUniversityAction.FetchData -> fetchData()
            is ChooseUniversityAction.SelectedCell -> selectCell(action.index)
            is ChooseUniversityAction.Dismiss -> dismiss()
            is ChooseUniversityAction.NextTapped -> nextTapped()
        }
    }

    private fun nextTapped() {
        viewModelScope.launch {
            val welcomeInputData = AuthWelcomeInputData("Huseyn")
            navigator.navigateAndClearStack(AuthScreens.Welcome.createRoute(welcomeInputData))
        }
    }

    private fun dismiss() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val universities = dependencies.useCase.chooseUniversity()
                updateState ( state.copy(uiModel = universities) )
            } catch (e: Exception) {

            }
        }
    }

    private fun selectCell(index: Int) {
        updateState (
            state.copy(
                uiModel = this.state.uiModel.mapIndexed { i, item ->
                    item.copy(selected = i == index)
                },
                enabled = true
            )
        )
    }
}
