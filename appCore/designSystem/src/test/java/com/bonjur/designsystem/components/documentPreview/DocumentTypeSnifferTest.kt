package com.bonjur.designSystem.components.documentPreview

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Attachments arrive with extension-less display names (`abbCardApproved`), so
 * the sniffer is what decides whether a document previews at all.
 *
 * `mimeType` stays null throughout: the MIME branch calls `android.webkit
 * .MimeTypeMap`, which isn't available to a plain JVM unit test.
 */
class DocumentTypeSnifferTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val url = "http://minio.local/unifies/9f2c1a"

    @Test
    fun `pdf magic bytes win over an extension-less name`() {
        val file = write("abbCardApproved", byteArrayOf(0x25, 0x50, 0x44, 0x46) + ByteArray(64))
        assertEquals("pdf", sniff("abbCardApproved", file))
    }

    @Test
    fun `png jpg gif bmp are recognised by header`() {
        assertEquals("png", sniff("a", write("a", bytes(0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A))))
        assertEquals("jpg", sniff("b", write("b", bytes(0xFF, 0xD8, 0xFF, 0xE0))))
        assertEquals("gif", sniff("c", write("c", bytes(0x47, 0x49, 0x46, 0x38, 0x39, 0x61))))
        assertEquals("bmp", sniff("d", write("d", bytes(0x42, 0x4D, 0x00, 0x00))))
    }

    @Test
    fun `webp needs the RIFF container plus the WEBP tag`() {
        val webp = bytes(0x52, 0x49, 0x46, 0x46, 0, 0, 0, 0) + "WEBP".toByteArray() + ByteArray(8)
        assertEquals("webp", sniff("e", write("e", webp)))
    }

    @Test
    fun `office containers are told apart by their entry names`() {
        assertEquals("docx", sniff("contract", zip("contract", "word/document.xml")))
        assertEquals("xlsx", sniff("sheet", zip("sheet", "xl/workbook.xml")))
        assertEquals("pptx", sniff("deck", zip("deck", "ppt/presentation.xml")))
        assertEquals("zip", sniff("bundle", zip("bundle", "assets/readme.md")))
    }

    @Test
    fun `bytes beat a name that merely ends in something extension-shaped`() {
        val file = write("report", byteArrayOf(0x25, 0x50, 0x44, 0x46) + ByteArray(64))
        assertEquals("pdf", sniff("report.2026.final", file))
    }

    @Test
    fun `a name extension is the fallback for formats with no magic number`() {
        val file = write("notes", "id,name\n1,alpha\n".toByteArray())
        assertEquals("csv", sniff("notes.csv", file))
    }

    @Test
    fun `an unrecognisable file resolves to no extension`() {
        val file = write("mystery", bytes(0x07, 0x08, 0x09, 0x0A) + ByteArray(32))
        assertEquals(null, sniff("mystery", file))
    }

    private fun sniff(name: String, file: File): String? =
        DocumentTypeSniffer.fileExtension(name = name, url = url, mimeType = null, file = file)

    private fun bytes(vararg values: Int): ByteArray =
        ByteArray(values.size) { values[it].toByte() }

    private fun write(name: String, content: ByteArray): File =
        folder.newFile(name).apply { writeBytes(content) }

    private fun zip(name: String, entry: String): File =
        folder.newFile(name).apply {
            ZipOutputStream(outputStream()).use { out ->
                out.putNextEntry(ZipEntry("[Content_Types].xml"))
                out.write("<Types/>".toByteArray())
                out.closeEntry()
                out.putNextEntry(ZipEntry(entry))
                out.write("<node/>".repeat(200).toByteArray())
                out.closeEntry()
            }
        }
}
