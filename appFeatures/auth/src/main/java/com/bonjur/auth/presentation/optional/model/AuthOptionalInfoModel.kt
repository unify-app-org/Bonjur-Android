package com.bonjur.auth.presentation.optional.model

import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.SideEffect

data class AuthOptionalInfoInputData(
    val dummy: String = ""
)

data class AuthOptionalInfoViewState(
    var currentStep: Int = 1
) : FeatureState

sealed class AuthOptionalInfoAction : FeatureAction {
    object Next : AuthOptionalInfoAction()
    object Back : AuthOptionalInfoAction()
    data class PageChanged(val step: Int) : AuthOptionalInfoAction()
}

sealed class AuthOptionalInfoSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : AuthOptionalInfoSideEffect()
}
