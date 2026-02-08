package com.bonjur.designSystem.components.attachments

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.bonjur.designSystem.ui.theme.image.Images
import java.util.UUID

data class AttachmentItemModel(
    val uuid: UUID = UUID.randomUUID(),
    val id: Int,
    val image: @Composable () -> Painter = { Images.Icons.fileAttachment() },
    val size: String,
    val name: String,
    val type: AttachmentType = AttachmentType.NONE,
    val canEdit: Boolean = false
) {
    enum class AttachmentType {
        PDF,
        IMAGE,
        XLSX,
        DOCX,
        NONE
    }
    
    companion object {
        @Composable
        fun previewMock(): List<AttachmentItemModel> = listOf(
            AttachmentItemModel(
                id = 1,
                image = { Images.Icons.fileAttachment() },
                name = "Career_fair_2025.pdf",
                size = "16 kb",
                canEdit = true
            ),
            AttachmentItemModel(
                id = 2,
                image = { Images.Icons.fileAttachment() },
                name = "Career_fair_2025.image",
                size = "14 mb",
                canEdit = true
            )
        )
    }
}