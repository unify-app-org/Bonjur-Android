package com.bonjur.auth.presentation.optional

import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoInputData
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthOptionalInfoViewModel @Inject constructor(
) : FeatureViewModel<AuthOptionalInfoViewState, AuthOptionalInfoAction, AuthOptionalInfoSideEffect>(
    AuthOptionalInfoViewState()
) {

    private lateinit var inputData: AuthOptionalInfoInputData

    fun init(input: AuthOptionalInfoInputData) {
        if (::inputData.isInitialized) return
        inputData = input
    }

    override fun handle(action: AuthOptionalInfoAction) {
        when (action) {
            is AuthOptionalInfoAction.PageChanged -> {
                updateState ( state.copy(currentStep = action.step) )
            }
            AuthOptionalInfoAction.Next -> updateState (
                state.copy(currentStep = state.currentStep + 1)
            )
            AuthOptionalInfoAction.Back -> updateState (
                state.copy(currentStep = state.currentStep - 1)
            )
        }
    }
}
