package com.bonjur.events.presentation.details

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.components.alert.AppAlert
import com.bonjur.designSystem.components.alert.AppAlertPresenter
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.events.domain.useCase.EventsUseCase
import com.bonjur.events.navigation.EventsScreens
import com.bonjur.events.presentation.create.models.EventCreateInputData
import com.bonjur.events.presentation.details.model.EventDetailsAction
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.events.presentation.details.model.EventDetailsSideEffect
import com.bonjur.events.presentation.details.model.EventDetailsViewState
import com.bonjur.member.list.MemberListInputData
import com.bonjur.member.list.MemberListScreens
import com.bonjur.navigation.ClubDetailsNavArgs
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.ProfileDetailNavArgs
import com.bonjur.navigation.SharedRoutes
import com.bonjur.navigation.route
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.network.manager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<EventDetailsViewState, EventDetailsAction, EventDetailsSideEffect>(
    EventDetailsViewState()
) {
    
    data class Dependencies @Inject constructor(
        val useCase: EventsUseCase,
        val tokenManager: TokenManager
    )

    private lateinit var inputData: EventDetailsInputData
    private lateinit var navigator: Navigator

    fun init(inputData: EventDetailsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(currentUserId = dependencies.tokenManager.getUserId()))
        fetchData()
    }

    override fun handle(action: EventDetailsAction) {
        when (action) {
            EventDetailsAction.FetchData -> fetchData()
            EventDetailsAction.BackTapped -> navigateBack()
            is EventDetailsAction.SegmentChanged -> {
                updateState(
                    state.copy(selectedSegment = action.segment)
                )
            }
            EventDetailsAction.EditTapped -> navigateToEdit()
            EventDetailsAction.JoinTapped -> joinEvent()
            EventDetailsAction.ExitTapped -> presentExitConfirm()
            EventDetailsAction.ClubTapped -> navigateToClub()
            EventDetailsAction.SeeAllMembersTapped -> navigateToMembersList()
            is EventDetailsAction.MemberTapped -> navigateToProfile(action.member.id)
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
                    activityType = AppUIEntities.ActivityType.EVENTS,
                    loadPage = { page, size ->
                        dependencies.useCase.fetchEventMembersPage(inputData.eventId, page, size)
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

    private fun navigateToClub() {
        val clubId = state.uiModel?.clubId ?: return
        if (clubId == 0) return
        viewModelScope.launch {
            navigator.navigateTo(SharedRoutes.CLUB_DETAILS, ClubDetailsNavArgs(clubId))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun navigateToEdit() {
        val prefill = state.uiModel?.editPrefillData ?: return
        viewModelScope.launch {
            navigator.navigateTo(
                EventsScreens.Edit.route,
                EventCreateInputData(eventId = inputData.eventId, prefill = prefill)
            )
        }
    }

    // MARK: - Join

    private fun joinEvent() {
        viewModelScope.launch {
            postEffect(EventDetailsSideEffect.Loading(true))
            try {
                dependencies.useCase.joinEvent(inputData.eventId)
                showJoinSnackBar()
                getDetails()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = "Could not join",
                    subtitle = "Please try again.",
                    style = AppSnackBar.Style.ERROR
                )
            } finally {
                postEffect(EventDetailsSideEffect.Loading(false))
            }
        }
    }

    /** Public events join immediately; private events create a pending request. */
    private fun showJoinSnackBar() {
        val name = state.uiModel?.name ?: "the event"
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

    // MARK: - Exit flow (events have no owner-transfer gate)

    private fun presentExitConfirm() {
        AppAlertPresenter.present(
            AppAlert(
                config = AppAlert.Config(
                    title = "Leave event?",
                    subtitle = "Are you sure you want to leave this event? You will no longer " +
                        "be able to participate or see updates."
                ),
                actions = listOf(
                    AppAlert.Action(
                        title = "Leave event",
                        style = AppAlert.Action.Style.DESTRUCTIVE
                    ) { performExit() },
                    AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.PRIMARY)
                )
            )
        )
    }

    private fun performExit() {
        viewModelScope.launch {
            postEffect(EventDetailsSideEffect.Loading(true))
            try {
                dependencies.useCase.exitEvent(inputData.eventId)
                AppSnackBar.show(title = "You left the event", style = AppSnackBar.Style.SUCCESS)
                navigator.navigateUp()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = "Could not leave event",
                    subtitle = "Please try again.",
                    style = AppSnackBar.Style.ERROR
                )
            } finally {
                postEffect(EventDetailsSideEffect.Loading(false))
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            getDetails()
        }
    }

    private suspend fun getDetails() {
        try {
            val data = dependencies.useCase.fetchDetailsData(
                id = inputData.eventId
            )
            updateState(
                state.copy(uiModel = data)
            )
        } catch (e: Exception) {
            // Handle error
        }
        try {
            val members = dependencies.useCase.fetchEventMembers(inputData.eventId)
            updateState(state.copy(membersData = members))
        } catch (e: Exception) {
            // Members are best-effort.
        }
    }
}