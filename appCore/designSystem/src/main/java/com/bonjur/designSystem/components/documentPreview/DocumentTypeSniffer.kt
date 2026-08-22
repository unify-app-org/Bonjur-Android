package com.bonjur.designSystem.components.documentPreview

import android.webkit.MimeTypeMap
import java.io.File

/**
 * Works out a document's file extension. Attachments come back from the API with
 * a display name that often has **no extension** (e.g. `abbCardApproved`) and a
 * URL that has none either, and the preview picks its renderer off the
 * extension — so we fall back to the response MIME type and finally to the
 * file's magic bytes.
 *
 * Mirrors the iOS `DocumentTypeSniffer`.
 */
object DocumentTypeSniffer {

    /** Office containers put their marker entries early; 1 MB is plenty. */
    private const val OFFICE_SCAN_LIMIT = 1_048_576

    /**
     * Extension without the dot, or null when nothing recognises the file.
     *
     * The bytes are asked first on purpose: a name like `report.2026.final` ends
     * in something that *looks* like an extension but isn't, while the header of
     * a PDF is never wrong. The name is the last resort, and covers the text
     * formats that have no magic number (txt/csv/json).
     */
    fun fileExtension(
        name: String,
        url: String,
        mimeType: String?,
        file: File?
    ): String? {
        file?.let { extensionFromMagicBytes(it) }?.let { return it }
        extensionFromMimeType(mimeType)?.let { return it }
        usableExtension(name.substringAfterLast('.', ""))?.let { return it }
        usableExtension(url.substringBefore('?').substringAfterLast('.', ""))?.let { return it }
        // Dead last: a text payload has no signature, so "this decodes as text"
        // is a guess. It must not outrank a specific name like `notes.csv`.
        return file?.let { textExtension(it) }
    }

    /**
     * Type resolved from the file's own bytes alone. Used to re-identify a
     * document that was already cached under a name with no extension.
     */
    fun fileExtensionFromContents(file: File): String? =
        extensionFromMagicBytes(file) ?: textExtension(file)

    /** Whether a name already carries an extension worth trusting. */
    fun hasUsableExtension(name: String): Boolean =
        usableExtension(name.substringAfterLast('.', "")) != null

    /**
     * Rejects junk that can't be an extension at all — punctuation, digits only,
     * or a segment far too long to be one.
     */
    private fun usableExtension(candidate: String): String? {
        val value = candidate.lowercase()
        if (value.isEmpty() || value.length > 5) return null
        if (!value.all { it.isLetterOrDigit() }) return null
        if (value.none { it.isLetter() }) return null
        return value
    }

    private fun extensionFromMimeType(mimeType: String?): String? {
        val cleaned = mimeType?.substringBefore(';')?.trim()?.lowercase().orEmpty()
        // MinIO serves anything it can't classify as octet-stream, which tells us
        // nothing — only the magic bytes can.
        if (cleaned.isEmpty() || cleaned == "application/octet-stream") return null
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(cleaned)
    }

    private fun extensionFromMagicBytes(file: File): String? {
        val header = ByteArray(16)
        val read = runCatching {
            file.inputStream().use { it.read(header) }
        }.getOrDefault(-1)
        if (read < 4) return null

        fun startsWith(vararg expected: Int): Boolean =
            expected.withIndex().all { (index, value) ->
                index < read && (header[index].toInt() and 0xFF) == value
            }

        return when {
            startsWith(0x25, 0x50, 0x44, 0x46) -> "pdf"                       // %PDF
            startsWith(0x89, 0x50, 0x4E, 0x47) -> "png"
            startsWith(0xFF, 0xD8, 0xFF) -> "jpg"
            startsWith(0x47, 0x49, 0x46, 0x38) -> "gif"                       // GIF8
            startsWith(0x42, 0x4D) -> "bmp"
            startsWith(0xD0, 0xCF, 0x11, 0xE0) -> "doc"                       // legacy OLE
            startsWith(0x52, 0x49, 0x46, 0x46) && read >= 12 &&
                header.copyOfRange(8, 12).toString(Charsets.ISO_8859_1) == "WEBP" -> "webp"
            startsWith(0x7B, 0x5C, 0x72, 0x74, 0x66) -> "rtf"                 // {\rtf
            startsWith(0x37, 0x7A, 0xBC, 0xAF) -> "7z"
            startsWith(0x52, 0x61, 0x72, 0x21) -> "rar"
            startsWith(0x50, 0x4B, 0x03, 0x04) -> officeExtension(file)
            // ISO base media: the brand at offset 8 says heic / avif / mp4 / mov.
            read >= 12 && header.copyOfRange(4, 8).toString(Charsets.ISO_8859_1) == "ftyp" ->
                isoBaseMediaExtension(header.copyOfRange(8, 12).toString(Charsets.ISO_8859_1))
            else -> null
        }
    }

    private fun isoBaseMediaExtension(brand: String): String = when (brand.lowercase()) {
        "heic", "heix", "hevc", "heim", "heis" -> "heic"
        "mif1", "msf1" -> "heif"
        "avif", "avis" -> "avif"
        "qt  " -> "mov"
        else -> "mp4"
    }

    /**
     * Sniffs plain-text payloads (XML/SVG/HTML/JSON/CSV/log). A file is text when
     * a leading sample decodes as UTF-8 and holds no NUL bytes.
     */
    private fun textExtension(file: File): String? {
        val sample = runCatching {
            file.inputStream().use { stream ->
                val buffer = ByteArray(minOf(file.length(), 2048L).toInt())
                val read = stream.read(buffer)
                if (read <= 0) null else buffer.copyOf(read)
            }
        }.getOrNull() ?: return null

        if (sample.any { it.toInt() == 0 }) return null
        val text = runCatching {
            val decoded = String(sample, Charsets.UTF_8)
            // A lossy decode leaves replacement chars; that means it isn't text.
            if (decoded.contains('\uFFFD')) null else decoded
        }.getOrNull() ?: return null

        val head = text.trim().take(512).lowercase()
        return when {
            head.startsWith("<svg") || head.contains("<svg ") -> "svg"
            head.startsWith("<!doctype html") || head.startsWith("<html") -> "html"
            head.startsWith("<?xml") -> "xml"
            head.startsWith("{") || head.startsWith("[") -> "json"
            else -> "txt"
        }
    }

    /**
     * docx/xlsx/pptx are all zips; the entry names say which. Zip stores every
     * entry name uncompressed in its local header, so scanning the raw bytes
     * finds them without unpacking anything.
     */
    private fun officeExtension(file: File): String {
        val text = runCatching {
            file.inputStream().use { stream ->
                val buffer = ByteArray(minOf(file.length(), OFFICE_SCAN_LIMIT.toLong()).toInt())
                val read = stream.read(buffer)
                if (read <= 0) "" else String(buffer, 0, read, Charsets.ISO_8859_1)
            }
        }.getOrDefault("")

        return when {
            text.contains("word/") -> "docx"
            text.contains("xl/") -> "xlsx"
            text.contains("ppt/") -> "pptx"
            else -> "zip"
        }
    }
}
