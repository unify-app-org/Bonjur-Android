package com.bonjur.network.model

/**
 * One page of already-mapped domain models, plus what the caller needs to ask for the
 * next one. Repos build this from [PageNationResponse] so view models never have to
 * guess whether more rows exist from the row count alone. Mirrors iOS `Page`.
 */
data class Page<T>(
    val items: List<T>,
    /** Zero-based index of the page these items came from. */
    val page: Int,
    val hasMore: Boolean,
    val totalCount: Int? = null
) {
    fun <R> map(transform: (T) -> R): Page<R> =
        Page(items.map(transform), page, hasMore, totalCount)

    companion object {
        fun <T> empty(): Page<T> = Page(emptyList(), page = 0, hasMore = false, totalCount = 0)
    }
}

/**
 * True when a further page exists. Prefers the page metadata; falls back to "this page
 * came back full" so paging still works if `page`/`totalPages` ever go missing (they'd
 * otherwise deserialize as null and silently end the list).
 */
fun PageNationResponse<*>.hasMore(
    requestedPage: Int,
    requestedSize: Int,
    receivedCount: Int
): Boolean {
    val totalPages = totalPages
    if (totalPages != null) return (page ?: requestedPage) + 1 < totalPages
    val received = numberOfElements ?: receivedCount
    val pageSize = size ?: requestedSize
    return pageSize > 0 && received >= pageSize
}

/** Wraps mapped domain models in a [Page], carrying the server's paging metadata over. */
fun <T> PageNationResponse<*>.toPage(
    requestedPage: Int,
    requestedSize: Int,
    items: List<T>
): Page<T> = Page(
    items = items,
    page = page ?: requestedPage,
    hasMore = hasMore(requestedPage, requestedSize, items.size),
    totalCount = totalElements
)
