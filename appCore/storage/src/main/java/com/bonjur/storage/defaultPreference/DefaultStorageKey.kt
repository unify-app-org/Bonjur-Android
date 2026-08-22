package com.bonjur.storage.defaultPreference

enum class DefaultStorageKey(val key: String) {
    USERNAME("username"),
    IS_AUTHENTICATED("is_authenticated"),
    LAST_OPEN_DATE("last_open_date"),
    COMMUNITY_ID("community_id"),

    /** Raw community role from the login response, e.g. "PRESIDENT". */
    USER_COMMUNITY_ROLE("user_community_role"),

    /** User ticked "Don't show this again" on the event-reminder warning alert. */
    HIDE_EVENT_REMINDER_WARNING("hide_event_reminder_warning"),
}

