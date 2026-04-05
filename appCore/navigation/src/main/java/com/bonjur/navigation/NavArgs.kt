package com.bonjur.navigation

import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory argument store for passing any data between screens.
 * Uses the object's type as the key — no string keys needed.
 *
 * Usage (sender - ViewModel):
 *   navigator.navigateTo(SomeScreen.route, SomeInputData(name = "John"))
 *
 * Usage (receiver - NavGraph composable):
 *   composable<SomeScreen> {
 *       val inputData = remember { NavArgs.get<SomeInputData>() } ?: SomeInputData()
 *       SomeScreen(inputData = inputData)
 *   }
 */
object NavArgs {
    val store = ConcurrentHashMap<String, Any>()

    fun put(value: Any) {
        store[value::class.java.name] = value
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> get(): T? {
        return store.remove(T::class.java.name) as? T
    }
}
