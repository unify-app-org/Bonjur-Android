package com.bonjur.member.list

import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.designsystem.R
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.member.model.GroupedMembersData
import com.bonjur.member.model.MemberCellModel
import com.bonjur.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
        private const val SEARCH_DEBOUNCE_MS = 300L
    }

    private lateinit var inputData: MemberListInputData
    private lateinit var navigator: Navigator

    private val loadedMembers = mutableListOf<MemberCellModel>()
    private val loadedIds = mutableSetOf<String>()
    private var nextPage = 0
    private var isFetching = false
    private var searchJob: kotlinx.coroutines.Job? = null
    private var loadJob: kotlinx.coroutines.Job? = null

    /**
     * Bumped by every restart-from-page-0 (search, refresh, first load). A page
     * that comes back under an older generation is dropped instead of being
     * appended onto the new result set — otherwise an in-flight `loadMore` lands
     * after a search and puts the previous 20 rows back on top of the one match.
     */
    private var loadGeneration = 0

    fun init(inputData: MemberListInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(
            state.copy(
                title = inputData.title,
                viewerRole = inputData.viewerRole,
                currentUserId = inputData.currentUserId,
                activityType = inputData.activityType,
                totalCount = inputData.totalCount
            )
        )
        handle(MemberListAction.OnAppear)
    }

    override fun handle(action: MemberListAction) {
        when (action) {
            MemberListAction.OnAppear -> refreshMembers()
            MemberListAction.LoadMore -> loadNextPage(initial = false)
            MemberListAction.BackTapped -> viewModelScope.launch { navigator.navigateUp() }
            is MemberListAction.MemberTapped -> inputData.onMemberTapped(action.member.id)
            is MemberListAction.SearchTextChanged -> handleSearchTextChanged(action.text)
            is MemberListAction.AssignRole -> assignRole(action.userId, action.role)
        }
    }

    /** Debounced server-side member search, mirroring the clubs/events list (300ms). */
    private fun handleSearchTextChanged(text: String) {
        updateState(state.copy(searchText = text))
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            refreshMembers()
        }
    }

    /** Trimmed search term, null when blank so the query param is omitted. */
    private fun currentKeyword(): String? = state.searchText.trim().ifEmpty { null }

    private fun loadNextPage(initial: Boolean) {
        // A restart always wins: cancel whatever page is in flight rather than
        // dropping the new request on the `isFetching` guard.
        if (initial) {
            loadJob?.cancel()
            isFetching = false
        } else if (isFetching || !state.hasMore) {
            return
        }
        isFetching = true
        val generation = loadGeneration
        val requestedPage = nextPage
        loadJob = viewModelScope.launch {
            if (initial) postEffect(MemberListSideEffect.Loading(true))
            updateState(state.copy(isLoadingMore = !initial))
            try {
                val page = inputData.loadPage(requestedPage, PAGE_SIZE, currentKeyword())
                if (generation != loadGeneration) return@launch
                // The backend sorts by a non-unique `modifiedAt`, so pages overlap.
                // De-dupe by id: duplicate keys would otherwise crash the LazyColumn.
                page.members.forEach { member ->
                    // Members with no userId all share the "-" placeholder, so only
                    // de-dupe real ids — otherwise distinct people collapse into one row.
                    val hasRealId = member.id.isNotBlank() && member.id != "-"
                    if (!hasRealId || loadedIds.add(member.id)) loadedMembers.add(member)
                }
                nextPage = requestedPage + 1
                updateState(
                    state.copy(
                        sections = GroupedMembersData.from(loadedMembers).sections,
                        hasMore = page.hasMore,
                        totalCount = page.totalCount ?: state.totalCount
                    )
                )
            } catch (e: Exception) {
                // Keep current state
            } finally {
                // Release the flags on the stale path too, or a dropped page
                // leaves isLoadingMore stuck and blocks all later paging.
                if (generation == loadGeneration) {
                    isFetching = false
                    updateState(state.copy(isLoadingMore = false))
                }
                if (initial) postEffect(MemberListSideEffect.Loading(false))
            }
        }
    }

    private fun assignRole(userId: String, role: AppUIEntities.UserActivityRole) {
        val assign = inputData.assignRole ?: return
        viewModelScope.launch {
            try {
                assign(userId, role)
                AppSnackBar.show(title = LanguageManager.string(R.string.common_role_updated), style = AppSnackBar.Style.SUCCESS)
                refreshMembers()
            } catch (e: Exception) {
                AppSnackBar.show(
                    title = LanguageManager.string(R.string.common_role_update_failed),
                    subtitle = LanguageManager.string(R.string.common_try_again),
                    style = AppSnackBar.Style.ERROR
                )
            }
        }
    }

    /** Restart from page 0, invalidating anything already in flight. */
    private fun refreshMembers() {
        loadGeneration += 1
        loadedMembers.clear()
        loadedIds.clear()
        nextPage = 0
        updateState(
            state.copy(
                hasMore = true,
                totalCount = null,
                listResetToken = state.listResetToken + 1
            )
        )
        loadNextPage(initial = true)
    }
}
