package com.bonjur.clubs.presentation

import com.bonjur.designsystem.R as DesignR
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.clubs.R
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.domain.useCase.ClubsUseCase
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.model.*
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.alert.AppAlert
import com.bonjur.designSystem.components.alert.AppAlertPresenter
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.events.domain.useCase.EventsUseCase
import com.bonjur.events.navigation.EventsScreens
import com.bonjur.events.presentation.details.model.EventDetailsInputData
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
class ClubDetailsViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ClubDetailsViewState, ClubDetailsAction, ClubDetailsSideEffect>(
    ClubDetailsViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ClubsUseCase,
        val eventsUseCase: EventsUseCase,
        val tokenManager: TokenManager
    )

    private lateinit var inputData: ClubDetailsInputData
    private lateinit var navigator: Navigator
    fun init(inputData: ClubDetailsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(currentUserId = dependencies.tokenManager.getUserId()))
        fetchData()
    }

    override fun handle(action: ClubDetailsAction) {
        when (action) {
            ClubDetailsAction.FetchData -> fetchData()
            ClubDetailsAction.BackTapped -> navigateBack()
            is ClubDetailsAction.SegmentChanged -> {
                updateState(
                    state.copy(selectedSegment = action.segment)
                )
            }
            ClubDetailsAction.EditTapped -> navigateToEdit()
            ClubDetailsAction.JoinClubTapped -> joinClub()
            ClubDetailsAction.ExitTapped -> presentExitConfirm()
            ClubDetailsAction.SeeAllMembersTapped -> navigateToMembersList()
            is ClubDetailsAction.MemberTapped -> navigateToProfile(action.member.id)
            is ClubDetailsAction.EventTapped -> navigateToEvent(action.eventId)
            is ClubDetailsAction.AssignRole -> assignRole(action.userId, action.role)
            ClubDetailsAction.RequestVerificationTapped -> requestVerification()
            ClubDetailsAction.CreateEventTapped -> navigateToCreateEvent()
        }
    }

    // MARK: - Verification

    /**
     * Ask the community admins to verify this club, then refresh so the status
     * (and the verify CTA) reflects the pending request. Mirrors iOS
     * ClubDetailsViewModel.requestVerification().
     */
    private fun requestVerification() {
        viewModelScope.launch {
            try {
                dependencies.useCase.requestVerify(inputData.clubId)
                AppSnackBar.show(
                    title = LanguageManager.string(R.string.clubs_verification_requested),
                    subtitle = LanguageManager.string(R.string.clubs_verification_requested_sub),
                    style = AppSnackBar.Style.SUCCESS
                )
                fetchData()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = LanguageManager.string(R.string.clubs_verification_fail),
                    subtitle = LanguageManager.string(R.string.common_try_again),
                    style = AppSnackBar.Style.ERROR
                )
            }
        }
    }

    private fun navigateToMembersList() {
        viewModelScope.launch {
            navigator.navigateTo(
                MemberListScreens.MembersList.route,
                MemberListInputData(
                    title = LanguageManager.string(R.string.clubs_members_title),
                    viewerRole = state.uiModel?.userActivityType
                        ?: AppUIEntities.UserActivityRole.NOT_JOINED,
                    currentUserId = state.currentUserId,
                    activityType = AppUIEntities.ActivityType.CLUBS,
                    totalCount = state.uiModel?.membersCount,
                    loadPage = { page, size, keyword ->
                        dependencies.useCase.fetchClubMembersPage(inputData.clubId, page, size, keyword)
                    },
                    assignRole = { userId, role ->
                        dependencies.useCase.assignRole(inputData.clubId, userId, role)
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
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun navigateToEdit() {
        viewModelScope.launch {
            navigator.navigateTo(
                ClubsScreens.Edit.route,
                ClubCreateInputData(
                    clubId = inputData.clubId,
                    prefill = state.uiModel?.editPrefillData
                )
            )
        }
    }

    // MARK: - Join

    private fun joinClub() {
        viewModelScope.launch {
            postEffect(ClubDetailsSideEffect.Loading(true))
            try {
                dependencies.useCase.joinClub(inputData.clubId)
                showJoinSnackBar()
                fetchUIModel()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = "Could not join",
                    subtitle = LanguageManager.string(R.string.common_try_again),
                    style = AppSnackBar.Style.ERROR
                )
            } finally {
                postEffect(ClubDetailsSideEffect.Loading(false))
            }
        }
    }

    /** Public clubs join immediately; private clubs create a pending request. */
    private fun showJoinSnackBar() {
        val name = state.uiModel?.name ?: "the club"
        if (state.isPrivate) {
            AppSnackBar.show(
                title = LanguageManager.string(R.string.clubs_join_request_sent),
                subtitle = "$name will review your request",
                style = AppSnackBar.Style.SUCCESS
            )
        } else {
            AppSnackBar.show(title = "Joined $name", style = AppSnackBar.Style.SUCCESS)
        }
    }

    // MARK: - Exit flow

    private fun presentExitConfirm() {
        AppAlertPresenter.present(
            AppAlert(
                config = AppAlert.Config(
                    title = LanguageManager.string(R.string.clubs_exit_title),
                    subtitle = LanguageManager.string(R.string.clubs_exit_subtitle)
                ),
                actions = listOf(
                    AppAlert.Action(
                        title = LanguageManager.string(R.string.clubs_exit_confirm),
                        style = AppAlert.Action.Style.DESTRUCTIVE
                    ) { handleExitConfirmed() },
                    AppAlert.Action(title = LanguageManager.string(DesignR.string.common_cancel), style = AppAlert.Action.Style.PRIMARY)
                )
            )
        )
    }

    private fun handleExitConfirmed() {
        viewModelScope.launch {
            postEffect(ClubDetailsSideEffect.Loading(true))
            val role = state.uiModel?.userActivityType ?: AppUIEntities.UserActivityRole.NOT_JOINED
            // President must hand off ownership: gate exit on an existing VP.
            if (role == AppUIEntities.UserActivityRole.PRESIDENT) {
                try {
                    val hasVicePresident =
                        dependencies.useCase.clubHasVicePresident(inputData.clubId)
                    if (!hasVicePresident) {
                        postEffect(ClubDetailsSideEffect.Loading(false))
                        presentTransferOwnership()
                        return@launch
                    }
                } catch (e: Exception) {
                    postEffect(ClubDetailsSideEffect.Loading(false))
                    showExitError()
                    return@launch
                }
            }
            performExit()
        }
    }

    private suspend fun performExit() {
        try {
            dependencies.useCase.exitClub(inputData.clubId)
            AppSnackBar.show(title = LanguageManager.string(R.string.clubs_left), style = AppSnackBar.Style.SUCCESS)
            navigator.navigateUp()
        } catch (e: Exception) {
            showExitError()
        } finally {
            postEffect(ClubDetailsSideEffect.Loading(false))
        }
    }

    private fun presentTransferOwnership() {
        AppAlertPresenter.present(
            AppAlert(
                config = AppAlert.Config(
                    title = LanguageManager.string(R.string.clubs_transfer_title),
                    subtitle = LanguageManager.string(R.string.clubs_transfer_subtitle)
                ),
                actions = listOf(
                    AppAlert.Action(title = LanguageManager.string(DesignR.string.common_cancel), style = AppAlert.Action.Style.SECONDARY),
                    AppAlert.Action(
                        title = LanguageManager.string(R.string.common_assign),
                        style = AppAlert.Action.Style.PRIMARY
                    ) {
                        // Opens the members list to assign a VP — deferred (row 5).
                        // Once the members module lands, navigate there here.
                    }
                )
            )
        )
    }

    private fun showExitError() {
        AppSnackBar.show(
            title = LanguageManager.string(R.string.clubs_exit_fail),
            subtitle = LanguageManager.string(R.string.common_try_again),
            style = AppSnackBar.Style.ERROR
        )
    }

    // MARK: - Assign role (dormant until the members list lands)

    private fun assignRole(userId: String, role: AppUIEntities.UserActivityRole) {
        viewModelScope.launch {
            try {
                dependencies.useCase.assignRole(inputData.clubId, userId, role)
                AppSnackBar.show(title = LanguageManager.string(R.string.clubs_role_updated), style = AppSnackBar.Style.SUCCESS)
                fetchUIModel()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = LanguageManager.string(R.string.clubs_role_update_fail),
                    subtitle = LanguageManager.string(R.string.common_try_again),
                    style = AppSnackBar.Style.ERROR
                )
            }
        }
    }

    // MARK: - Fetch

    private fun fetchData() {
        viewModelScope.launch {
            fetchUIModel()
        }
        fetchEvents()
    }

    /** Club's active events for the Events tab (first page). Mirrors iOS ClubDetailsViewModel. */
    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val events = dependencies.eventsUseCase.fetchClubEvents(
                    clubId = inputData.clubId,
                    page = 0,
                    size = 10
                )
                updateState(state.copy(eventsData = events))
            } catch (e: Exception) {
                // Events tab is best-effort; keep detail visible without them.
            }
        }
    }

    private fun navigateToCreateEvent() {
        viewModelScope.launch {
            navigator.navigateTo(EventsScreens.Create.route)
        }
    }

    private fun navigateToEvent(eventId: String) {
        viewModelScope.launch {
            navigator.navigateTo(EventsScreens.Details.route, EventDetailsInputData(eventId = eventId))
        }
    }

    private suspend fun fetchUIModel() {
        try {
            val uiModel = dependencies.useCase.fetchClubsDetails(
                clubId = inputData.clubId
            )
            updateState(state.copy(uiModel = uiModel))
        } catch (e: Exception) {
            // Handle error
        }
        fetchMembers()
    }

    private suspend fun fetchMembers() {
        try {
            val members = dependencies.useCase.fetchClubMembers(inputData.clubId)
            updateState(state.copy(membersData = members))
        } catch (e: Exception) {
            // Members are best-effort; keep detail visible without them.
        }
    }
}