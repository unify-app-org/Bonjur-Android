package com.bonjur.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    private val _navigationCommands = Channel<NavigationCommand>(Channel.BUFFERED)
    val navigationCommands: Flow<NavigationCommand> = _navigationCommands.receiveAsFlow()

    suspend fun navigateUp() {
        _navigationCommands.send(NavigationCommand.NavigateUp)
    }

    suspend fun navigateTo(route: String) {
        _navigationCommands.send(NavigationCommand.NavigateTo(route))
    }

    suspend fun navigateTo(route: String, inputData: Any) {
        NavArgs.put(inputData)
        _navigationCommands.send(NavigationCommand.NavigateTo(route))
    }

    suspend fun navigateAndClearStack(route: String, inputData: Any) {
        NavArgs.put(inputData)
        _navigationCommands.send(NavigationCommand.NavigateAndClearStack(route))
    }

    suspend fun navigateAndClearStack(route: String) {
        _navigationCommands.send(NavigationCommand.NavigateAndClearStack(route))
    }

    suspend fun popToRoute(route: String, inclusive: Boolean = false) {
        _navigationCommands.send(NavigationCommand.PopToRoute(route, inclusive))
    }
}