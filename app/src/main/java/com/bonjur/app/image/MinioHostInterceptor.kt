package com.bonjur.app.image

import android.net.Uri
import androidx.core.net.toUri
import coil.intercept.Interceptor
import coil.request.ImageResult
import java.net.URL

/**
 * Rewrites image URLs whose host is the internal `minio` host to the public
 * API base host, mirroring iOS `CachedAsyncImage.resolvedURL`. The port and
 * path are preserved; only the host (and scheme, if missing) is swapped.
 *
 * Registered once on the singleton [coil.ImageLoader] (see MainApplication),
 * so every call site stays unaware of the rewrite.
 */
class MinioHostInterceptor(
    baseUrl: String
) : Interceptor {

    private val baseHost: String? = baseUrl.toUri().host
    private val baseScheme: String? = baseUrl.toUri().scheme

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val rewritten = rewrite(chain.request.data)
            ?: return chain.proceed(chain.request)

        val request = chain.request.newBuilder()
            .data(rewritten)
            .build()
        return chain.proceed(request)
    }

    private fun rewrite(data: Any): String? {
        val original = when (data) {
            is String -> data
            is Uri -> data.toString()
            is URL -> data.toString()
            else -> return null
        }

        val uri = original.toUri()
        if (uri.host != MINIO_HOST) return null
        val host = baseHost ?: return null

        val authority = if (uri.port != -1) "$host:${uri.port}" else host
        return uri.buildUpon()
            .encodedAuthority(authority)
            .scheme(uri.scheme ?: baseScheme)
            .build()
            .toString()
    }

    private companion object {
        const val MINIO_HOST = "minio"
    }
}
