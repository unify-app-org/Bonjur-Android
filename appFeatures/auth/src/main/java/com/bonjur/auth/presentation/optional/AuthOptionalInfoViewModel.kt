package com.bonjur.auth.presentation.optional

import android.R.attr.action
import android.R.attr.category
import android.R.attr.data
import android.util.Log.i
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoInputData
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
                selectedGender(action.id)
            }
            is AuthOptionalInfoAction.SelectedLanguage -> {
                selectedLanguage(action.id)
            }
            is AuthOptionalInfoAction.BioChange -> {
                bioChange(action.text)
            }
            is AuthOptionalInfoAction.LanguageTextChange -> {
                languageSearchTextChange(action.text)
            }
            is AuthOptionalInfoAction.SelectImage -> {
                selectedImage(action.data)
            }
            is AuthOptionalInfoAction.SelectedInterests -> {
                selectedInterests(action.id)
            }
        }
    }

    private fun selectedInterests(id: Int) {
        updateState(
            state.copy(
                interests = state.interests.map { interest ->
                    interest.copy(
                        interests = interest.interests.map { category ->
                            if (category.id == id) {
                                category.copy(selected = !category.selected)
                            } else {
                                category
                            }
                        }
                    )
                }
            )
        )
    }

    private fun selectedImage(data: ByteArray) {
        updateState(state.copy(selectedImage = data))
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

    private fun selectedLanguage(id: Int) {
        updateState(
            state.copy(
                languages = state.languages.map { language ->
                    if (language.id == id) language.copy(selected = !language.selected)
                    else language
                }
            )
        )
    }

    private fun selectedGender(id: Int) {
        updateState(
            state.copy(
                genders = state.genders.map { gender ->
                    gender.copy(selected = gender.id == id)
                }
            )
        )
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateState(state.copy(genders = dependencies.useCase.genders()))
            updateState(state.copy(languages = dependencies.useCase.languages()))
            updateState(state.copy(interests = dependencies.useCase.interests()))
        }
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
