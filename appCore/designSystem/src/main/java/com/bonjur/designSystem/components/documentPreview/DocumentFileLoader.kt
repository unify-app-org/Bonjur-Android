package com.bonjur.designSystem.components.documentPreview

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * Fetches a remote document into the app cache and hands back a local [File].
 * Previewing needs the bytes on disk, and downloading them here is what keeps
 * the document inside the app instead of handing the URL to a browser.
 *
 * Re-opening the same document is served straight off disk, and two taps on the
 * same row share a single download.
 */
object DocumentFileLoader {

    private const val FOLDER = "document_preview"
    private const val TIMEOUT_MS = 30_000

    private val locks = mutableMapOf<String, Mutex>()
    private val locksGuard = Mutex()

    /**
     * @param preferredName the attachment's display name. The API often sends it
     * **without an extension**, so the real type is resolved from the response and
     * the file's magic bytes — the preview picks its renderer off the extension.
     */
    suspend fun localFile(
        context: Context,
        url: String,
        preferredName: String
    ): File = withContext(Dispatchers.IO) {
        val folder = cacheFolder(context, url)
        cachedFile(folder)?.let { return@withContext it }

        // One download per URL: a second tap waits on the first instead of
        // writing the same file twice.
        lockFor(url).withLock {
            cachedFile(folder) ?: download(url, folder, preferredName)
        }
    }

    private suspend fun lockFor(url: String): Mutex = locksGuard.withLock {
        locks.getOrPut(url) { Mutex() }
    }

    private fun download(url: String, folder: File, preferredName: String): File {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            connectTimeout = TIMEOUT_MS
            readTimeout = TIMEOUT_MS
            instanceFollowRedirects = true
            requestMethod = "GET"
        }
        val partial = File(folder, "download.part")
        try {
            val status = connection.responseCode
            if (status !in 200..299) {
                throw DocumentLoadException("HTTP $status for $url")
            }
            folder.mkdirs()
            connection.inputStream.use { input ->
                partial.outputStream().use { output -> input.copyTo(output) }
            }

            val destination = File(
                folder,
                fileName(
                    preferredName = preferredName,
                    url = url,
                    mimeType = connection.contentType,
                    downloadedFile = partial
                )
            )
            destination.delete()
            // Only publish the final name once the body is fully written, so a
            // cancelled transfer can't be mistaken for a cached file later.
            if (!partial.renameTo(destination)) {
                throw DocumentLoadException("Could not store $url")
            }
            return destination
        } finally {
            partial.delete()
            connection.disconnect()
        }
    }

    /**
     * Each document gets its own hashed folder so two files sharing a name don't
     * overwrite each other. The folder holds exactly one file, which is also how
     * a cache hit is found without knowing the extension up front.
     */
    private fun cacheFolder(context: Context, url: String): File {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(url.toByteArray())
            .joinToString("") { "%02x".format(it) }
            .take(16)
        return File(File(context.cacheDir, FOLDER), digest).apply { mkdirs() }
    }

    private fun cachedFile(folder: File): File? =
        folder.listFiles()
            ?.firstOrNull { it.isFile && it.length() > 0 && it.extension != "part" }
            ?.let(::repairExtension)

    /**
     * A file cached before the type was ever sniffed sits on disk under a name
     * with no extension, and would keep hitting the "can't be shown" state
     * forever. Re-identify it from its own bytes and rename in place — no second
     * download, and the next tap is already correct.
     */
    private fun repairExtension(file: File): File {
        if (DocumentTypeSniffer.hasUsableExtension(file.name)) return file
        val resolved = DocumentTypeSniffer.fileExtensionFromContents(file) ?: return file
        val repaired = File(file.parentFile, "${file.name}.$resolved")
        repaired.delete()
        return if (file.renameTo(repaired)) repaired else file
    }

    /**
     * Display name plus a resolved extension. Keeping the server name matters for
     * the preview title; the extension is what makes it render.
     */
    private fun fileName(
        preferredName: String,
        url: String,
        mimeType: String?,
        downloadedFile: File
    ): String {
        var base = preferredName.trim().replace('/', '_')
        if (base.isEmpty()) base = url.substringBefore('?').substringAfterLast('/')
        if (base.isEmpty()) base = "document"

        val resolved = DocumentTypeSniffer.fileExtension(
            name = preferredName,
            url = url,
            mimeType = mimeType,
            file = downloadedFile
        ) ?: return base

        return if (base.substringAfterLast('.', "").lowercase() == resolved) {
            base
        } else {
            "$base.$resolved"
        }
    }
}

class DocumentLoadException(message: String) : Exception(message)
