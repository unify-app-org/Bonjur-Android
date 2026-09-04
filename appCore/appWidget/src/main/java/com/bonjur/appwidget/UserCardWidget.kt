package com.bonjur.appwidget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.compose.ui.unit.sp
import kotlin.math.min
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * Home-screen mirror of `UserCardView`: cover-coloured background with the
 * `CardBackgroundView` ring, avatar + name + speciality, community badge, the
 * course/degree/entry row and the email strip under a divider. Only the sizes shrink.
 *
 * Renders whatever the app last wrote to [UserCardWidgetStore]; it never calls the
 * API itself (same constraint as the iOS extension, which has no session).
 */
class UserCardWidget : GlanceAppWidget() {

    /**
     * Exact, not `Responsive`: the cover's ring is drawn into a bitmap sized from
     * `LocalSize`, and a responsive bucket reports the *bucket's* size, so the ring
     * came out stretched into an oval on any widget taller than the bucket.
     */
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // Read INSIDE the composition. `provideGlance` runs once per session and
            // then suspends in `provideContent`, so anything loaded above this lambda
            // is captured once and frozen: every later `updateAll` recomposed with the
            // first snapshot, and the card stayed one publish behind the store.
            val stored = UserCardWidgetStore.load(context)
            val avatar = UserCardWidgetStore.loadAvatar(context)

            // Three states. A missing snapshot is NOT the same as signed out: the
            // card is published on the first own-profile load, so a user who just
            // signed in has none yet and must not be told to sign in again.
            when {
                stored != null -> UserCardWidgetContent(snapshot = stored, avatar = avatar)

                UserCardWidgetStore.isSignedIn(context) -> MessageContent(
                    title = context.getString(R.string.widget_card_pending_title),
                    subtitle = context.getString(R.string.widget_card_pending_subtitle)
                )

                else -> MessageContent(
                    title = context.getString(R.string.widget_sign_in_title),
                    subtitle = context.getString(R.string.widget_sign_in_subtitle)
                )
            }
        }
    }

    companion object {
        /** Below either of these the card drops the course/degree/entry row, like iOS `.systemSmall`. */
        val COMPACT_WIDTH = 200.dp
        val INFO_ROW_MIN_HEIGHT = 135.dp
    }
}

