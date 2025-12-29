package com.bonjur.auth.presentation.optional.model

import androidx.compose.runtime.Composable
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import java.time.LocalDate

data class AuthOptionalInfoInputData(
    val dummy: String = ""
)

data class AuthOptionalInfoViewState(
    var currentStep: Int = 1,
    var birthDate: LocalDate? = null,
    val showDatePicker: Boolean = false,
    val genders: List<SelectableListItemModel> = emptyList(),
    val languages: List<SelectableListItemModel> = emptyList(),
    val biography: String? = null,
    val languageSearchText: String? = null
) : FeatureState

data class AuthOptionalInfoStep(
    val id: Int,
    val content: @Composable () -> Unit
)

sealed class AuthOptionalInfoAction : FeatureAction {
    object FetchData: AuthOptionalInfoAction()
    object Next : AuthOptionalInfoAction()
    object Back : AuthOptionalInfoAction()
    data class PageChanged(val step: Int) : AuthOptionalInfoAction()
    data class DateSelected(val date: LocalDate) : AuthOptionalInfoAction()
    object CloseDatePicker : AuthOptionalInfoAction()
    object OpenDatePicker : AuthOptionalInfoAction()
    object Skip : AuthOptionalInfoAction()
    data class SelectedGender(val index: Int) : AuthOptionalInfoAction()
    data class SelectedLanguage(val index: Int) : AuthOptionalInfoAction()
    data class BioChange(val text: String) : AuthOptionalInfoAction()
    data class LanguageTextChange(val text: String) : AuthOptionalInfoAction()

}

sealed class AuthOptionalInfoSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : AuthOptionalInfoSideEffect()
}
