package com.bonjur.network.manager

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App-wide session signals raised from the network layer.
 *
 * [expired] fires when a 401 could not be recovered by `auth/refresh` (no refresh token,
 * or the server rejected it). The network module can't navigate, so the app shell
 * collects this and routes to onboarding with a cleared back stack — the Android side
 * of iOS `NetworkActivityDelegate.refreshFailure()` → `AppCoordinator.showRegisterVC()`.
 */
@Singleton
class SessionEvents @Inject constructor() {

    // Several requests can fail together (a screen fetches in parallel); one buffered
    // slot with DROP_OLDEST collapses the burst into a single logout instead of queueing.
    private val _expired = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val expired: SharedFlow<Unit> = _expired.asSharedFlow()

    fun notifyExpired() {
        _expired.tryEmit(Unit)
    }
}
