package com.bonjur.auth.presentation.chooseUniversity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bonjur.auth.R
import com.bonjur.appfoundation.FeatureStore
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityAction
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversitySideEffect
import com.bonjur.auth.presentation.chooseUniversity.model.ChooseUniversityViewState
import com.bonjur.auth.presentation.chooseUniversity.model.CommunitiesPhase
import com.bonjur.designSystem.components.button.AppButton
import com.bonjur.designSystem.components.button.AppButtonModel
import com.bonjur.designSystem.components.button.ContentSize
import com.bonjur.designSystem.components.emptyView.AppEmptyModel
import com.bonjur.designSystem.components.emptyView.AppEmptyView
import com.bonjur.designSystem.components.selectableList.SelectableListItem
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun ChooseUniversityView(
    store: FeatureStore<ChooseUniversityViewState, ChooseUniversityAction, ChooseUniversitySideEffect>
) {
    val state = store.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // Top View
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = stringResource(R.string.auth_choose_university_title),
                style = AppTypography.TitleXL.extraBold,
                color = Palette.blackHigh
            )
            Text(
                text = stringResource(R.string.auth_choose_university_subtitle),
                style = AppTypography.BodyTextMd.regular,
                color = Palette.grayPrimary
            )
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            if (state.uiModel.isNotEmpty()) {
                // List View
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(state.uiModel) { index, university ->
                        SelectableListItem(
                            model = university,
                            onClick = {
                                store.send(ChooseUniversityAction.SelectedCell(index))
                            }
                        )
                    }
                }
            } else {
                // The list is never legitimately empty: a blank screen here means the
                // request failed (or the backend has no communities yet), so say so and
                // offer a retry instead of leaving the user on a dead screen.
                when (state.phase) {
                    CommunitiesPhase.LOADING -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = Palette.appBlue) }

                    CommunitiesPhase.FAILED -> CommunitiesMessage(
                        text = stringResource(R.string.auth_communities_error),
                        onRetry = { store.send(ChooseUniversityAction.FetchData) }
                    )

                    CommunitiesPhase.LOADED -> CommunitiesMessage(
                        text = stringResource(R.string.auth_communities_empty),
                        onRetry = { store.send(ChooseUniversityAction.FetchData) }
                    )
                }
            }
        }

        TermsAgreement(
            accepted = state.termsAccepted,
            onToggle = { store.send(ChooseUniversityAction.TermsToggled) },
            onOpenTerms = { store.send(ChooseUniversityAction.TermsTapped) }
        )

        AppButton(
            title = stringResource(R.string.auth_next),
            model = AppButtonModel(
                contentSize = ContentSize.Fill
            ),
            onClick = {
                store.send(ChooseUniversityAction.NextTapped)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.enabled && state.termsAccepted && state.uiModel.isNotEmpty()
        )
    }
}

/** Checkbox toggles acceptance; only the underlined title opens the document. Mirrors iOS. */
@Composable
private fun TermsAgreement(
    accepted: Boolean,
    onToggle: () -> Unit,
    onOpenTerms: () -> Unit
) {
    val linkTitle = stringResource(R.string.auth_terms_link)
    val sentence = stringResource(R.string.auth_terms_agree, linkTitle)
    val text = buildAnnotatedString {
        val start = sentence.indexOf(linkTitle)
        if (start < 0) {
            append(sentence)
            return@buildAnnotatedString
        }
        append(sentence.substring(0, start))
        withLink(
            LinkAnnotation.Clickable(
                tag = "terms",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Palette.appBlue,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = { onOpenTerms() }
            )
        ) { append(linkTitle) }
        append(sentence.substring(start + linkTitle.length))
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = if (accepted) Images.Icons.selectedCheckBox() else Images.Icons.notSelectedCheckBox(),
            contentDescription = linkTitle,
            modifier = Modifier
                .size(22.dp)
                .toggleable(value = accepted, role = Role.Checkbox, onValueChange = { onToggle() })
        )
        Text(
            text = text,
            style = AppTypography.TextMd.regular,
            color = Palette.blackHigh,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CommunitiesMessage(text: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppEmptyView(
            model = AppEmptyModel(
                icon = Images.Icons.twoUsers(),
                text = text
            )
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.auth_try_again),
            style = AppTypography.BodyTextMd.semiBold,
            color = Palette.appBlue,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable(onClick = onRetry)
        )
    }
}
