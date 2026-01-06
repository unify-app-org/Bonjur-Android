package com.bonjur.auth.presentation.signIn

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.auth.presentation.signIn.model.SignInAction
import com.bonjur.auth.presentation.signIn.model.SignInInputData
import com.bonjur.auth.presentation.signIn.model.SignInSideEffect
import com.bonjur.auth.presentation.signIn.model.SignInViewState
import com.bonjur.navigation.AppScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val navigator: Navigator
) : FeatureViewModel<SignInViewState, SignInAction, SignInSideEffect>(
    SignInViewState()
) {

    private lateinit var inputData: SignInInputData

    fun init(inputData: SignInInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        fetchData()
    }

    override fun handle(action: SignInAction) {
        when (action) {
            SignInAction.FetchData -> {
                fetchData()
            }
            SignInAction.SignIn -> {
                viewModelScope.launch {
                    navigator.navigateAndClearStack(AppScreens.Main.route)
                }
            }
            SignInAction.Dismiss -> {
                viewModelScope.launch {
                    navigator.navigateUp()
                }
            }
        }
    }

    private fun fetchData() {
        // reserved for future logic
    }
}
