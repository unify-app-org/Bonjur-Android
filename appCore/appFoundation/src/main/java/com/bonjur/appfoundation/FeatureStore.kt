package com.bonjur.appfoundation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Store that holds state and dispatches actions for a feature
 * Equivalent to Swift's StoreOf<Feature>: ObservableObject
 */
class FeatureStore<State : FeatureState, Action : FeatureAction, Effect : SideEffect>(
    initialState: State
) {
    // Using Compose's mutableStateOf for reactive state (equivalent to @Published)
    var state by mutableStateOf(initialState)
        internal set

    private var actionHandler: ((Action) -> Unit)? = null

    /**
     * Bind the action handler from ViewModel
     * Internal function called by UIFeatureViewModel
     */
    internal fun bindActionHandler(handler: (Action) -> Unit) {
        this.actionHandler = handler
    }

    /**
     * Send an action to be processed
     * Equivalent to Swift's send(_ action:)
     */
    fun send(action: Action) {
        val handler = actionHandler
        if (handler == null) {
            error("UIStore action handler is not bound")
            return
        }
        handler(action)
    }
}