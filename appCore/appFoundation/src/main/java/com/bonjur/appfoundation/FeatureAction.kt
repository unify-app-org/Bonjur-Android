package com.bonjur.appfoundation

/**
 * Marker interface for UI feature actions
 */
interface FeatureAction

/**
 * Marker interface for UI feature state
 * In Kotlin/Compose, we don't need ObservableObject as state is handled by Compose
 */
interface FeatureState

/**
 * Marker interface for side effects
 */
interface SideEffect

/**
 * Base interface for UI features with associated types
 * Kotlin doesn't have associated types like Swift, so we use generics
 */
interface Feature<State : FeatureState, Action : FeatureAction, Effect : SideEffect>
