package com.bonjur.groups.data.models

/**
 * Pagination + search query for the joined-activity endpoints.
 * Mirrors iOS `GroupsDTOModel.PaginationQuery` (page, size, keyword).
 * `keyword` is sent only when non-blank.
 */
data class GroupsPaginationQuery(
    val page: Int,
    val size: Int,
    val keyword: String?
) {
    fun toMap(): Map<String, String> {
        // NB: build with mutableMapOf, not buildMap { ... } — inside buildMap's
        // MutableMap receiver, `size` resolves to the map's own `.size` property
        // (shadowing this param), which made every request go out as size=1.
        val params = mutableMapOf(
            "page" to page.toString(),
            "size" to size.toString()
        )
        keyword?.takeIf { it.isNotBlank() }?.let { params["keyword"] = it }
        return params
    }
}
