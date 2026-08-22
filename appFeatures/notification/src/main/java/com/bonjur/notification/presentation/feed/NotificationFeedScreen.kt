package com.bonjur.notification.presentation.feed

import androidx.compose.ui.res.stringResource
import com.bonjur.designsystem.R as DesignR
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.designSystem.components.topBar.AppTopBar
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.navigation.Navigator
import com.bonjur.notification.presentation.feed.components.NotificationFeedView
import com.bonjur.notification.presentation.feed.models.NotificationFeedAction
import com.bonjur.notification.presentation.feed.models.NotificationFeedSideEffect
import kotlinx.coroutines.launch

@Composable
fun NotificationFeedScreen(
    navigator: Navigator,
    viewModel: NotificationFeedViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.init(navigator) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            isScrolled = true,
            showTitle = true,
            title = stringResource(DesignR.string.notification),
            onBack = { scope.launch { navigator.navigateUp() } },
            trailing = {
                Text(
                    "✓✓",
                    style = AppTypography.BodyTextMd.semiBold,
                    color = Palette.green900,
                    modifier = Modifier.clickable { viewModel.store.send(NotificationFeedAction.MarkAllRead) }
                )
            }
        )
        FeatureScreen(
            viewModel = viewModel,
            handleEffect = { effect ->
                when (effect) {
                    is NotificationFeedSideEffect.Error -> AppSnackBar.showError(effect.message)
                }
            }
        ) { store ->
            NotificationFeedView(store = store)
        }
    }
}
