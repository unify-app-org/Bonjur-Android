package com.bonjur.network.model

import kotlinx.serialization.Serializable

@Serializable
class EmptyData

/** Paginated wrapper used by list endpoints. Mirrors iOS `PageNationResponse`. */
@Serializable
data class PageNationResponse<T>(
    val content: T,
    val totalElements: Int? = null,
    val size: Int? = null,
    val numberOfElements: Int? = null,
    val totalPages: Int? = null,
    val page: Int? = null
)