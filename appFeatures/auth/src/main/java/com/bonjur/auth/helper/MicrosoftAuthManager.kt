package com.bonjur.auth.helper

import android.app.Activity
import android.content.Context
import com.bonjur.auth.R
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.SignInParameters
import com.microsoft.identity.client.exception.MsalException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class MsalSignInResult(
    val name: String?,
    val email: String?,
    val error: Throwable?
)

@Singleton
class MicrosoftAuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val scopes = listOf("User.Read")
    @Volatile
    private var cachedApp: ISingleAccountPublicClientApplication? = null
    private val appMutex = Mutex()

    private suspend fun application(): ISingleAccountPublicClientApplication =
        cachedApp ?: appMutex.withLock {
            cachedApp ?: createApplication().also { cachedApp = it }
        }

    private suspend fun createApplication(): ISingleAccountPublicClientApplication =
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { cont ->
                PublicClientApplication.createSingleAccountPublicClientApplication(
                    context,
                    R.raw.msal_auth_config,
                    object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                        override fun onCreated(application: ISingleAccountPublicClientApplication) {
                            cont.resume(application)
                        }

                        override fun onError(exception: MsalException) {
                            cont.resumeWithException(exception)
                        }
                    }
                )
            }
        }

    /**
     * Launches interactive Microsoft sign-in. Mirrors iOS `buildMsalWeb`.
     * Signs out any cached account first to force a fresh org account picker.
     */
    suspend fun signIn(activity: Activity): MsalSignInResult {
        return try {
            val app = application()
            signOutIfNeeded(app)
            val result = withContext(Dispatchers.Main) { acquireToken(app, activity) }
            val claims = result.account.claims
            MsalSignInResult(
                name = claims?.get("name") as? String,
                email = (claims?.get("email") as? String)
                    ?: (claims?.get("preferred_username") as? String),
                error = null
            )
        } catch (e: Throwable) {
            MsalSignInResult(name = null, email = null, error = e)
        }
    }

    private suspend fun acquireToken(
        app: ISingleAccountPublicClientApplication,
        activity: Activity
    ): IAuthenticationResult = suspendCancellableCoroutine { cont ->
        val params = SignInParameters.builder()
            .withActivity(activity)
            .withScopes(scopes)
            .withCallback(object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult) {
                    cont.resume(authenticationResult)
                }

                override fun onError(exception: MsalException) {
                    cont.resumeWithException(exception)
                }

                override fun onCancel() {
                    cont.resumeWithException(MsalSignInCancelled())
                }
            })
            .build()
        app.signIn(params)
    }

    private suspend fun signOutIfNeeded(
        app: ISingleAccountPublicClientApplication
    ) = withContext(Dispatchers.IO) {
        val current = currentAccount(app) ?: return@withContext
        suspendCancellableCoroutine<Unit> { cont ->
            app.signOut(object : ISingleAccountPublicClientApplication.SignOutCallback {
                override fun onSignOut() {
                    cont.resume(Unit)
                }

                override fun onError(exception: MsalException) {
                    cont.resume(Unit)
                }
            })
        }
    }

    private suspend fun currentAccount(
        app: ISingleAccountPublicClientApplication
    ): IAccount? = suspendCancellableCoroutine { cont ->
        app.getCurrentAccountAsync(object :
            ISingleAccountPublicClientApplication.CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                cont.resume(activeAccount)
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                cont.resume(currentAccount)
            }

            override fun onError(exception: MsalException) {
                cont.resume(null)
            }
        })
    }
}

class MsalSignInCancelled : Exception("Microsoft sign in cancelled")
