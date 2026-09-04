package com.bonjur.app.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.bonjur.appwidget.UserCardWidgetStore
import com.bonjur.navigation.Navigator
import com.bonjur.network.manager.TokenManager
import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.profile.presentation.detail.models.ProfileDetail
import com.bonjur.profile.presentation.detail.widget.UserCardWidgetPublisher
import com.bonjur.storage.defaultPreference.DefaultStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    val navigator: Navigator,
    val defaultStorage: DefaultStorage,
    @ApplicationContext private val context: Context,
    private val profileUseCase: ProfileUseCase,
    private val widgetPublisher: UserCardWidgetPublisher,
    private val tokenManager: TokenManager
) : ViewModel() {

    /**
     * Publishes the home-screen card once per install if the app has never written one.
     * It is otherwise only written when the user opens their own profile, so a widget
     * added right after signing in sat there asking the user to sign in again.
     */
    fun publishWidgetCardIfNeeded() {
        if (UserCardWidgetStore.load(context) != null) return
        if (!UserCardWidgetStore.isSignedIn(context)) return
        viewModelScope.launch {
            // `userId = null` = "me", scoped to the community stored at login.
            val userId = tokenManager.getUserId() ?: return@launch
            val profile: ProfileDetail.UIModel =
                runCatching { profileUseCase.fetchProfileData(userId = null) }
                    .getOrNull() ?: return@launch
            widgetPublisher.publish(profile.userCardModel, userId)
        }
    }
}
