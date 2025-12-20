package com.bonjur.auth.presentation

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.navigation.Navigator
import com.bonjur.auth.ForgotPass
import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.domain.useCase.AuthUseCase
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.auth.presentation.model.ExampleAction
import com.bonjur.auth.presentation.model.ExampleInputData
import com.bonjur.auth.presentation.model.ExampleSideEffect
import com.bonjur.auth.presentation.model.ExampleViewState
import com.bonjur.network.model.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val navigator: Navigator,
    private val dependencies: Dependencies
) : FeatureViewModel<ExampleViewState, ExampleAction, ExampleSideEffect>(
    ExampleViewState()
) {
    private lateinit var inputData: ExampleInputData

    data class Dependencies @Inject constructor(
        val useCase: AuthUseCase
    )
    fun init(inputData: ExampleInputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        fetchData()
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
        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    "husein10.hasanov@gmail.com"
                )
                dependencies.useCase.register(
                    request
                )
            } catch (e: ApiException) {
                val message = when (e) {
                    is ApiException.ServerError -> "Server error: ${e.error.message}"
                    is ApiException.Unauthorized -> "Unauthorized. Please login again."
                    is ApiException.NetworkException -> "Network error: ${e.throwable.message}"
                    is ApiException.DecodingError -> "Failed to decode response"
                    is ApiException.InvalidURL -> "Invalid URL"
                    is ApiException.NoData -> "No data received"
                    is ApiException.Unknown -> "Unknown error occurred"
                }
                print(message)
            }
        }
    }
}