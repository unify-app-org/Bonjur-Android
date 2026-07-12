package com.bonjur.apputils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stable per-install device identifier.
 *
 * Android analog of iOS `DeviceManager.deviceId` (`UIDevice.identifierForVendor`).
 * Uses `Settings.Secure.ANDROID_ID`: stable for the lifetime of the install on a
 * given device+user+signing-key, survives app restarts, resets on factory reset.
 *
 * Shared across modules (auth login + FCM device register) so both send the SAME
 * device id, matching iOS which uses one id for both.
 */
@Singleton
class DeviceManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    @SuppressLint("HardwareIds")
    val deviceId: String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown"

    val devicePlatform: String = "Android"

    val appVersion: String = "1.0.0"

    val deviceOs: String = "Android ${Build.VERSION.RELEASE}"

    val deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}"
}
