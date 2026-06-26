package com.bonjur.member.list

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Activity-agnostic see-all members screen. Pages + role changes go through the
 * closures supplied in [MemberListInputData], so this single screen serves clubs /
 * events / hangouts / communities (mirrors iOS shared MembersListViewModel).
 */
@HiltViewModel
class MemberListViewModel @Inject constructor() :
    FeatureViewModel<MemberListViewState, MemberListAction, MemberListSideEffect>(
        MemberListViewState()
    ) {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private lateinit var inputData: MemberListInputData
    private lateinit var navigator: Navigator

    private val loadedMembers = mutableListOf<MemberCellModel>()
    private var nextPage = 0
    private var isFetching = false

    fun init(inputData: MemberListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(
            state.copy(
                title = inputData.title,
                viewerRole = inputData.viewerRole,
                currentUserId = inputData.currentUserId,
                activityType = inputData.activityType
            )
        )
        handle(MemberListAction.OnAppear)
    }

    override fun handle(action: MemberListAction) {
        when (action) {
            MemberListAction.OnAppear -> loadNextPage(initial = true)
            MemberListAction.LoadMore -> loadNextPage(initial = false)
            MemberListAction.BackTapped -> viewModelScope.launch { navigator.navigateUp() }
            is MemberListAction.MemberTapped -> inputData.onMemberTapped(action.member.id)
            is MemberListAction.AssignRole -> assignRole(action.userId, action.role)
        }
    }

    private fun loadNextPage(initial: Boolean) {
        if (isFetching || (!initial && !state.hasMore)) return
        isFetching = true
        viewModelScope.launch {
            if (initial) postEffect(MemberListSideEffect.Loading(true))
            updateState(state.copy(isLoadingMore = !initial))
            try {
                val page = inputData.loadPage(nextPage, PAGE_SIZE)
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
                if (initial) postEffect(MemberListSideEffect.Loading(false))
            }
        }
    }

    private fun assignRole(userId: String, role: AppUIEntities.UserActivityRole) {
        val assign = inputData.assignRole ?: return
        viewModelScope.launch {
            try {
                assign(userId, role)
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

    private fun refreshMembers() {
        loadedMembers.clear()
        nextPage = 0
        updateState(state.copy(hasMore = true))
        loadNextPage(initial = true)
    }
}
