package com.bonjur.auth.presentation.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect


// MARK: - Example input
data class ExampleInputData (
    val initialValue: String = ""
)

// MARK: - Side effects
sealed class ExampleSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ExampleSideEffect()
}

// MARK: - View State
data class ExampleViewState(
    val title: String = "Example",
    val counter: Int = 0
) : FeatureState

// MARK: - Feature Action
sealed class ExampleAction : FeatureAction {
    object Increment : ExampleAction()
    object Decrement : ExampleAction()
}