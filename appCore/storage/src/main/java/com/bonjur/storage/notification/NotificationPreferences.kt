package com.bonjur.storage.notification

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Backing store + system bridge for the Settings → Notifications switch.
 *
 * iOS implements the same switch with `registerForRemoteNotifications()` /
 * `unregisterForRemoteNotifications()`. Android has no equivalent: an app cannot
 * grant or revoke its own notification permission, only the OS settings page can.
 * So the switch is two things ANDed together:
 *
 * - [isEnabledInApp]  — the user's in-app mute, persisted here. This is the part
 *   the app really controls: [UnifyMessagingService][com.bonjur.app.fcm] refuses
 *   to post anything while it is off, so muting works even when the OS allows
 *   notifications.
 * - [isEnabledInSystem] — the OS grant (POST_NOTIFICATIONS on 13+, "block
 *   notifications" on any version). Read-only; [openSystemSettings] is the only
 *   way to change it.
 */
@Singleton
class NotificationPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storage: DefaultStorage,
) {
    /** In-app mute. Defaults to on, matching iOS's initial `notificationsEnabled = true`. */
    var isEnabledInApp: Boolean
        get() = storage.getBoolean(DefaultStorageKey.NOTIFICATIONS_ENABLED, true)
        set(value) = storage.saveBoolean(DefaultStorageKey.NOTIFICATIONS_ENABLED, value)

    /** OS-level grant. Re-read every time — the user can change it while the app is backgrounded. */
    val isEnabledInSystem: Boolean
        get() = NotificationManagerCompat.from(context).areNotificationsEnabled()

    /** What the switch shows and what the FCM service gates on. */
    val isEnabled: Boolean
        get() = isEnabledInApp && isEnabledInSystem

    /**
     * Opens this app's notification page in system settings. Launched from the
     * application context, so it needs its own task.
     */
    fun openSystemSettings() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(android.net.Uri.fromParts("package", context.packageName, null))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(intent) }
    }
}
