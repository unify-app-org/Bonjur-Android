package com.bonjur.designSystem.components.documentPreview

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.designsystem.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private sealed interface DocumentLoadState {
    data object Loading : DocumentLoadState
    data class Ready(val file: File) : DocumentLoadState
    data object Failed : DocumentLoadState
}

/**
 * Full-screen in-app document preview: downloads the file first, then renders
 * the local copy. PDFs, images and plain text render inline; anything the
 * platform can't draw (Office formats) offers a hand-off to an external app.
 *
 * Mirrors the iOS `DocumentPreviewView`, which gets the same states out of
 * QuickLook.
 */
@Composable
fun DocumentPreviewDialog(
    url: String,
    name: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var state by remember(url) { mutableStateOf<DocumentLoadState>(DocumentLoadState.Loading) }
    var attempt by remember(url) { mutableIntStateOf(0) }

    LaunchedEffect(url, attempt) {
        state = DocumentLoadState.Loading
        state = runCatching { DocumentFileLoader.localFile(context, url, name) }
            .fold(
                onSuccess = { DocumentLoadState.Ready(it) },
                onFailure = { DocumentLoadState.Failed }
            )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Palette.white)
        ) {
            PreviewHeader(
                name = name,
                onDismiss = onDismiss,
                // Sharing hands over the downloaded copy, so it only makes sense
                // once there is one.
                onShare = (state as? DocumentLoadState.Ready)?.let { ready ->
                    { shareDocument(context, ready.file) }
                }
            )
            HorizontalDivider(color = Palette.grayTeritary, thickness = 0.5.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (val current = state) {
                    DocumentLoadState.Loading -> LoadingState()
                    DocumentLoadState.Failed -> FailedState(onRetry = { attempt++ })
                    is DocumentLoadState.Ready -> DocumentBody(
                        file = current.file,
                        onOpenExternally = { openExternally(context, current.file) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviewHeader(
    name: String,
    onDismiss: () -> Unit,
    onShare: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = Palette.grayQuaternary,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                painter = Images.Icons.xmark(),
                contentDescription = LanguageManager.string(R.string.common_close),
                tint = Palette.blackHigh,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = name,
            style = AppTypography.BodyTextSm.medium,
            color = Palette.blackHigh,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        if (onShare != null) {
            IconButton(
                onClick = onShare,
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = Palette.grayQuaternary,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    painter = Images.Icons.share(),
                    contentDescription = LanguageManager.string(R.string.document_preview_share),
                    tint = Palette.blackHigh,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(color = Palette.blackHigh)
        Text(
            text = LanguageManager.string(R.string.document_preview_loading),
            style = AppTypography.TextMd.regular,
            color = Palette.blackMedium
        )
    }
}

@Composable
private fun FailedState(onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        Text(
            text = LanguageManager.string(R.string.document_preview_failed),
            style = AppTypography.BodyTextSm.medium,
            color = Palette.blackHigh,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onRetry) {
            Text(
                text = LanguageManager.string(R.string.document_preview_retry),
                style = AppTypography.BodyTextSm.medium,
                color = Palette.blackHigh
            )
        }
    }
}

@Composable
private fun DocumentBody(
    file: File,
    onOpenExternally: () -> Unit
) {
    when (extensionOf(file)) {
        "pdf" -> PdfBody(file)
        in IMAGE_EXTENSIONS -> ImageBody(file)
        in TEXT_EXTENSIONS -> TextBody(file)
        else -> UnsupportedBody(file, onOpenExternally)
    }
}

@Composable
private fun PdfBody(file: File) {
    val renderer = remember(file.path) { runCatching { PdfDocumentRenderer(file) }.getOrNull() }
    DisposableEffect(renderer) { onDispose { renderer?.close() } }

    if (renderer == null || renderer.pageCount == 0) {
        FailedState(onRetry = {})
        return
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val widthPx = with(LocalDensity.current) { maxWidth.toPx() }.toInt().coerceAtLeast(1)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Palette.grayQuaternary),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items((0 until renderer.pageCount).toList()) { index ->
                PdfPage(renderer = renderer, index = index, widthPx = widthPx)
            }
        }
    }
}

@Composable
private fun PdfPage(renderer: PdfDocumentRenderer, index: Int, widthPx: Int) {
    // Pages render as they scroll into view; a long document never rasterises
    // every page up front.
    val bitmap by produceState<android.graphics.Bitmap?>(null, index, widthPx) {
        value = renderer.renderPage(index, widthPx)
    }

    val current = bitmap
    if (current == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .background(Palette.white),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Palette.blackMedium)
        }
    } else {
        Image(
            bitmap = current.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
private fun ImageBody(file: File) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    AsyncImage(
        model = file,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary)
            .pointerInput(file.path) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 5f)
                    if (scale > 1f) {
                        offsetX += pan.x
                        offsetY += pan.y
                    } else {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
    )
}

@Composable
private fun TextBody(file: File) {
    val content by produceState("", file.path) {
        value = withContext(Dispatchers.IO) {
            runCatching { file.readText().take(MAX_TEXT_CHARS) }.getOrDefault("")
        }
    }
    Text(
        text = content,
        style = AppTypography.TextMd.regular,
        color = Palette.blackHigh,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    )
}

@Composable
private fun UnsupportedBody(file: File, onOpenExternally: () -> Unit) {
    // Nothing renders this one. Log what it actually is, so a report about a
    // still-broken document says something more useful than "can't be shown".
    LaunchedEffect(file.path) {
        Log.w(
            "DocumentPreview",
            "Unsupported document: name=${file.name} ext=${file.extension} " +
                "size=${file.length()} head=${file.headHex()}"
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        Text(
            text = LanguageManager.string(R.string.document_preview_unsupported),
            style = AppTypography.BodyTextSm.medium,
            color = Palette.blackHigh,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onOpenExternally) {
            Text(
                text = LanguageManager.string(R.string.document_preview_open_external),
                style = AppTypography.BodyTextSm.medium,
                color = Palette.blackHigh
            )
        }
    }
}

private const val MAX_TEXT_CHARS = 200_000

private val IMAGE_EXTENSIONS =
    setOf("png", "jpg", "jpeg", "gif", "webp", "bmp", "heic", "heif", "avif")
private val TEXT_EXTENSIONS =
    setOf("txt", "csv", "json", "md", "log", "xml", "html", "svg", "yaml", "yml")

/**
 * The cached file already carries the extension [DocumentFileLoader] resolved
 * (display name → URL → MIME type → magic bytes), so it is the authority here —
 * the API's display name is frequently extension-less.
 */
private fun extensionOf(file: File): String = file.extension.lowercase()

/**
 * Hands the already-downloaded copy to another app. Only used for formats the
 * platform can't draw itself, so the user still gets to the content.
 */
private fun openExternally(context: Context, file: File) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(contentUri(context, file), mimeTypeOf(file))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startOrWarn(intent)
}

/** Shares the downloaded copy — the same file the preview is showing. */
private fun shareDocument(context: Context, file: File) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = mimeTypeOf(file)
        putExtra(Intent.EXTRA_STREAM, contentUri(context, file))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startOrWarn(
        Intent.createChooser(
            intent,
            LanguageManager.string(R.string.document_preview_share)
        ).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
    )
}

/**
 * A `file://` URI would crash the receiving app on API 24+, so cached documents
 * always leave the process through the FileProvider declared in this library's
 * manifest.
 */
private fun contentUri(context: Context, file: File) = FileProvider.getUriForFile(
    context,
    "${context.packageName}.documentprovider",
    file
)

private fun mimeTypeOf(file: File): String =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
        ?: "*/*"

private fun Context.startOrWarn(intent: Intent) {
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        AppSnackBar.show(
            title = LanguageManager.string(R.string.document_preview_failed),
            style = AppSnackBar.Style.ERROR
        )
    }
}

/** First bytes as hex, for diagnosing a document nothing could identify. */
private fun File.headHex(): String = runCatching {
    inputStream().use { stream ->
        val buffer = ByteArray(16)
        val read = stream.read(buffer)
        if (read <= 0) "" else buffer.copyOf(read).joinToString(" ") { "%02x".format(it) }
    }
}.getOrDefault("unreadable")
