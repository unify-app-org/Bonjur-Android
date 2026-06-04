package com.bonjur.designSystem.components.alert

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppAlertPresenter {

    private val _currentAlert = MutableStateFlow<AppAlert?>(null)
    val currentAlert: StateFlow<AppAlert?> = _currentAlert.asStateFlow()

    fun present(alert: AppAlert) {
        _currentAlert.value = alert
    }

    fun dismiss() {
        val alert = _currentAlert.value
        _currentAlert.value = null
        alert?.onDismiss?.invoke()
    }
}

@Composable
fun AppAlertOverlay() {
    val alert by AppAlertPresenter.currentAlert.collectAsState()
    val isVisible = alert != null

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (alert?.dismissOnBackgroundTap == true) {
                        AppAlertPresenter.dismiss()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(200)
                ) + fadeIn(animationSpec = tween(200)),
                exit = scaleOut(
                    targetScale = 0.9f,
                    animationSpec = tween(150)
                ) + fadeOut(animationSpec = tween(150))
            ) {
                alert?.let { currentAlert ->
                    AppAlertView(
                        alert = currentAlert,
                        onDismiss = { handler ->
                            if (handler != null) {
                                handler.invoke()
                            } else {
                                AppAlertPresenter.dismiss()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        }
    }
}
