package com.bonjur.communities.navigation

import kotlinx.serialization.Serializable

sealed interface CommunitiesScreens {
    @Serializable
    data object List : CommunitiesScreens

    @Serializable
    data object Details : CommunitiesScreens

    // Payloads pass via NavArgs (matching Details) — the string-based Navigator routes
    // by qualified name, so parameterized `data class` routes are UNREACHABLE and crash
    // with "destination ... cannot be found". Keep these as `data object`. See ClubsScreens.
    // (See-all members now uses the shared member.list.MemberListScreens.)
    @Serializable
    data object FacultyBrowse : CommunitiesScreens

    @Serializable
    data object FacultySelection : CommunitiesScreens

    @Serializable
    data object FacultyStudentList : CommunitiesScreens

    @Serializable
    data object FacultyStudentSelectList : CommunitiesScreens
}
