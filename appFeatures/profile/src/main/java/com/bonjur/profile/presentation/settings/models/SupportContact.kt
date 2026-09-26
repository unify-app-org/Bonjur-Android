package com.bonjur.profile.presentation.settings.models

import android.net.Uri

/** Public contact points shown from Settings. Keep in sync with iOS `SupportContact.swift`. */
object SupportContact {
    const val EMAIL = "unifyapp2026@gmail.com"
    const val EMAIL_SUBJECT = "Unify support"

    /**
     * Google Doc (shared "anyone with the link"). `/mobilebasic` is Google's read-only
     * reader view — `/edit` would drop the user into the editor.
     */
    const val TERMS_URL =
        "https://docs.google.com/document/d/15iHIcgQvaHAG80U_0mOgehCm8RQfvn68Qdgdwv8viY0/mobilebasic"

    val emailUri: Uri
        get() = Uri.parse("mailto:$EMAIL?subject=${Uri.encode(EMAIL_SUBJECT)}")
}
