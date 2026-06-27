package com.bonjur.hangouts.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.components.alert.AppAlert
import com.bonjur.designSystem.components.alert.AppAlertPresenter
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.hangouts.domain.useCase.HangoutsUseCase
import com.bonjur.hangouts.navigation.HangoutsScreens
import com.bonjur.hangouts.presentation.create.models.HangoutCreateInputData
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsAction
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsSideEffect
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsViewState
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.member.list.MemberListInputData
import com.bonjur.member.list.MemberListScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.ProfileDetailNavArgs
import com.bonjur.navigation.SharedRoutes
import com.bonjur.navigation.route
import com.bonjur.network.manager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HangoutDetailsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<HangoutDetailsViewState, HangoutDetailsAction, HangoutDetailsSideEffect>(
    HangoutDetailsViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: HangoutsUseCase,
        val tokenManager: TokenManager
    )

    private lateinit var inputData: HangoutDetailsInputData
    private lateinit var navigator: Navigator

    fun init(inputData: HangoutDetailsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(currentUserId = dependencies.tokenManager.getUserId()))
        fetchData()
    }

    override fun handle(action: HangoutDetailsAction) {
        when (action) {
            HangoutDetailsAction.FetchData -> fetchData()
            HangoutDetailsAction.BackTapped -> navigateBack()
            is HangoutDetailsAction.SegmentChanged ->
                updateState(state.copy(selectedSegment = action.segment))
            HangoutDetailsAction.EditTapped -> navigateToEdit()
            HangoutDetailsAction.JoinTapped -> joinHangout()
            HangoutDetailsAction.ExitTapped -> presentExitConfirm()
            HangoutDetailsAction.SeeAllMembersTapped -> navigateToMembersList()
            is HangoutDetailsAction.MemberTapped -> navigateToProfile(action.member.id)
        }
    }

    private fun navigateToMembersList() {
        viewModelScope.launch {
            navigator.navigateTo(
                MemberListScreens.MembersList.route,
                MemberListInputData(
                    title = "Members",
                    viewerRole = state.uiModel?.userActivityType
                        ?: AppUIEntities.UserActivityRole.NOT_JOINED,
                    currentUserId = state.currentUserId,
                    activityType = AppUIEntities.ActivityType.HANG_OUTS,
                    loadPage = { page, size, keyword ->
                        dependencies.useCase.fetchHangoutMembersPage(inputData.hangoutId, page, size, keyword)
                    },
                    onMemberTapped = { userId -> navigateToProfile(userId) }
                )
            )
        }
    }

    private fun navigateToProfile(userId: String) {
        viewModelScope.launch {
            navigator.navigateTo(SharedRoutes.PROFILE_DETAIL, ProfileDetailNavArgs(userId))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }

    private fun navigateToEdit() {
        val prefill = state.uiModel?.editPrefillData ?: return
        viewModelScope.launch {
            navigator.navigateTo(
                HangoutsScreens.Edit.route,
                HangoutCreateInputData(hangoutId = inputData.hangoutId, prefill = prefill)
            )
        }
    }

    // MARK: - Join

    private fun joinHangout() {
        viewModelScope.launch {
            postEffect(HangoutDetailsSideEffect.Loading(true))
            try {
                dependencies.useCase.joinHangout(inputData.hangoutId)
                showJoinSnackBar()
                getDetails()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = "Could not join",
                    subtitle = "Please try again.",
                    style = AppSnackBar.Style.ERROR
                )
            } finally {
                postEffect(HangoutDetailsSideEffect.Loading(false))
            }
        }
    }

    /** Public hangouts join immediately; private hangouts create a pending request. */
    private fun showJoinSnackBar() {
        val name = state.uiModel?.name ?: "the hangout"
        if (state.isPrivate) {
            AppSnackBar.show(
                title = "Request sent",
                subtitle = "$name will review your request",
                style = AppSnackBar.Style.SUCCESS
            )
        } else {
            AppSnackBar.show(title = "Joined $name", style = AppSnackBar.Style.SUCCESS)
        }
    }

    // MARK: - Exit flow (hangouts have no owner-transfer gate)

    private fun presentExitConfirm() {
        AppAlertPresenter.present(
            AppAlert(
                config = AppAlert.Config(
                    title = "Leave hangout?",
                    subtitle = "Are you sure you want to leave this hangout? You will no longer " +
                        "be able to participate or see updates."
                ),
                actions = listOf(
                    AppAlert.Action(
                        title = "Leave hangout",
                        style = AppAlert.Action.Style.DESTRUCTIVE
                    ) { performExit() },
                    AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.PRIMARY)
                )
            )
        )
    }

    private fun performExit() {
        viewModelScope.launch {
            postEffect(HangoutDetailsSideEffect.Loading(true))
            try {
                dependencies.useCase.exitHangout(inputData.hangoutId)
                AppSnackBar.show(title = "You left the hangout", style = AppSnackBar.Style.SUCCESS)
                navigator.navigateUp()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = "Could not leave hangout",
                    subtitle = "Please try again.",
                    style = AppSnackBar.Style.ERROR
                )
            } finally {
                postEffect(HangoutDetailsSideEffect.Loading(false))
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch { getDetails() }
    }

    private suspend fun getDetails() {
        try {
            val data = dependencies.useCase.fetchDetailData(id = inputData.hangoutId)
            updateState(state.copy(uiModel = data))
        } catch (e: Exception) {
            // Handle error
        }
        try {
            val members = dependencies.useCase.fetchHangoutMembers(inputData.hangoutId)
            updateState(state.copy(membersData = members))
        } catch (e: Exception) {
            // Members are best-effort.
        }
    }
}
