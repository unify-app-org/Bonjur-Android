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

/** "3 of 25 members" — the wording the **cards** use (iOS `count_of_members`). */
fun memberOfCapacityText(count: Int, capacity: Int): String =
    LanguageManager.string(R.string.count_of_members, count, capacity)

/**
 * "3/25 members" — the **detail screens'** Capacity row.
 *
 * Deliberately a different shape from [memberOfCapacityText]: iOS renders the card with
 * `count_of_members` ("3 of 25 members") and the detail row with a slash, and Android's
 * detail rows were reusing the card wording. Unlike iOS — which hardcodes English
 * " members" in every repo's `capacityText` — this reads a resource, so az/ru follow.
 */
fun capacityOfMembersText(count: Int, capacity: Int): String =
    LanguageManager.string(R.string.capacity_of_members, count, capacity)
