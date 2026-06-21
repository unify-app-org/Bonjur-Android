package com.bonjur.profile.presentation.editProfile.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import coil.compose.AsyncImage
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.cashedImage.CachedAsyncImage
import com.bonjur.designSystem.components.categorieChips.SelectCategoryView
import com.bonjur.designSystem.components.radioSelect.RadioSelectItem
import com.bonjur.designSystem.components.selectableList.SelectableListItem
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.designSystem.components.textField.AppTextField
import com.bonjur.designSystem.components.textField.AppTextFieldModel
import com.bonjur.designSystem.components.textView.TextView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.profile.presentation.studentCard.components.StudentCardCoverPickerSheet
import com.bonjur.profile.presentation.editProfile.models.EditProfileAction
import com.bonjur.profile.presentation.editProfile.models.EditProfileSideEffect
import com.bonjur.profile.presentation.editProfile.models.EditProfileViewState
import com.bonjur.profile.presentation.editProfile.models.Gender

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
        AppTopBar(
            isScrolled = false,
            showTitle = true,
            title = "Edit profile",
            onBack = { store.send(EditProfileAction.BackTapped) }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

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
                    val pickedUri = store.state.selectedImageUri
                    if (pickedUri != null) {
                        // CachedAsyncImage is HTTP-only; use Coil for the picked content:// uri.
                        AsyncImage(
                            model = pickedUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
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

                // Categories (chips + Add)
                val selectedCategories = store.state.categorySections
                    .flatMap { it.categories }
                    .filter { it.selected }
                    .map { it.id to it.title }
                ChipsSelectionField(
                    title = "Category",
                    addTitle = "Add category",
                    chips = selectedCategories,
                    onAdd = { store.send(EditProfileAction.AddCategoryTapped) },
                    onRemove = { id -> store.send(EditProfileAction.CategoryToggled(id)) }
                )

                // Languages (chips + Add)
                val selectedLanguages = store.state.languageOptions
                    .filter { it.selected }
                    .map { it.id to it.title }
                ChipsSelectionField(
                    title = "Spoken languages",
                    addTitle = "Add language",
                    chips = selectedLanguages,
                    onAdd = { store.send(EditProfileAction.AddLanguageTapped) },
                    onRemove = { id -> store.send(EditProfileAction.LanguageToggled(id)) }
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
                                val sdf = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
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

    // Category picker sheet
    if (store.state.showCategoryPicker) {
        ModalBottomSheet(
            onDismissRequest = { store.send(EditProfileAction.DismissCategoryPicker) },
            containerColor = Palette.white
        ) {
            SelectCategoryView(
                sections = store.state.categorySections,
                onToggle = { store.send(EditProfileAction.CategoryToggled(it)) },
                onDone = { store.send(EditProfileAction.CategoryPickerDone) },
                onClose = { store.send(EditProfileAction.DismissCategoryPicker) }
            )
        }
    }

    // Language picker sheet
    if (store.state.showLanguagePicker) {
        ModalBottomSheet(
            onDismissRequest = { store.send(EditProfileAction.DismissLanguagePicker) },
            containerColor = Palette.white
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 25.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select languages",
                    style = AppTypography.TitleMd.extraBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                store.state.languageOptions.forEach { lang ->
                    SelectableListItem(
                        model = lang,
                        onClick = { store.send(EditProfileAction.LanguageToggled(lang.id)) }
                    )
                }
                AppButton(
                    title = "Done",
                    model = AppButtonModel(contentSize = ContentSize.Fill),
                    onClick = { store.send(EditProfileAction.LanguagePickerDone) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}

@Composable
private fun ChipsSelectionField(
    title: String,
    addTitle: String,
    chips: List<Pair<Int, String>>,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = title, style = AppTypography.HeadingMd.medium)

        if (chips.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chips.forEach { (id, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .background(Palette.greenLight, CircleShape)
                            .border(1.dp, Palette.secondary, CircleShape)
                            .clickable { onRemove(id) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = AppTypography.TextMd.regular,
                            color = Palette.green900
                        )
                        Icon(
                            painter = Images.Icons.xmark(),
                            contentDescription = "Remove",
                            tint = Palette.green900,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, Palette.graySecondary, CircleShape)
                .clickable { onAdd() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = addTitle,
                style = AppTypography.TextMd.regular,
                color = Palette.blackMedium
            )
            Icon(
                painter = Images.Icons.plus(),
                contentDescription = null,
                tint = Palette.blackMedium,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun GenderPicker(
    selectedGender: Gender,
    onGenderSelected: (Gender) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Choose gender",
            style = AppTypography.HeadingMd.medium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // iOS exposes only Male/Female.
            listOf(Gender.MALE, Gender.FEMALE).forEach { gender ->
                RadioSelectItem(
                    id = gender.name,
                    title = gender.displayName,
                    isSelected = selectedGender == gender,
                    onClick = { onGenderSelected(gender) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
