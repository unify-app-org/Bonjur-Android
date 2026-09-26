package com.bonjur.profile.presentation.settings.components

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.profile.R

/**
 * Google's reader view ships its own title bar (with a back arrow that goes nowhere inside
 * the app) and an "open in the Docs app" banner. Our top bar already covers both.
 */
private const val HIDE_GOOGLE_CHROME_JS = """
(function() {
  if (document.getElementById('unify-hide-chrome')) return;
  var style = document.createElement('style');
  style.id = 'unify-hide-chrome';
  style.textContent = '#docs-ml-header-id, .docs-ml-promotion { display: none !important; }';
  document.documentElement.appendChild(style);
})();
"""

/**
 * Read-only in-app page for Settings documents (Terms and conditions). Mirrors iOS
 * `SettingsWebViewController`. Rendered in place of the settings list rather than as its
 * own nav destination, so it inherits the app-level insets and localization as-is.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun SettingsWebView(
    title: String,
    url: String,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.white)
    ) {
        AppTopBar(
            isScrolled = true,
            showTitle = true,
            title = title,
            onBack = onBack
        )

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                                isLoading = true
                            }

                            // Injected on commit (not finish) so the Google header never flashes.
                            override fun onPageCommitVisible(view: WebView, url: String?) {
                                view.evaluateJavascript(HIDE_GOOGLE_CHROME_JS, null)
                            }

                            override fun onPageFinished(view: WebView, url: String?) {
                                view.evaluateJavascript(HIDE_GOOGLE_CHROME_JS, null)
                                isLoading = false
                            }

                            // Links the user taps inside the document open in the browser /
                            // mail app instead of replacing the page they were reading.
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest
                            ): Boolean {
                                if (!request.isForMainFrame || !request.hasGesture()) return false
                                try {
                                    view.context.startActivity(
                                        Intent(Intent.ACTION_VIEW, request.url)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                } catch (_: ActivityNotFoundException) {
                                    // Nothing can open it; stay on the document.
                                }
                                return true
                            }

                            override fun onReceivedError(
                                view: WebView,
                                request: WebResourceRequest,
                                error: WebResourceError
                            ) {
                                if (!request.isForMainFrame) return
                                isLoading = false
                                AppSnackBar.showError(
                                    LanguageManager.string(R.string.settings_web_load_error)
                                )
                            }
                        }
                        loadUrl(url)
                    }
                },
                onRelease = { it.destroy() }
            )

            if (isLoading) {
                CircularProgressIndicator(
                    color = Palette.blackMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
