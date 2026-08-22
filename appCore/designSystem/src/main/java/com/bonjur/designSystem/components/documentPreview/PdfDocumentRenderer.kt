package com.bonjur.designSystem.components.documentPreview

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File

/**
 * Thin wrapper over [PdfRenderer]. The platform renderer only allows one open
 * page at a time, so every render goes through a mutex — pages can be requested
 * from any number of composables without stepping on each other.
 */
class PdfDocumentRenderer(file: File) : Closeable {

    private val descriptor: ParcelFileDescriptor =
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    private val renderer = PdfRenderer(descriptor)
    private val mutex = Mutex()

    val pageCount: Int get() = renderer.pageCount

    /** Renders page [index] scaled to [widthPx], or null if the page is unreadable. */
    suspend fun renderPage(index: Int, widthPx: Int): Bitmap? = mutex.withLock {
        withContext(Dispatchers.IO) {
            runCatching {
                val page = renderer.openPage(index)
                try {
                    val height = (widthPx.toFloat() / page.width * page.height)
                        .toInt()
                        .coerceAtLeast(1)
                    val bitmap = Bitmap.createBitmap(widthPx, height, Bitmap.Config.ARGB_8888)
                    // PDF pages are transparent where nothing is drawn; without a
                    // white base the text renders onto black in dark surfaces.
                    bitmap.eraseColor(Color.WHITE)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap
                } finally {
                    page.close()
                }
            }.getOrNull()
        }
    }

    override fun close() {
        runCatching { renderer.close() }
        runCatching { descriptor.close() }
    }
}
