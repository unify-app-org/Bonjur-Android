package com.bonjur.communities.presentation.detail

import com.bonjur.designsystem.R as DesignR
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.communities.R
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.create.models.ClubCreateInputData
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.communities.presentation.detail.model.CommunityDetailAction
import com.bonjur.communities.presentation.detail.model.CommunityDetailInputData
import com.bonjur.communities.presentation.detail.model.CommunityDetailSideEffect
import com.bonjur.communities.presentation.detail.model.CommunityDetailViewState
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.member.list.MemberListInputData
import com.bonjur.member.list.MemberListScreens
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.SharedRoutes
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

    private var clubsPage = 0
    private var isLoadingMoreClubs = false

    fun init(inputData: CommunityDetailInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(currentUserId = dependencies.tokenManager.getUserId()))
        // No fetch here — the screen sends `FetchData` on every entry so returning from
        // Edit reloads. See the comment in the screen composable.
    }

    override fun handle(action: CommunityDetailAction) {
        when (action) {
            CommunityDetailAction.FetchData -> fetchData()

            CommunityDetailAction.LoadMoreClubs -> loadMoreClubs()
            CommunityDetailAction.BackTapped -> navigateBack()
            CommunityDetailAction.EditTapped -> navigateToEdit()
            CommunityDetailAction.SeeAllMembersTapped -> navigateToMembersList()
            CommunityDetailAction.CreateClubTapped -> navigateToCreateClub()
            CommunityDetailAction.CreateEventTapped -> navigateToCreateEvent()
            is CommunityDetailAction.AssignRole -> assignRole(action.userId, action.role)
            is CommunityDetailAction.UserTapped -> navigateToUser(action.userId)
            is CommunityDetailAction.ClubItemTapped -> handleClubItemTapped(action.id)
            is CommunityDetailAction.SegmentChanged -> {
                updateState(state.copy(selectedSegment = action.segment))
            }
        }
    }

    private fun navigateToCreateClub() {
        viewModelScope.launch {
            navigator.navigateTo(ClubsScreens.Create.route)
        }
    }

    // `communities` has no Gradle dependency on `events`, so the event-create
    // screen is reached by its route string (same pattern as CLUB_DETAILS).
    private fun navigateToCreateEvent() {
        viewModelScope.launch {
            navigator.navigateTo(SharedRoutes.EVENT_CREATE)
        }
    }

    private fun navigateToMembersList() {
        viewModelScope.launch {
            navigator.navigateTo(
                MemberListScreens.MembersList.route,
                MemberListInputData(
                    title = LanguageManager.string(DesignR.string.common_members),
                    viewerRole = state.uiModel?.userActivity
                        ?: AppUIEntities.UserActivityRole.NOT_JOINED,
                    currentUserId = state.currentUserId,
                    activityType = AppUIEntities.ActivityType.COMMUNITY,
                    totalCount = state.uiModel?.membersCount,
                    loadPage = { page, size, keyword ->
                        dependencies.useCase.fetchCommunityMembersPage(inputData.communityId, page, size, keyword)
                    },
                    assignRole = { userId, role ->
                        dependencies.useCase.assignRole(inputData.communityId, userId, role)
                    },
                    onMemberTapped = { userId -> navigateToUser(userId) }
                )
            )
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
                // Inside a community, the profile is scoped to THAT community, not the one
                // stored at login (which is what every other context uses).
                ProfileDetailInputData(userId = userId, communityId = inputData.communityId)
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
                AppSnackBar.show(title = LanguageManager.string(R.string.comm_role_updated), style = AppSnackBar.Style.SUCCESS)
                refreshMembers()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = LanguageManager.string(R.string.comm_role_update_fail),
                    subtitle = LanguageManager.string(DesignR.string.common_try_again),
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
                communityId = inputData.communityId,
                page = 0,
                size = CLUBS_PAGE_SIZE
            )
            clubsPage = clubs.page
            updateState(state.copy(clubsData = clubs.items, clubsHasMore = clubs.hasMore))
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
            android.util.Log.e("CommunityDetail", "fetchCommunityMembers failed", e)
        }
    }

    private fun loadMoreClubs() {
        if (isLoadingMoreClubs || !state.clubsHasMore) return
        isLoadingMoreClubs = true
        val nextPage = clubsPage + 1
        viewModelScope.launch {
            try {
                val result = dependencies.useCase.fetchClubs(
                    communityId = inputData.communityId,
                    page = nextPage,
                    size = CLUBS_PAGE_SIZE
                )
                clubsPage = result.page
                // The list is re-sorted server-side, so a club can straddle the page
                // boundary and arrive twice — a duplicate key crashes LazyColumn.
                val seen = state.clubsData.mapTo(mutableSetOf()) { it.id }
                updateState(
                    state.copy(
                        clubsData = state.clubsData + result.items.filter { seen.add(it.id) },
                        clubsHasMore = result.hasMore
                    )
                )
            } catch (e: Exception) {
                // Stop paging rather than retry-looping on every scroll tick.
                updateState(state.copy(clubsHasMore = false))
            } finally {
                isLoadingMoreClubs = false
            }
        }
    }

    private companion object {
        const val CLUBS_PAGE_SIZE = 10
    }
}
