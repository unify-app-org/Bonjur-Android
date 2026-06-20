package com.bonjur.communities.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.navigation.CommunitiesScreens
import com.bonjur.communities.presentation.detail.model.CommunityDetailAction
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.communities.presentation.detail.model.CommunityDetailSideEffect
import com.bonjur.communities.presentation.detail.model.CommunityDetailViewState
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.manager.TokenManager
import com.bonjur.profile.navigation.ProfileScreens
import com.bonjur.profile.presentation.detail.models.ProfileDetailInputData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<CommunityDetailViewState, CommunityDetailAction, CommunityDetailSideEffect>(
    CommunityDetailViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: CommunitiesUseCase,
        val tokenManager: TokenManager
    )

    private lateinit var inputData: CommunityDetailInputData
    private lateinit var navigator: Navigator

    fun init(inputData: CommunityDetailInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(currentUserId = dependencies.tokenManager.getUserId()))
        fetchData()
    }

    override fun handle(action: CommunityDetailAction) {
        when (action) {
            CommunityDetailAction.FetchData -> fetchData()
            CommunityDetailAction.BackTapped -> navigateBack()
            CommunityDetailAction.EditTapped -> navigateToEdit()
            CommunityDetailAction.SeeAllMembersTapped -> navigateToMembersList()
            is CommunityDetailAction.AssignRole -> assignRole(action.userId, action.role)
            is CommunityDetailAction.UserTapped -> navigateToUser(action.userId)
            is CommunityDetailAction.ClubItemTapped -> handleClubItemTapped(action.id)
            is CommunityDetailAction.SegmentChanged -> {
                updateState(state.copy(selectedSegment = action.segment))
            }
        }
    }

    private fun navigateToMembersList() {
        viewModelScope.launch {
            val destination = CommunitiesScreens.MembersList(
                communityId = inputData.communityId,
                title = "Members"
            )
            navigator.navigateTo(destination.route, destination)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }

    private fun navigateToUser(userId: String) {
        viewModelScope.launch {
            navigator.navigateTo(
                ProfileScreens.ProfileDetail.route,
                ProfileDetailInputData(userId = userId)
            )
        }
    }

    private fun navigateToEdit() {
        val prefill = state.uiModel?.editPrefillData ?: return
        viewModelScope.launch {
            navigator.navigateTo(
                ClubsScreens.Edit.route,
                ClubCreateInputData(
                    clubId = inputData.communityId,
                    prefill = prefill
                )
            )
        }
    }

    private fun assignRole(userId: String, role: AppUIEntities.UserActivityRole) {
        viewModelScope.launch {
            try {
                dependencies.useCase.assignRole(inputData.communityId, userId, role)
                AppSnackBar.show(title = "Role updated", style = AppSnackBar.Style.SUCCESS)
                refreshMembers()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = "Could not update role",
                    subtitle = "Please try again.",
                    style = AppSnackBar.Style.ERROR
                )
            }
        }
    }

    private suspend fun refreshMembers() {
        try {
            val members = dependencies.useCase.fetchCommunityMembers(
                communityId = inputData.communityId
            )
            updateState(state.copy(membersData = members))
        } catch (e: Exception) {
            // Best-effort refresh; keep prior members on failure.
        }
    }

    private fun handleClubItemTapped(id: Int) {
        viewModelScope.launch {
            navigator.navigateTo(
                ClubsScreens.Details.route,
                ClubDetailsInputData(clubId = id)
            )
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchUIModel()
        }
    }

    private suspend fun fetchUIModel() {
        try {
            val uiModel = dependencies.useCase.fetchCommunityDetails(
                communityId = inputData.communityId
            )
            updateState(state.copy(uiModel = uiModel))
        } catch (e: Exception) {
            print(e)
        }
        try {
            val clubs = dependencies.useCase.fetchClubs(
                communityId = inputData.communityId
            )
            updateState(state.copy(clubsData = clubs))
        } catch (e: Exception) {
            // Clubs are best-effort; keep detail visible without them.
        }
        try {
            val members = dependencies.useCase.fetchCommunityMembers(
                communityId = inputData.communityId
            )
            updateState(state.copy(membersData = members))
        } catch (e: Exception) {
            // Members are best-effort; keep detail visible without them.
        }
    }
}
