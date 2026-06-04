package com.bonjur.profile.presentation.editProfile.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.textField.AppTextField
import com.bonjur.designSystem.components.textField.AppTextFieldModel
import com.bonjur.designSystem.components.textView.TextView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.profile.presentation.editProfile.models.EditProfileAction
import com.bonjur.profile.presentation.editProfile.models.EditProfileSideEffect
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState
import com.bonjur.profile.presentation.editProfile.models.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileView(
    store: FeatureStore<EditProfileViewState, EditProfileAction, EditProfileSideEffect>
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { store.send(EditProfileAction.ImageSelected(it)) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(96.dp)
                    .clickable { imagePickerLauncher.launch("image/*") }
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Palette.grayQuaternary, CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CachedAsyncImage(
                        url = store.state.avatarUrl,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = {
                            Icon(
                                painter = Images.Icons.user(),
                                contentDescription = null,
                                tint = Palette.blackMedium,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Palette.grayQuaternary, CircleShape)
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = Images.Icons.camera(),
                        contentDescription = "Change photo",
                        tint = Palette.blackMedium,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Static read-only fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppTextField(
                    text = store.state.name,
                    onTextChange = {},
                    model = AppTextFieldModel(title = "Name and surname"),
                    enabled = false
                )
                AppTextField(
                    text = store.state.faculty,
                    onTextChange = {},
                    model = AppTextFieldModel(title = "Faculty"),
                    enabled = false
                )
                AppTextField(
                    text = store.state.community,
                    onTextChange = {},
                    model = AppTextFieldModel(title = "University"),
                    enabled = false
                )
                AppTextField(
                    text = store.state.entry,
                    onTextChange = {},
                    model = AppTextFieldModel(title = "Entry"),
                    enabled = false
                )
                AppTextField(
                    text = store.state.course,
                    onTextChange = {},
                    model = AppTextFieldModel(title = "Course"),
                    enabled = false
                )
            }

            // Editable fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "About", style = AppTypography.HeadingMd.medium)
                    TextView(
                        text = store.state.about,
                        onTextChange = { store.send(EditProfileAction.AboutChanged(it)) },
                        characterLimit = 150,
                        placeholder = "About",
                        modifier = Modifier.height(120.dp)
                    )
                }

                // Gender picker
                GenderPicker(
                    selectedGender = store.state.selectedGender,
                    onGenderSelected = { store.send(EditProfileAction.GenderSelected(it)) }
                )

                // Birthday
                AppTextField(
                    text = store.state.birthDateText,
                    onTextChange = {},
                    placeHolder = "Select birthday",
                    readOnly = true,
                    model = AppTextFieldModel(title = "Birthday"),
                    trailingIcon = {
                        Icon(
                            painter = Images.Icons.calendar(),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                store.send(EditProfileAction.BirthdayTapped)
                            }
                        )
                    }
                )

                if (store.state.showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { store.send(EditProfileAction.CloseDatePicker) },
                        confirmButton = {
                            TextButton(onClick = { store.send(EditProfileAction.CloseDatePicker) }) {
                                Text("OK")
                            }
                        }
                    ) {
                        val datePickerState = rememberDatePickerState()
                        DatePicker(state = datePickerState)
                        LaunchedEffect(datePickerState.selectedDateMillis) {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val sdf = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault())
                                val dateStr = sdf.format(java.util.Date(millis))
                                store.send(EditProfileAction.BirthDateChanged(dateStr))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        AppButton(
            title = "Save",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = { store.send(EditProfileAction.SaveTapped) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun GenderPicker(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Choose gender",
            style = AppTypography.HeadingMd.medium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Gender.entries.forEach { gender ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { onGenderSelected(gender) }
                    )
                    Text(
                        text = gender.displayName,
                        style = AppTypography.TextMd.regular
                    )
                }
            }
        }
    }
}
