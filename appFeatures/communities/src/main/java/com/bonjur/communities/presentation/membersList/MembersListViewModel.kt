package com.bonjur.communities.presentation.membersList

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.communities.domain.useCase.CommunitiesUseCase
import com.bonjur.member.model.MemberCellModel
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.communities.presentation.membersList.models.MembersListAction
import com.bonjur.communities.presentation.membersList.models.MembersListInputData
import com.bonjur.communities.presentation.membersList.models.MembersListSideEffect
import com.bonjur.communities.presentation.membersList.models.MembersListViewState
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.profile.navigation.ProfileScreens
import com.bonjur.profile.presentation.detail.models.ProfileDetailInputData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembersListViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<MembersListViewState, MembersListAction, MembersListSideEffect>(
    MembersListViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: CommunitiesUseCase
    )

    companion object {
        private const val PAGE_SIZE = 20
    }

    private lateinit var inputData: MembersListInputData
    private lateinit var navigator: Navigator

    private val loadedMembers = mutableListOf<MemberCellModel>()
    private var nextPage = 0
    private var isFetching = false

    fun init(inputData: MembersListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(state.copy(title = inputData.title))
        handle(MembersListAction.OnAppear)
    }

    override fun handle(action: MembersListAction) {
        when (action) {
            MembersListAction.OnAppear -> loadNextPage(initial = true)
            MembersListAction.LoadMore -> loadNextPage(initial = false)
            MembersListAction.BackTapped -> viewModelScope.launch { navigator.navigateUp() }
            is MembersListAction.MemberTapped -> viewModelScope.launch {
                navigator.navigateTo(
                    ProfileScreens.ProfileDetail.route,
                    ProfileDetailInputData(userId = action.member.id)
                )
            }
        }
    }

    private fun loadNextPage(initial: Boolean) {
        if (isFetching || (!initial && !state.hasMore)) return
        isFetching = true
        viewModelScope.launch {
            if (initial) postEffect(MembersListSideEffect.Loading(true))
            updateState(state.copy(isLoadingMore = !initial))
            try {
                val page = dependencies.useCase.fetchCommunityMembersPage(
                    communityId = inputData.communityId,
                    page = nextPage,
                    size = PAGE_SIZE
                )
                loadedMembers.addAll(page.members)
                nextPage += 1
                updateState(
                    state.copy(
                        sections = GroupedMembersData.from(loadedMembers).sections,
                        hasMore = page.hasMore
                    )
                )
            } catch (e: Exception) {
                // Keep current state
            } finally {
                isFetching = false
                updateState(state.copy(isLoadingMore = false))
                if (initial) postEffect(MembersListSideEffect.Loading(false))
            }
        }
    }
}
