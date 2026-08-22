
package com.bonjur.designSystem.commonModel

import com.bonjur.designSystem.localization.LanguageManager
import androidx.annotation.StringRes
import com.bonjur.designsystem.R
import androidx.compose.ui.graphics.Color
import com.bonjur.designSystem.ui.theme.colors.Palette
import java.util.UUID

object AppUIEntities {
    
    // MARK: - Member Model
    
    data class Member(
        val uuid: UUID = UUID.randomUUID(),
        val id: Int,
        val profileImage: String?
    )
    
    // MARK: - Tags
    
    data class Tags(
        val uuid: UUID = UUID.randomUUID(),
        val id: Int,
        val type: String,
        val title: String
    )
    
    // MARK: - Access Type
    
    /**
     * Canonical API parsers live on the enums themselves, mirroring iOS where each
     * concept is ONE `String`-backed enum. Per-module `when` copies drifted: clubs
     * mapped PURPLE to pink, groups compared case-sensitively, and the colour table
     * sent PRIMARY/SECONDARY/TERTIARY, which the backend enum does not define.
     */
    enum class AccessType {
        PUBLIC,
        PRIVATE;

        companion object {
            /** Unknown/absent → PRIVATE, matching iOS `AccessType.defaultValue`. */
            fun fromApi(raw: String?): AccessType =
                if (raw?.uppercase() == "PUBLIC") PUBLIC else PRIVATE
        }
    }
    
    // MARK: - Request Type
    
    enum class RequestType {
        JOINED,
        REJECTED,
        PENDING,
        NONE;

        companion object {
            /** Unknown/absent → NONE, matching iOS `RequestType.defaultValue`. */
            fun fromApi(raw: String?): RequestType = when (raw?.uppercase()) {
                "JOINED", "ACCEPTED" -> JOINED
                "PENDING" -> PENDING
                "REJECTED" -> REJECTED
                else -> NONE
            }
        }
    }

    // MARK: - Club Status

    /** Verification state of a club. Mirrors iOS `AppPresentationModel.ClubStatus`. */
    enum class ClubStatus {
        VERIFIED,
        UNVERIFIED,
        PENDING,

        /** Admin rejected the verification request; the club may request again. */
        REJECTED;

        val isVerified: Boolean get() = this == VERIFIED

        companion object {
            /** Maps the API string; unknown/null → null. */
            fun from(raw: String?): ClubStatus? = when (raw?.uppercase()) {
                "VERIFIED" -> VERIFIED
                "UNVERIFIED" -> UNVERIFIED
                "PENDING" -> PENDING
                "REJECTED" -> REJECTED
                else -> null
            }
        }
    }

    // MARK: - Background Color Type
    
    sealed class BackgroundType {
        /// green
        object Primary : BackgroundType()
        /// blue
        object Secondary : BackgroundType()
        /// purple
        object Tertiary : BackgroundType()
        data class CustomColor(val colorType: ColorType) : BackgroundType()

        /**
         * Wire value for `backgroundColour`. The backend enum is
         * `az.unify.app.clubservice.enums.BackgroundColour` — colour names, NOT
         * PRIMARY/SECONDARY/TERTIARY. Sending the latter fails the request with
         * "no enum constant", which is what broke club create/edit on Android.
         * Matches iOS `AppPresentationModel.BackgroundType` raw values.
         */
        val apiValue: String
            get() = when (this) {
                is Primary -> "GREEN"
                is Secondary -> "BLUE"
                is Tertiary -> "PURPLE"
                is CustomColor -> when (colorType) {
                    is ColorType.Orange -> "ORANGE"
                    is ColorType.Red -> "RED"
                    is ColorType.Pink -> "PINK"
                    is ColorType.Custom -> "GREEN"
                }
            }

        companion object {
            /**
             * Single parser for every module. The backend can also return WHITE /
             * BLACK / YELLOW / GRAY, which have no card style here — those fall back
             * to [Primary], mirroring iOS's `CaseIterableWithDefault` default.
             * PRIMARY/SECONDARY/TERTIARY are accepted for older stored values.
             */
            fun fromApi(raw: String?): BackgroundType = when (raw?.uppercase()) {
                "GREEN", "PRIMARY" -> Primary
                "BLUE", "SECONDARY" -> Secondary
                "PURPLE", "TERTIARY" -> Tertiary
                "RED" -> CustomColor(ColorType.Red)
                "ORANGE" -> CustomColor(ColorType.Orange)
                "PINK" -> CustomColor(ColorType.Pink)
                else -> Primary
            }
        }
        
