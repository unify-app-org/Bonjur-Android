package com.bonjur.auth.presentation.optional.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoStep
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.progress.AppProgressView
import com.bonjur.designSystem.ui.theme.image.Images
import java.time.LocalDate
import java.util.Calendar

@Composable
fun AuthOptionalInfoView(
    store: FeatureStore<AuthOptionalInfoViewState, AuthOptionalInfoAction, AuthOptionalInfoSideEffect>
) {
    val state = store.state
    val context = LocalContext.current

    val steps: List<AuthOptionalInfoStep> = listOf(
        AuthOptionalInfoStep(
            id = 0,
            content = { AuthOptionalBirthdayView(store) }
        ),
        AuthOptionalInfoStep(
            id = 1,
            content = { AuthOptionalSelectGenderView(store) }
        ),
        AuthOptionalInfoStep(
            id = 2,
            content = { AuthOptionalSelectLanguagesView(store) }
        ),
        AuthOptionalInfoStep(
            id = 2,
            content = { AuthOptionalBioView(store) }
        )
    )

    val pagerState = rememberPagerState(
        initialPage = state.currentStep - 1,
        pageCount = { steps.count() }
    )

    LaunchedEffect(state.showDatePicker) {
        if (state.showDatePicker) {
            val calendar = Calendar.getInstance()

            state.birthDate?.let { birthDate ->
                calendar.set(birthDate.year, birthDate.monthValue - 1, birthDate.dayOfMonth)
            }

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    store.send(AuthOptionalInfoAction.DateSelected(selectedDate))
                    store.send(AuthOptionalInfoAction.CloseDatePicker)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.setOnDismissListener {
                store.send(AuthOptionalInfoAction.CloseDatePicker)
            }

            datePickerDialog.show()
        }
    }

    LaunchedEffect(state.currentStep) {
        pagerState.animateScrollToPage(state.currentStep - 1)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { newPage ->
                store.send(AuthOptionalInfoAction.PageChanged(newPage + 1))
            }
    }

    LaunchedEffect(Unit) {
        store.send(AuthOptionalInfoAction.FetchData)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopSection(
            count = steps.count(),
            currentStep = state.currentStep,
            onBack = { store.send(AuthOptionalInfoAction.Back) }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) { page ->
            steps[page].content()
        }

        BottomSection(
            onSkip = { store.send(AuthOptionalInfoAction.Skip) },
            onNext = { store.send(AuthOptionalInfoAction.Next) }
        )
    }
}

@Composable
private fun TopSection(
    count: Int,
    currentStep: Int,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Icon(
            painter = Images.Icons.arrowLeft01(),
            contentDescription = "Back",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onBack() }
                .size(28.dp)
        )

        AppProgressView(
            currentStep = currentStep,
            totalSteps = count,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun BottomSection(
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppButton(
            title = "Skip",
            model = AppButtonModel(type = ButtonType.Tertiary),
            onClick = onSkip
        )
        AppButton(
            title = "Next",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = onNext
        )
    }
}