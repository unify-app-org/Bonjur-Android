package com.bonjur.profile.presentation.settings.components

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.profile.R
import com.bonjur.profile.presentation.settings.models.SupportContact

/** Settings → Help center sheet: how to reach support. Mirrors iOS `HelpCenterView`. */
@Composable
internal fun HelpCenterView() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Palette.white)
            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_help_center),
            style = AppTypography.TitleSm.semiBold,
            color = Palette.black
        )

        Text(
            text = stringResource(R.string.help_center_description),
            style = AppTypography.BodyTextSm.regular,
            color = Palette.blackMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Palette.grayQuaternary, RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { openSupportEmail(context) },
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Palette.grayQuaternary, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_mail),
                        contentDescription = null,
                        tint = Palette.black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.help_center_email_label),
                        style = AppTypography.TextMd.medium,
                        color = Palette.blackMedium
                    )
                    Text(
                        text = SupportContact.EMAIL,
                        style = AppTypography.BodyTextMd.semiBold,
                        color = Palette.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = { openSupportEmail(context) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_send),
                    contentDescription = stringResource(R.string.help_center_send),
                    tint = Palette.blackMedium,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Opens a pre-addressed draft in the user's mail app. No mail app installed means
 * `mailto:` can't open — fall back to copying the address so it isn't lost.
 */
private fun openSupportEmail(context: Context) {
    // Sheet content runs under a re-provided (localized) context that may not be the
    // Activity itself, so the launch can't rely on inheriting its task. Extras back up
    // the `mailto:` query for clients (some Gmail builds) that ignore `?subject=`.
    val intent = Intent(Intent.ACTION_SENDTO, SupportContact.emailUri)
        .putExtra(Intent.EXTRA_EMAIL, arrayOf(SupportContact.EMAIL))
        .putExtra(Intent.EXTRA_SUBJECT, SupportContact.EMAIL_SUBJECT)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        copySupportEmail(context)
    }
}

private fun copySupportEmail(context: Context) {
    val clipboard = context.getSystemService(ClipboardManager::class.java)
    clipboard.setPrimaryClip(ClipData.newPlainText(SupportContact.EMAIL, SupportContact.EMAIL))
    AppSnackBar.show(title = LanguageManager.string(R.string.help_center_email_copied))
}
