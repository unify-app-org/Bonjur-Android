package com.bonjur.designSystem.components.alert

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R

data class AppAlert(
    val config: Config,
    val actions: List<Action>,
    val dismissOnBackgroundTap: Boolean = true,
    val onDismiss: (() -> Unit)? = null
) {
    data class Config(
        val title: String,
        val subtitle: String? = null,
        /** Optional opt-out row rendered between the text and the actions. Mirrors iOS. */
        val checkbox: Checkbox? = null
    )

    /**
     * Opt-out checkbox inside an alert ("Don't show this again"). The caller owns
     * the persistence — the alert only reports the toggle through [onChange].
     */
    data class Checkbox(
        val title: String,
        val isOn: Boolean = false,
        val onChange: (Boolean) -> Unit
    )

    data class Action(
        val title: String,
        val style: Style = Style.PRIMARY,
        val handler: (() -> Unit)? = null
    ) {
        enum class Style {
            PRIMARY,
            SECONDARY,
            DESTRUCTIVE
        }
    }
}
