package com.bonjur.network.manager

import com.bonjur.network.APIClient.ApiClientProtocol
import com.bonjur.network.APIClient.AppEndpoint
import com.bonjur.network.APIClient.NetworkMethod
import com.bonjur.network.APIClient.NetworkService
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The logged-in user's email, for the create forms' owner-contact prefill.
 *
 * [TokenManager.saveUserEmail] only runs inside `AuthUseCaseImpl.login`, so any session that
 * signed in before that shipped has no `user_email` in secure storage and the prefill silently
 * no-opped — the field came up blank on all three create forms while iOS filled it (the Keychain
 * there outlives reinstalls). This falls back to the user endpoint and writes the answer back,
 * so the miss costs one request per install rather than one per form.
 *
 * Lives in `:appCore:network` rather than `:appFeatures:profile` because clubs / events /
 * hangouts all already depend on this module and none of them should depend on profile. It
 * cannot live on [TokenManager] itself: `ApiClient` injects the token manager, so taking the
 * api client there would close a DI cycle.
 */
@Singleton
class CurrentUserProvider @Inject constructor(
    apiClient: ApiClientProtocol,
    private val tokenManager: TokenManager,
    private val defaultStorage: DefaultStorage
) : NetworkService(apiClient) {

    /** Cached-then-network. Returns null when there is no session or the lookup fails. */
    suspend fun email(): String? {
        tokenManager.getUserEmail()?.let { return it }

        val userId = tokenManager.getUserId() ?: return null
        val communityId = defaultStorage.getInt(DefaultStorageKey.COMMUNITY_ID, 0)

        val fetched = runCatching { fetch<CurrentUserResponse>(GetUserById(userId, communityId)).mail }
            .getOrNull()
            ?.takeIf { it.isNotBlank() }
            ?: return null

        tokenManager.saveUserEmail(fetched)
        return fetched
    }

    /**
     * `GET api/us/v1/users/{userId}/{communityId}` — the same call the profile and discover
     * modules make. Deliberately **not** `api/us/v1/users/profile`: that route answers 500
     * (verified 2026-09-03). The trailing segment is named `clubId` server-side but carries
     * the community id. Only `mail` is read here; every other field is ignored.
     */
    private data class GetUserById(val userId: String, val communityId: Int) : AppEndpoint {
        override val path = "api/us/v1/users/$userId/$communityId"
        override val method = NetworkMethod.GET
    }

    @Serializable
    private data class CurrentUserResponse(val mail: String? = null)
}
