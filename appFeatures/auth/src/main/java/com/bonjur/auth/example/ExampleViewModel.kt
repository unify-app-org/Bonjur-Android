package com.bonjur.auth.example

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.Navigator
import com.bonjur.auth.ForgotPass
import com.bonjur.auth.navigation.AuthScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val navigator: Navigator
) : FeatureViewModel<ExampleViewState, ExampleAction, ExampleSideEffect>(
    ExampleViewState()
) {
    private lateinit var inputData: ExampleInputData

    fun init(inputData: ExampleInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
    }

    override fun handle(action: ExampleAction) {
        when (action) {
            ExampleAction.Increment -> {
                updateState(state.copy(counter = state.counter + 1))
            }
            ExampleAction.Decrement -> {
                val forgotPass = ForgotPass("husein10.hasanov@gmail.com")
                navigate(AuthScreens.ForgotPassword.createRoute(forgotPass))
//                updateState(state.copy(counter = state.counter - 1))
            }
        }
    }

    private fun navigate(to: String) {
        viewModelScope.launch {
            navigator.navigateTo(to)
        }
    }

    private fun fetchData() {
        // Fetch data logic
    }
}