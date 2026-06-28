package com.bonjur.navigation

/**
 * Cross-feature navigation contract that lives in the shared navigation module so
 * features can navigate into each other WITHOUT a direct Gradle dependency.
 *
 * `clubs` already depends on `events` (for the events tab card), so `events`
 * cannot depend on `clubs` back — that would be a cycle. Instead, `events`
 * navigates to the club-details route by its qualified-name string and passes
 * [ClubDetailsNavArgs] through [NavArgs]; the `clubs` nav graph reads it as a
 * fallback. iOS solves the same problem via module protocols.
 */
object SharedRoutes {
    /** Qualified name of `ClubsScreens.Details` (the `Any.route` of that object). */
    const val CLUB_DETAILS = "com.bonjur.clubs.navigation.ClubsScreens.Details"

    /** Qualified name of `ClubsScreens.Create` — event-create empty-state funnel. */
    const val CLUB_CREATE = "com.bonjur.clubs.navigation.ClubsScreens.Create"

    /** Qualified name of `ClubsScreens.List` — event-create "browse clubs" funnel. */
    const val CLUB_LIST = "com.bonjur.clubs.navigation.ClubsScreens.List"

    /**
     * Qualified name of `ProfileScreens.ProfileDetail`. `profile` depends on
     * clubs/events/hangouts, so those modules can't depend back on it — they open a
     * member's profile by this route string + [ProfileDetailNavArgs] (read as a fallback
     * by the profile nav graph). Same pattern as [CLUB_DETAILS].
     */
    const val PROFILE_DETAIL = "com.bonjur.profile.navigation.ProfileScreens.ProfileDetail"

    /**
     * Qualified name of `NotificationScreens.Feed`. The Discover bell opens the
     * notification stack by this route string so `discover` needs no dependency
     * on the `notification` module.
     */
    const val NOTIFICATION_FEED = "com.bonjur.notification.navigation.NotificationScreens.Feed"
}

/** Neutral club-details payload usable across feature modules. */
data class ClubDetailsNavArgs(val clubId: Int)

/** Neutral profile-details payload (member tap → profile) usable across feature modules. */
data class ProfileDetailNavArgs(val userId: String?)
