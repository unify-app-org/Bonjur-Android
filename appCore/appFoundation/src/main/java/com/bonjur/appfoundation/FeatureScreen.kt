package com.bonjur.appfoundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * Base composable screen for UI features
 * This is the Compose equivalent of UIFeatureController<Feature, Content>
 * 
 * In iOS you have UIHostingController that bridges SwiftUI to UIKit.
 * In Compose, we don't need a controller - the Composable itself IS the view.
 * 
 * However, we provide this wrapper to match your iOS architecture pattern.
 */
@Composable
fun <State : FeatureState, Action : FeatureAction, Effect : SideEffect> FeatureScreen(
    viewModel: FeatureViewModel<State, Action, Effect>,
    handleEffect: (Effect) -> Unit,
    content: @Composable (FeatureStore<State, Action, Effect>) -> Unit
) {
    // Store reference
    val store = viewModel.store

    // Set up effect handling (equivalent to setting effectClosure in init)
    DisposableEffect(viewModel) {
        viewModel.effectClosure = { effect ->
            handleEffect(effect)
        }

        onDispose {
            viewModel.effectClosure = null
        }
    }

    // Render content with store
    content(store)
}