@Composable
private fun UserCardWidgetContent(
    snapshot: UserCardWidgetSnapshot,
    avatar: android.graphics.Bitmap?
) {
    val context = LocalContext.current
    val size = LocalSize.current
    val density = context.resources.displayMetrics.density
    // The course/degree/entry row is the first thing to go: it needs both width to
    // lay out three columns and height to sit between the name block and the strip.
    val showInfoRow = size.width >= UserCardWidget.COMPACT_WIDTH &&
        size.height >= UserCardWidget.INFO_ROW_MIN_HEIGHT
    val isCompact = !showInfoRow

    // The iOS card is a fixed ~2:1 tile; an Android cell is whatever height the
    // launcher hands out. So the card is *scaled* to the cell rather than padded out
    // — otherwise the slack pools into dead bands between the rows, which is what
    // made the first cut look stretched and empty.
    //
    // Height drives the scale (that is the axis that varies); width only clamps it,
    // so a short-but-wide cell does not blow the text past the edges.
    // Measured against the natural height of *this* variant — with the info row gone
    // the card is much shorter, and scaling it against the full-card baseline made the
    // content overflow and clipped the email strip off the bottom.
    val baseHeight = if (showInfoRow) BASE_HEIGHT_FULL else BASE_HEIGHT_COMPACT
    val scale = min(
        size.height.value / baseHeight,
        size.width.value / BASE_WIDTH * WIDTH_SLACK
    ).coerceIn(MIN_SCALE, MAX_SCALE)

    val cover = snapshot.background?.let { AppUIEntities.BackgroundType.fromApi(it) }
    // Card text colour: the cover decides it, exactly like `UserCardView` —
    // except that view hardcodes blackHigh, so covers keep their own contrast here.
    val foreground = cover?.foregroundColor ?: Palette.blackHigh
    // The email strip keeps its own fill: green on the plain white card, the cover
    // colour otherwise (mirrors `EmailView`'s `bgType == null` branch).
    val stripColor = cover?.bgColor ?: Palette.primary

    val launchIntent = context.packageManager
        .getLaunchIntentForPackage(context.packageName)
        ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    var root = GlanceModifier.fillMaxSize()
    if (launchIntent != null) {
        root = root.clickable(actionStartActivity(launchIntent))
    }

    val sidePadding = (14 * scale).dp

    Box(modifier = root) {
        CardBackground(
            cover = cover,
            widthPx = (size.width.value * density).toInt(),
            heightPx = (size.height.value * density).toInt()
        )

        Column(modifier = GlanceModifier.fillMaxSize()) {
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(horizontal = sidePadding, vertical = (11 * scale).dp),
                verticalAlignment = Alignment.Top
            ) {
                Avatar(
                    bitmap = avatar,
                    sideDp = (40 * scale),
                    cornerRadiusDp = (12 * scale),
                    density = density
                )

                Spacer(modifier = GlanceModifier.width((10 * scale).dp))

                Column(modifier = GlanceModifier.defaultWeight()) {
                    Text(
                        text = snapshot.nameSurname,
                        maxLines = if (isCompact) 2 else 1,
                        style = TextStyle(
                            fontSize = (13.5f * scale).sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(foreground)
                        )
                    )
                    if (snapshot.speciality.isNotEmpty()) {
                        Text(
                            text = snapshot.speciality,
                            maxLines = 1,
                            style = TextStyle(
                                fontSize = (10.5f * scale).sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorProvider(foreground)
                            )
                        )
                    }
                    if (snapshot.community.isNotEmpty()) {
                        Spacer(modifier = GlanceModifier.height((6 * scale).dp))
                        CommunityBadge(
                            community = snapshot.community,
                            hasCover = cover != null,
                            fontSize = (9 * scale),
                            scale = scale
                        )
                    }
                }
            }

            // Whatever is left after scaling — a thin band, not the old dead half.
            Spacer(modifier = GlanceModifier.defaultWeight())

            // Course / Degree / Entry — dropped on the narrow size, where iOS also
            // shows only the avatar, name and badge.
            if (showInfoRow) {
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(horizontal = sidePadding)
                        .padding(bottom = (10 * scale).dp)
                ) {
                    InfoItem(
                        context.getString(R.string.widget_user_card_course),
                        snapshot.course, foreground, scale, GlanceModifier.defaultWeight()
                    )
                    InfoItem(
                        context.getString(R.string.widget_user_card_degree),
                        snapshot.degree, foreground, scale, GlanceModifier.defaultWeight()
                    )
                    InfoItem(
                        context.getString(R.string.widget_user_card_entry),
                        snapshot.entryYear, foreground, scale, GlanceModifier.defaultWeight()
                    )
                }
            }

            EmailStrip(
                email = snapshot.email,
                foreground = foreground,
                stripColor = stripColor,
                scale = scale,
                sidePadding = sidePadding
            )
        }
    }
}

/**
 * Tile for the states with no card to draw — signed out, or signed in before the
 * app has published one. Opens the app on tap instead of rendering placeholder
 * identity data.
 */
@Composable
private fun MessageContent(title: String, subtitle: String) {
    val context = LocalContext.current
    val launchIntent = context.packageManager
        .getLaunchIntentForPackage(context.packageName)
        ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    var root = GlanceModifier.fillMaxSize().background(Palette.white)
    if (launchIntent != null) {
        root = root.clickable(actionStartActivity(launchIntent))
    }

    Box(modifier = root, contentAlignment = Alignment.Center) {
        Column(
            modifier = GlanceModifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(com.bonjur.designsystem.R.drawable.ic_user),
                contentDescription = null,
                colorFilter = androidx.glance.ColorFilter.tint(ColorProvider(Palette.blackMedium)),
                modifier = GlanceModifier.size(28.dp)
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = title,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(Palette.blackHigh)
                )
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Text(
                text = subtitle,
                maxLines = 2,
                style = TextStyle(
                    fontSize = 11.sp,
                    color = ColorProvider(Palette.blackMedium)
                )
            )
        }
    }
}

/** Cell size the unscaled type sizes were drawn for — roughly the iOS medium widget. */
private const val BASE_HEIGHT_FULL = 150f
/** Same, for the variant without the course/degree/entry row. */
private const val BASE_HEIGHT_COMPACT = 105f
private const val BASE_WIDTH = 330f
/** How much narrower than [BASE_WIDTH] a cell may be before the width starts shrinking type. */
private const val WIDTH_SLACK = 1.3f
private const val MIN_SCALE = 0.6f
private const val MAX_SCALE = 1.35f

