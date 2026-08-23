package com.bonjur.app.fcm

import com.bonjur.designSystem.localization.LanguageManager
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bonjur.app.MainActivity
import com.bonjur.app.R
import com.bonjur.app.fcm.data.DeviceDataSource
import com.bonjur.apputils.DeviceManager
import com.bonjur.storage.notification.NotificationPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UnifyMessagingService : FirebaseMessagingService() {

    @Inject lateinit var deviceDataSource: DeviceDataSource
    @Inject lateinit var deviceManager: DeviceManager
    @Inject lateinit var notificationPreferences: NotificationPreferences

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /** Called when a new registration token is generated (install, reinstall, data wipe). */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Mirrors iOS `messaging(_:didReceiveRegistrationToken:)` → PUT api/as/v1/device/{id}.
        scope.launch {
            runCatching { deviceDataSource.updateFcmToken(deviceManager.deviceId, token) }
                .onFailure { Log.w("FCM", "updateFcmToken failed (unauthenticated?)", it) }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    /** Called when an FCM message arrives while the app is in the foreground,
     *  or for data-only messages in any state. */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Settings → Notifications is off: drop it. The OS-level grant is checked
        // separately in showNotification; this is the in-app mute.
        if (!notificationPreferences.isEnabledInApp) return
        val title = message.notification?.title ?: message.data["title"] ?: LanguageManager.string(R.string.app_name)
        val body = message.notification?.body ?: message.data["body"] ?: ""
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val channelId = CHANNEL_ID
        val manager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General",
                NotificationManager.IMPORTANCE_HIGH,
            )
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pending = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()

        // On Android 13+ posting requires the runtime POST_NOTIFICATIONS grant.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    companion object {
        const val CHANNEL_ID = "unify_general"
    }
}