        val bgColor: Color
            get() = when (this) {
                is Primary -> Palette.primary
                is Secondary -> Palette.cardBgSecondary
                is Tertiary -> Palette.cardBgTertiary
                is CustomColor -> when (colorType) {
                    is ColorType.Orange -> Palette.cardBgOrange
                    is ColorType.Red -> Palette.cardBgRed
                    is ColorType.Pink -> Palette.cardBgPink
                    is ColorType.Custom -> colorType.color
                }
            }
        
        val foregroundColor: Color
            get() = when (this) {
                is Primary, is Tertiary -> Palette.blackHigh
                is Secondary -> Palette.whiteHigh
                is CustomColor -> when (colorType) {
                    is ColorType.Red -> Palette.whiteHigh
                    is ColorType.Pink, is ColorType.Orange -> Palette.blackHigh
                    is ColorType.Custom -> colorType.foregroundColor
                }
            }
        
        val arrowTint: Color
            get() = when (this) {
                is Primary -> Palette.whiteHigh
                is Tertiary, is Secondary -> Palette.blackHigh
                is CustomColor -> when (colorType) {
                    is ColorType.Pink, is ColorType.Red, is ColorType.Orange -> Palette.blackHigh
                    is ColorType.Custom -> colorType.arrowTint
                }
            }
        
        val arrowBgColor: Color
            get() = when (this) {
                is Primary -> Palette.cardBgSecondary
                is Tertiary, is Secondary -> Palette.primary
                is CustomColor -> when (colorType) {
                    is ColorType.Pink, is ColorType.Red, is ColorType.Orange -> Palette.whiteHigh
                    is ColorType.Custom -> colorType.arrowBgColor
                }
            }
    }
    
    sealed class ColorType {
        object Orange : ColorType()
        object Red : ColorType()
        object Pink : ColorType()
        data class Custom(
            val color: Color,
            val foregroundColor: Color,
            val arrowBgColor: Color = Color.White,
            val arrowTint: Color = Palette.blackHigh
        ) : ColorType()
    }
    
    // MARK: - Activity Types
    
    enum class ActivityType {
        COMMUNITY,
        EVENTS,
        CLUBS,
        HANG_OUTS
    }

    /**
     * [title] is the plural **section header** ("Members"); [displayTitle] is the
     * singular label for chips and card badges ("Member"). Using [title] on a card
     * made a plain member's club card read "TEST2 • 0 events • Members".
     * Mirrors iOS `UserActivityRole.displayTitle`.
     */
    enum class UserActivityRole(
        @StringRes private val titleRes: Int?,
        @StringRes private val displayTitleRes: Int?
    ) {
        MEMBER(R.string.common_members, R.string.member),
        PRESIDENT(R.string.role_president, R.string.role_president),
        VISE_PRESIDENT(R.string.vise_president, R.string.role_vice_president),
        EVENT_CREATOR(R.string.role_event_creators, R.string.creator),
        NOT_JOINED(null, null);

        // Resolved on read, never in the constructor: enum constants initialize at
        // class load, before LanguageManager has a Context, and a value captured
        // there could never follow a language switch.
        val title: String get() = titleRes?.let { LanguageManager.string(it) } ?: ""
        val displayTitle: String get() = displayTitleRes?.let { LanguageManager.string(it) } ?: ""

        companion object {
            /**
             * Unknown roles (including the backend's `REQUESTED`) → NOT_JOINED, matching
             * iOS `UserActivityRole.defaultValue`. Both spellings of vice-president are
             * accepted: the server sends `VICE_PRESIDENT`, older payloads `VISE_PRESIDENT`.
             */
            fun fromApi(raw: String?): UserActivityRole = when (raw?.uppercase()) {
                "MEMBER" -> MEMBER
                "PRESIDENT" -> PRESIDENT
                "VICE_PRESIDENT", "VISE_PRESIDENT" -> VISE_PRESIDENT
                "EVENT_CREATOR" -> EVENT_CREATOR
                else -> NOT_JOINED
            }
        }
    }

}

/** Maps a backend role code (e.g. "VISE_PRESIDENT") to [AppUIEntities.UserActivityRole]. */
fun String.toUserActivityRole(): AppUIEntities.UserActivityRole = when (uppercase()) {
    "MEMBER" -> AppUIEntities.UserActivityRole.MEMBER
    "PRESIDENT" -> AppUIEntities.UserActivityRole.PRESIDENT
    "VICE_PRESIDENT" -> AppUIEntities.UserActivityRole.VISE_PRESIDENT
    "EVENT_CREATOR" -> AppUIEntities.UserActivityRole.EVENT_CREATOR
    else -> AppUIEntities.UserActivityRole.NOT_JOINED
}