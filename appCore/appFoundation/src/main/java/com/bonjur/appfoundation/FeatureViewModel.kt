package com.bonjur.appfoundation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Base ViewModel for UI features
 * Equivalent to Swift's UIFeatureViewModel<Feature: UIFeature>
 */
abstract class FeatureViewModel<State : FeatureState, Action : FeatureAction, Effect : SideEffect>(
    initialState: State
) : ViewModel() {

    // Equivalent to @MainActor var effectClosure
    internal var effectClosure: ((Effect) -> Unit)? = null

    // Public store property
    val store: FeatureStore<State, Action, Effect>

    // Computed property for state (equivalent to Swift's computed var state)
    val state: State
        get() = store.state

    init {
        // Initialize store with initial state
        store = FeatureStore(initialState)

        // Bind action handler (equivalent to Swift's store.bindActionHandler)
        store.bindActionHandler { action ->
            handle(action = action)
        }
    }

    /**
     * Handle incoming actions - must be overridden in subclass
     * Equivalent to Swift's open func handle(action:)
     */
    abstract fun handle(action: Action)

    /**
     * Post a side effect to be handled by the controller/screen
     * Equivalent to Swift's postEffect(_ effect:)
     */
    protected fun postEffect(effect: Effect) {
        viewModelScope.launch {
            // Run on Main dispatcher (equivalent to MainActor.run)
            withContext(Dispatchers.Main) {
                effectClosure?.invoke(effect)
            }
        }
    }

    /**
     * Update state helper method
     */
    protected fun updateState(newState: State) {
        store.state = newState
    }
}