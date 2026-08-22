package com.bonjur.auth.presentation.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.tabView.AppTabView
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import com.bonjur.designSystem.components.button.ButtonType
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.width
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.bonjur.auth.presentation.onboarding.model.OnboardingAction
import com.bonjur.auth.presentation.onboarding.model.OnboardingSideEffect
import com.bonjur.auth.R
import com.bonjur.auth.presentation.onboarding.model.OnboardingViewState
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designSystem.localization.LanguageSelectionView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingView(
    store: FeatureStore<OnboardingViewState, OnboardingAction, OnboardingSideEffect>
) {
    val state = store.state
    var currentPage by remember { mutableStateOf(0) }
    var showLanguagePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        currentPage = 0
        store.send(OnboardingAction.FetchData)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(44.dp)
        ) {
            // Back (when past slide 1) on the left, language flag on the right —
            // language is switchable before sign-in, mirroring iOS.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentPage > 0) {
                    Image(
                        painter = Images.Icons.arrowLeft01(),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                currentPage -= 1
                            }
                            .size(width = 28.dp, height = 28.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = LanguageManager.language.flag,
                    fontSize = 26.sp,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { showLanguagePicker = true }
                )
            }

            // No logo here — iOS onboarding shows only the back arrow, the
            // language flag and the slide artwork.
        }

        AppTabView(
            currentPage = currentPage,
            pageCount = state.uiModel.size,
            onPageChange = { newPage ->
                currentPage = newPage
            },
            modifier = Modifier.weight(1f)
        ) { page ->
            val item = state.uiModel[page]
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = item.titleArg
                            ?.let { stringResource(item.titleRes, it) }
                            ?: stringResource(item.titleRes),
                        style = AppTypography.TitleXL.extraBold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = stringResource(item.subtitleRes),
                        style = AppTypography.BodyTextMd.regular,
                        color = Palette.grayPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Image(
                    painter = item.image(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppButton(
                title = stringResource(R.string.auth_skip),
                model = AppButtonModel(type = ButtonType.Tertiary),
                onClick = {
                    store.send(OnboardingAction.SkipClicked)
                }
            )
            AppButton(
                title = stringResource(R.string.auth_next),
                model = AppButtonModel(contentSize = ContentSize.Fill),
                onClick = {
                    currentPage += 1

                    if (currentPage >= store.state.uiModel.count()) {
                        store.send(OnboardingAction.SkipClicked)
                    }
                }
            )
        }
    }

    if (showLanguagePicker) {
        AppBottomSheet(onDismiss = { showLanguagePicker = false }) {
            LanguageSelectionView { language ->
                LanguageManager.select(language)
                showLanguagePicker = false
            }
        }
    }
}
