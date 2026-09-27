package com.bonjur.profile.presentation.settings.models

import android.net.Uri
import com.bonjur.designSystem.components.webView.LegalLinks

/** Public contact points shown from Settings. Keep in sync with iOS `SupportContact.swift`. */
object SupportContact {
    const val EMAIL = "unifyapp2026@gmail.com"
    const val EMAIL_SUBJECT = "Unify support"

    /** Lives in designSystem so sign-in can show the same document. */
    const val TERMS_URL = LegalLinks.TERMS_URL

    val emailUri: Uri
        get() = Uri.parse("mailto:$EMAIL?subject=${Uri.encode(EMAIL_SUBJECT)}")
}
