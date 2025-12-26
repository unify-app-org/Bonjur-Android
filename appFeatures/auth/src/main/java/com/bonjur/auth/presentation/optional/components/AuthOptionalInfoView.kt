package com.bonjur.auth.presentation.optional.components

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
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoAction
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoSideEffect
import com.bonjur.auth.presentation.optional.model.AuthOptionalInfoViewState
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.progress.AppProgressView
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun AuthOptionalInfoView(
    store: FeatureStore<AuthOptionalInfoViewState, AuthOptionalInfoAction, AuthOptionalInfoSideEffect>
) {
    val state = store.state

    val pagerState = rememberPagerState(
        initialPage = state.currentStep - 1,
        pageCount = { 3 }
    )

    LaunchedEffect(state.currentStep) {
        pagerState.animateScrollToPage(state.currentStep - 1)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { newPage ->
                store.send(AuthOptionalInfoAction.PageChanged(newPage + 1))
            }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopSection(
            currentStep = state.currentStep,
            onBack = { store.send(AuthOptionalInfoAction.Back) }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) { page ->
            when (page) {
                0 -> Text("Test")
                1 -> Text("Test2")
                2 -> Text("Test3")
            }
        }

        BottomSection(
            onNext = { store.send(AuthOptionalInfoAction.Next) }
        )
    }
}

@Composable
private fun TopSection(currentStep: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(44.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = Images.Icons.arrowLeft01(),
            contentDescription = null,
            modifier = Modifier
                .clickable{
                    onBack
                }
                .size(28.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        AppProgressView(
            currentStep = currentStep,
            totalSteps = 3,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BottomSection(onNext: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppButton(
            title = "Skip",
            model = AppButtonModel(type = ButtonType.Tertiary),
            onClick = { /* TODO: skip navigation */ }
        )
        AppButton(
            title = "Next",
            model = AppButtonModel(contentSize = ContentSize.Fill),
            onClick = onNext
        )
    }
}
