package com.bonjur.appwidget

import org.json.JSONObject

/**
 * The slice of the user card the home-screen widget renders.
 *
 * The widget is rendered by the launcher and has no session, so the app writes
 * this snapshot every time it loads the signed-in user's profile and the widget
 * only ever reads it. Mirrors iOS `UserCardWidgetSnapshot`.
 */
data class UserCardWidgetSnapshot(
    val userId: String,
    val nameSurname: String,
    val speciality: String,
    val course: String,
    val community: String,
    val degree: String,
    val entryYear: String,
    val email: String,
    /**
     * Raw `AppUIEntities.BackgroundType.apiValue` ("GREEN", "BLUE", …) — the cover
     * the user picked for their card. `null` = the plain white card.
     */
    val background: String?,
    val updatedAt: Long = System.currentTimeMillis()
) {

    fun toJson(): String = JSONObject().apply {
        put(KEY_USER_ID, userId)
        put(KEY_NAME, nameSurname)
        put(KEY_SPECIALITY, speciality)
        put(KEY_COURSE, course)
        put(KEY_COMMUNITY, community)
        put(KEY_DEGREE, degree)
        put(KEY_ENTRY_YEAR, entryYear)
        put(KEY_EMAIL, email)
        // JSONObject.put(String, null) removes the key, which is what we want:
        // a missing background decodes back to the plain white card.
        put(KEY_BACKGROUND, background)
        put(KEY_UPDATED_AT, updatedAt)
    }.toString()

    companion object {
        private const val KEY_USER_ID = "userId"
        private const val KEY_NAME = "nameSurname"
        private const val KEY_SPECIALITY = "speciality"
        private const val KEY_COURSE = "course"
        private const val KEY_COMMUNITY = "community"
        private const val KEY_DEGREE = "degree"
        private const val KEY_ENTRY_YEAR = "entryYear"
        private const val KEY_EMAIL = "email"
        private const val KEY_BACKGROUND = "background"
        private const val KEY_UPDATED_AT = "updatedAt"

        /** `null` when the stored blob is absent or was written by an older, incompatible build. */
        fun fromJson(raw: String?): UserCardWidgetSnapshot? {
            if (raw.isNullOrEmpty()) return null
            return runCatching {
                val json = JSONObject(raw)
                UserCardWidgetSnapshot(
                    userId = json.optString(KEY_USER_ID),
                    nameSurname = json.optString(KEY_NAME),
                    speciality = json.optString(KEY_SPECIALITY),
                    course = json.optString(KEY_COURSE),
                    community = json.optString(KEY_COMMUNITY),
                    degree = json.optString(KEY_DEGREE),
                    entryYear = json.optString(KEY_ENTRY_YEAR),
                    email = json.optString(KEY_EMAIL),
                    background = if (json.isNull(KEY_BACKGROUND)) null else json.optString(KEY_BACKGROUND),
                    updatedAt = json.optLong(KEY_UPDATED_AT)
                )
            }.getOrNull()
        }

        /** Shown in the widget picker and before the user has ever opened Profile. */
        val placeholder = UserCardWidgetSnapshot(
            userId = "",
            nameSurname = "Huseyn Hasanov",
            speciality = "Oil-gas engineering",
            course = "2nd year",
            community = "UFAZ",
            degree = "Bachelor",
            entryYear = "2025",
            email = "h.hasanov@unify.com",
            background = "GREEN"
        )
    }
}
