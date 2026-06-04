package com.bonjur.designSystem.components.alert

data class AppAlert(
    val config: Config,
    val actions: List<Action>,
    val dismissOnBackgroundTap: Boolean = true,
    val onDismiss: (() -> Unit)? = null
) {
    data class Config(
        val title: String,
        val subtitle: String? = null
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
