package com.bonjur.appwidget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll

/** Manifest entry point for [UserCardWidget]; declared in this module's manifest. */
class UserCardWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = UserCardWidget()
}

/**
 * Redraws every placed instance of the card.
 *
 * The app's equivalent of iOS `WidgetCenter.reloadTimelines(ofKind:)` — call it
 * after writing a new snapshot, never on a schedule.
 */
suspend fun reloadUserCardWidget(context: Context) {
    runCatching { UserCardWidget().updateAll(context.applicationContext) }
        .onFailure { Log.w(TAG, "widget reload failed", it) }
        .onSuccess { Log.d(TAG, "widget reload requested") }
}

private const val TAG = "UserCardWidget"
