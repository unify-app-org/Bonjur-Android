package com.bonjur.auth.presentation.optional.components

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import androidx.compose.runtime.getValue
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun AuthOptionalUploadPhotoView(
    store: FeatureStore<AuthOptionalInfoViewState, AuthOptionalInfoAction, AuthOptionalInfoSideEffect>
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
        }
    }

    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            val imageData = convertUriToByteArray(context, uri)
            imageData?.let {
                store.send(AuthOptionalInfoAction.SelectImage(imageData))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        TopView()
        PhotoUploadView(
            selectedImageData = store.state.selectedImage,
            onPhotoPickerClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        )
    }
}

fun convertUriToByteArray(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { it.readBytes() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Composable
private fun TopView() {
    Column(
        verticalArrangement = Arrangement
            .spacedBy(16.dp)
    ) {
        Text(
            text = "Upload your photos",
            style = AppTypography.TitleXL.extraBold,
            color = Palette.black,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Upload 1 images to get started",
            style = AppTypography.BodyTextMd.regular,
            color = Palette.grayPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PhotoUploadView(
    selectedImageData: ByteArray?,
    onPhotoPickerClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val width = screenWidth / 1.5f
    val height = screenHeight / 3f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp,
                    color = Palette.grayTeritary,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = if (selectedImageData != null) Alignment.BottomCenter else Alignment.Center
        ) {
            SelectedImage(
                imageData = selectedImageData,
                width = width,
                height = height
            )

            ChangeOrAddPhotoButton(
                hasImage = selectedImageData != null,
                onClick = onPhotoPickerClick
            )
        }
    }
}

@Composable
private fun SelectedImage(
    imageData: ByteArray?,
    width: Dp,
    height: Dp
) {
    imageData?.let { data ->
        val bitmap = remember(data) {
            BitmapFactory.decodeByteArray(data, 0, data.size)
        }
        
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Selected photo",
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ChangeOrAddPhotoButton(
    hasImage: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    ) {
        if (!hasImage) {
            Image(
                painter = Images.Icons.emojiLaugh(),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Row (
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = if (hasImage) 16.dp else 0.dp)
                .background(
                    color = Palette.greenLight,
                    shape = CircleShape
                )
                .border(
                    width = 0.5.dp,
                    color = Palette.grayTeritary,
                    shape = CircleShape
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Image(
                painter = if (hasImage) Images.Icons.camera() else Images.Icons.plus(),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = if (hasImage) "Change" else "Add",
                color = Palette.black,
                style = AppTypography.BodyTextMd.regular
            )
        }
    }
}