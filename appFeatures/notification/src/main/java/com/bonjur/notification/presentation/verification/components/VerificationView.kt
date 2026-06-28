package com.bonjur.notification.presentation.verification.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.AppButtonSize
import com.bonjur.designSystem.components.button.ButtonType
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.notification.domain.models.VerificationItem
import com.bonjur.notification.presentation.needsAction.components.Avatar
import com.bonjur.notification.presentation.needsAction.components.ComingSoon
import com.bonjur.notification.presentation.needsAction.components.ErrorState
import com.bonjur.notification.presentation.needsAction.components.LoadingState
import com.bonjur.notification.presentation.needsAction.models.RequestsPhase
import com.bonjur.notification.presentation.verification.models.VerificationAction
import com.bonjur.notification.presentation.verification.models.VerificationSideEffect
import com.bonjur.notification.presentation.verification.models.VerificationViewState

@Composable
fun VerificationView(store: FeatureStore<VerificationViewState, VerificationAction, VerificationSideEffect>) {
    val state = store.state
    LaunchedEffect(Unit) { store.send(VerificationAction.OnAppear) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.grayQuaternary.copy(alpha = 0.4f))
    ) {
        if (state.items.isNotEmpty()) {
            VerificationList(store, state)
        } else {
            when (state.phase) {
                RequestsPhase.IDLE, RequestsPhase.LOADING -> LoadingState()
                RequestsPhase.FAILED -> ErrorState { store.send(VerificationAction.Retry) }
                RequestsPhase.LOADED -> ComingSoon(
                    "Nothing to verify",
                    "All club verification requests are handled."
                )
            }
        }
    }
}

@Composable
private fun VerificationList(
    store: FeatureStore<VerificationViewState, VerificationAction, VerificationSideEffect>,
    state: VerificationViewState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.items, key = { it.id }) { item ->
            if (item.id == state.items.last().id && state.canLoadMore) {
                LaunchedEffect(item.id) { store.send(VerificationAction.LoadMore) }
            }
            VerificationRow(
                item = item,
                isProcessing = state.processingIds.contains(item.id),
                onTap = { store.send(VerificationAction.CellTapped(item)) },
                onVerify = { store.send(VerificationAction.Verify(item)) },
                onReject = { store.send(VerificationAction.Reject(item)) }
            )
        }
        if (state.isLoadingMore) {
            item {
                Box(Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun VerificationRow(
    item: VerificationItem,
    isProcessing: Boolean,
    onTap: () -> Unit,
    onVerify: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Palette.white)
            .border(1.dp, Palette.onBackground, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onTap),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Avatar(item.logoUrl)
            Column(modifier = Modifier.weight(1f)) {
                Text(item.clubName, style = AppTypography.BodyTextMd.semiBold, color = Palette.black)
                Text("Submitted by ${item.submitterName}", style = AppTypography.TextL.regular, color = Palette.graySecondary)
            }
        }
        if (isProcessing) {
            Box(Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppButton(
                    title = "Reject",
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    model = AppButtonModel(type = ButtonType.Destructive, contentSize = ContentSize.Fill, size = AppButtonSize.Small)
                )
                AppButton(
                    title = "Verify",
                    onClick = onVerify,
                    modifier = Modifier.weight(1f),
                    model = AppButtonModel(type = ButtonType.Primary, contentSize = ContentSize.Fill, size = AppButtonSize.Small)
                )
            }
        }
    }
}
