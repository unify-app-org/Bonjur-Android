package com.bonjur.storage.securePreference

enum class SecureStorageKey(val key: String) {
    AUTH_TOKEN("auth_token"),
    REFRESH_TOKEN("refresh_token"),
    USER_ID("user_id"),

    /** Email the user signed in with. Prefills owner contact on the create forms. */
    USER_EMAIL("user_email"),
}
