package com.bonjur.clubs.presentation

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
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
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
        val useCase: ClubsUseCase
    )

    private lateinit var inputData: ClubDetailsInputData
    private lateinit var navigator: Navigator
    fun init(inputData: ClubDetailsInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
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
            is ClubDetailsAction.AssignRole -> assignRole(action.userId, action.role)
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
                    subtitle = "Please try again.",
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
                title = "Request sent",
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
                    title = "Exit Club?",
                    subtitle = "Are you sure you want to leave this club? You will no longer " +
                        "be able to participate in events or see club updates."
                ),
                actions = listOf(
                    AppAlert.Action(
                        title = "Exit club",
                        style = AppAlert.Action.Style.DESTRUCTIVE
                    ) { handleExitConfirmed() },
                    AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.PRIMARY)
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
            AppSnackBar.show(title = "You left the club", style = AppSnackBar.Style.SUCCESS)
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
                    title = "Transfer Ownership Required",
                    subtitle = "You are the owner of this club. Before leaving, you must assign " +
                        "the vice president role to another member to keep the club active."
                ),
                actions = listOf(
                    AppAlert.Action(title = "Cancel", style = AppAlert.Action.Style.SECONDARY),
                    AppAlert.Action(
                        title = "Assign",
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
            title = "Could not leave club",
            subtitle = "Please try again.",
            style = AppSnackBar.Style.ERROR
        )
    }

    // MARK: - Assign role (dormant until the members list lands)

    private fun assignRole(userId: String, role: AppUIEntities.UserActivityRole) {
        viewModelScope.launch {
            try {
                dependencies.useCase.assignRole(inputData.clubId, userId, role)
                AppSnackBar.show(title = "Role updated", style = AppSnackBar.Style.SUCCESS)
                fetchUIModel()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = "Could not update role",
                    subtitle = "Please try again.",
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
    }
}