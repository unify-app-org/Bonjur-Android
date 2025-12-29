package com.bonjur.auth.presentation.optional

import android.R.attr.action
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoInputData
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AuthOptionalInfoViewModel @Inject constructor(
    val dependencies: Dependencies
) : FeatureViewModel<AuthOptionalInfoViewState, AuthOptionalInfoAction, AuthOptionalInfoSideEffect>(
    AuthOptionalInfoViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: AuthUseCase
    )

    private lateinit var inputData: AuthOptionalInfoInputData

    fun init(input: AuthOptionalInfoInputData) {
        if (::inputData.isInitialized) return
        inputData = input
    }

    override fun handle(action: AuthOptionalInfoAction) {
        when (action) {
            is AuthOptionalInfoAction.PageChanged -> {
                pageChange(action.step)
            }
            AuthOptionalInfoAction.Next -> nextTapped()
            AuthOptionalInfoAction.Back -> previous()
            AuthOptionalInfoAction.Skip -> skipTapped()
            AuthOptionalInfoAction.CloseDatePicker -> closeDatePicker()
            AuthOptionalInfoAction.OpenDatePicker ->  openDatePicker()
            is AuthOptionalInfoAction.DateSelected -> {
                selectedDate(action.date)
            }
            AuthOptionalInfoAction.FetchData -> fetchData()
            is AuthOptionalInfoAction.SelectedGender -> {
                selectedGender(action.index)
            }
            is AuthOptionalInfoAction.SelectedLanguage -> {
                selectedLanguage(action.index)
            }
            is AuthOptionalInfoAction.BioChange -> {
                bioChange(action.text)
            }
            is AuthOptionalInfoAction.LanguageTextChange -> {
                languageSearchTextChange(action.text)
            }
        }
    }

    private fun languageSearchTextChange(text: String) {
        updateState(
            state.copy(languageSearchText = text)
        )
    }

    private fun bioChange(text: String) {
        updateState(
            state.copy(biography = text)
        )
    }

    private fun selectedLanguage(index: Int) {
        updateState(
            state.copy(
                languages = state.languages.mapIndexed { i, language ->
                    if (i == index) language.copy(selected = !language.selected)
                    else language
                }
            )
        )
    }

    private fun selectedGender(index: Int) {
        updateState(
            state.copy(
                genders = state.genders.mapIndexed { i, gender ->
                    gender.copy(selected = i == index)
                }
            )
        )
    }

    private fun fetchData() {
        updateState(state.copy(genders = dependencies.useCase.genders()))
        updateState(state.copy(languages = dependencies.useCase.languages()))
    }

    private fun openDatePicker() {
        updateState(state.copy(showDatePicker = true))
    }

    private fun closeDatePicker() {
        updateState(state.copy(showDatePicker = false))
    }

    private fun selectedDate(date: LocalDate) {
        updateState(state.copy(birthDate = date))
    }

    private fun skipTapped() {

    }

    private fun nextTapped() {
        updateState (
            state.copy(currentStep = state.currentStep + 1)
        )
    }

    private fun pageChange(step: Int) {
        updateState ( state.copy(currentStep = step) )
    }

    private fun previous() {
        updateState (
            state.copy(currentStep = state.currentStep - 1)
        )
    }
}
