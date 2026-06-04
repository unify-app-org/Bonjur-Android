package com.bonjur.communities.navigation

import kotlinx.serialization.Serializable

sealed interface CommunitiesScreens {
    @Serializable
    data object List : CommunitiesScreens

    @Serializable
    data object Details : CommunitiesScreens

    @Serializable
    data class FacultyBrowse(val communityId: String, val title: String = "All members") : CommunitiesScreens

    @Serializable
    data class FacultySelection(val communityId: String, val title: String = "Select Faculty") : CommunitiesScreens

    @Serializable
    data class FacultyStudentList(
        val communityId: String,
        val facultyId: String,   // degree string used to filter members
        val title: String = "Members"
    ) : CommunitiesScreens

    @Serializable
    data class FacultyStudentSelectList(
        val communityId: String,
        val facultyId: String,
        val title: String = "Select Member"
    ) : CommunitiesScreens
}
