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
}

/** Neutral club-details payload usable across feature modules. */
data class ClubDetailsNavArgs(val clubId: Int)