@Composable
private fun CardBackground(
    cover: AppUIEntities.BackgroundType?,
    widthPx: Int,
    heightPx: Int
) {
    if (cover == null) {
        Box(modifier = GlanceModifier.fillMaxSize().background(Palette.white)) {}
        return
    }

    val bitmap = UserCardWidgetBitmaps.coverBackground(
        widthPx = widthPx,
        heightPx = heightPx,
        color = cover.bgColor
    )
    if (bitmap == null) {
        Box(modifier = GlanceModifier.fillMaxSize().background(cover.bgColor)) {}
    } else {
        Image(
            provider = ImageProvider(bitmap),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = GlanceModifier.fillMaxSize()
        )
    }
}

@Composable
private fun Avatar(
    bitmap: android.graphics.Bitmap?,
    sideDp: Float,
    cornerRadiusDp: Float,
    density: Float
) {
    val sidePx = (sideDp * density).toInt()
    val cornerPx = cornerRadiusDp * density
    val borderPx = 0.5f * density

    val rendered = bitmap?.let {
        UserCardWidgetBitmaps.roundedAvatar(
            source = it,
            sidePx = sidePx,
            cornerRadiusPx = cornerPx,
            borderColor = Palette.blackHigh,
            borderWidthPx = borderPx
        )
    }

    if (rendered != null) {
        Image(
            provider = ImageProvider(rendered),
            contentDescription = null,
            modifier = GlanceModifier.size(sideDp.dp)
        )
        return
    }

    // No avatar stored yet: the grey tile with the person glyph, same as the app's
    // `placeholder` / `error` branches.
    val tile = UserCardWidgetBitmaps.roundedFill(
        sidePx = sidePx,
        cornerRadiusPx = cornerPx,
        fill = Palette.grayQuaternary,
        borderColor = Palette.blackHigh,
        borderWidthPx = borderPx
    )
    Box(
        modifier = GlanceModifier.size(sideDp.dp),
        contentAlignment = Alignment.Center
    ) {
        if (tile != null) {
            Image(
                provider = ImageProvider(tile),
                contentDescription = null,
                modifier = GlanceModifier.size(sideDp.dp)
            )
        }
        Image(
            provider = ImageProvider(com.bonjur.designsystem.R.drawable.ic_user),
            contentDescription = null,
            colorFilter = androidx.glance.ColorFilter.tint(ColorProvider(Palette.blackMedium)),
            modifier = GlanceModifier.size((sideDp * 0.45f).dp)
        )
    }
}

@Composable
private fun CommunityBadge(
    community: String,
    hasCover: Boolean,
    fontSize: Float,
    scale: Float
) {
    Box(
        modifier = GlanceModifier.background(
            ImageProvider(
                if (hasCover) R.drawable.widget_badge_translucent else R.drawable.widget_badge_primary
            )
        )
    ) {
        Text(
            text = community,
            maxLines = 1,
            modifier = GlanceModifier.padding(
                horizontal = (10 * scale).dp,
                vertical = (3 * scale).dp
            ),
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(Palette.blackHigh)
            )
        )
    }
}

@Composable
private fun InfoItem(
    title: String,
    value: String,
    foreground: Color,
    scale: Float,
    modifier: GlanceModifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            maxLines = 1,
            style = TextStyle(fontSize = (8.5f * scale).sp, color = ColorProvider(foreground))
        )
        Text(
            text = value.ifEmpty { "—" },
            maxLines = 1,
            style = TextStyle(
                fontSize = (10.5f * scale).sp,
                fontWeight = FontWeight.Medium,
                color = ColorProvider(foreground)
            )
        )
    }
}

@Composable
private fun EmailStrip(
    email: String,
    foreground: Color,
    stripColor: Color,
    scale: Float,
    sidePadding: androidx.compose.ui.unit.Dp
) {
    val fontSize = 10 * scale
    Column(modifier = GlanceModifier.fillMaxWidth().background(stripColor)) {
        // Glance has no divider; a 1dp filled Box is the RemoteViews equivalent
        // (0.5dp would round to 1px anyway).
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ColorProvider(foreground.copy(alpha = 0.3f)))
        ) {}

        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(horizontal = sidePadding, vertical = (8 * scale).dp)
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_widget_mail),
                contentDescription = null,
                colorFilter = androidx.glance.ColorFilter.tint(ColorProvider(foreground)),
                modifier = GlanceModifier.size((fontSize + 3f).dp)
            )
            Spacer(modifier = GlanceModifier.width((6 * scale).dp))
            Text(
                text = email,
                maxLines = 1,
                style = TextStyle(fontSize = fontSize.sp, color = ColorProvider(foreground))
            )
        }
    }
}
