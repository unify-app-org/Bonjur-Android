package com.bonjur.designSystem.commonModel

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R

/**
 * Count labels for cards and detail headers.
 *
 * These go through `plurals`, not string interpolation: the bare `"$count members"`
 * printed "1 members", and Russian needs three forms that iOS's flat `"%d members"`
 * cannot express.
 */
fun memberCountText(count: Int): String =
    LanguageManager.plural(R.plurals.members_count, count)

fun clubCountText(count: Int): String =
    LanguageManager.plural(R.plurals.clubs_count, count)

fun eventCountText(count: Int): String =
    LanguageManager.plural(R.plurals.events_count, count)

/** "3 of 25 members" — capacity-style label. */
fun memberOfCapacityText(count: Int, capacity: Int): String =
    LanguageManager.string(R.string.count_of_members, count, capacity)
