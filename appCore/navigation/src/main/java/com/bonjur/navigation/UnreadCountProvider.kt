package com.bonjur.navigation

/**
 * Cross-feature hook for the Discover bell badge. Lives in the shared navigation
 * module so `discover` can read the notification unread total WITHOUT a Gradle
 * dependency on the `notification` module — the binding is provided app-wide by
 * notification's Hilt module. Mirrors iOS `NotificationModule.fetchUnreadCount()`.
 */
interface UnreadCountProvider {
    /** Current unread notification total; implementations swallow errors → 0. */
    suspend fun unreadCount(): Int
}
