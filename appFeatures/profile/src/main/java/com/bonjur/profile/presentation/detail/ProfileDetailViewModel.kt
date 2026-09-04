package com.bonjur.profile.presentation.detail

import com.bonjur.designsystem.R as DesignR
import com.bonjur.designSystem.localization.LanguageManager
import com.bonjur.profile.R
import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.clubs.navigation.ClubsScreens
import com.bonjur.clubs.presentation.model.ClubDetailsInputData
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.snackbar.AppSnackBar
import com.bonjur.events.navigation.EventsScreens
import com.bonjur.events.presentation.details.model.EventDetailsInputData
import com.bonjur.hangouts.navigation.HangoutsScreens
import com.bonjur.hangouts.presentation.detail.model.HangoutDetailsInputData
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.network.manager.TokenManager
import com.bonjur.profile.data.DTOs.ProfileUpdateRequest
import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.profile.navigation.ProfileScreens
import com.bonjur.profile.presentation.detail.models.ProfileDetailAction
import com.bonjur.profile.presentation.detail.models.ProfileDetailInputData
import com.bonjur.profile.presentation.detail.models.ProfileDetailSideEffect
import com.bonjur.profile.presentation.detail.models.ProfileDetailViewState
import com.bonjur.profile.presentation.detail.widget.UserCardWidgetPublisher
import com.bonjur.profile.presentation.editProfile.models.EditProfileInputData
import com.bonjur.profile.presentation.editProfile.models.Gender
import com.bonjur.profile.presentation.studentCard.models.StudentCardInputData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bonjur.network.model.userMessage
import com.bonjur.network.model.Page

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ProfileDetailViewState, ProfileDetailAction, ProfileDetailSideEffect>(
    ProfileDetailViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ProfileUseCase,
        val tokenManager: TokenManager,
        val widgetPublisher: UserCardWidgetPublisher
    )

    private lateinit var inputData: ProfileDetailInputData
    private lateinit var navigator: Navigator

    /** Last page index fetched per tab, plus the in-flight guard that keeps the
     *  end-of-list loader from asking for the same page twice while it scrolls past. */
    private var clubsPage = 0
    private var eventsPage = 0
    private var hangoutsPage = 0
    private var isLoadingMoreClubs = false
    private var isLoadingMoreEvents = false
    private var isLoadingMoreHangouts = false

    fun init(inputData: ProfileDetailInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        updateState(
            state.copy(
                isOwnProfile = inputData.userId.isNullOrEmpty(),
                isPushed = inputData.isPushed
            )
        )
        fetchData()
    }

    override fun handle(action: ProfileDetailAction) {
        when (action) {
            ProfileDetailAction.FetchData -> fetchData()

            ProfileDetailAction.LoadMoreClubs -> loadMoreClubs()

            ProfileDetailAction.LoadMoreEvents -> loadMoreEvents()

            ProfileDetailAction.LoadMoreHangouts -> loadMoreHangouts()

            ProfileDetailAction.BackTapped -> viewModelScope.launch { navigator.navigateUp() }

            is ProfileDetailAction.ClubsItemTapped -> viewModelScope.launch {
                navigator.navigateTo(
                    ClubsScreens.Details.route,
                    ClubDetailsInputData(clubId = action.id)
                )
            }

            is ProfileDetailAction.SegmentTapped -> viewModelScope.launch {
                updateState(state.copy(selectedSegment = action.segment))
            }

            is ProfileDetailAction.EventsItemTapped -> viewModelScope.launch {
                navigator.navigateTo(
                    EventsScreens.Details.route,
                    EventDetailsInputData(eventId = action.id)
                )
            }

            is ProfileDetailAction.HangoutsItemTapped -> viewModelScope.launch {
                navigator.navigateTo(
                    HangoutsScreens.Details.route,
                    HangoutDetailsInputData(hangoutId = action.id)
                )
            }

            ProfileDetailAction.SettingsTapped -> viewModelScope.launch {
                navigator.navigateTo(ProfileScreens.Settings.route)
            }

            ProfileDetailAction.EditProfileTapped -> viewModelScope.launch {
                navigator.navigateTo(
                    ProfileScreens.EditProfile.route,
                    EditProfileInputData(
                        profileData = state.uiModel,
                        onSaved = { fetchData() }
                    )
                )
            }

            ProfileDetailAction.UserCardTapped -> viewModelScope.launch {
                val userCardModel = state.uiModel?.userCardModel ?: return@launch
                navigator.navigateTo(
                    ProfileScreens.StudentCard.route,
                    StudentCardInputData(
                        userCardModel = userCardModel,
                        onSave = { cover ->
                            handle(ProfileDetailAction.UserCardCoverSaved(cover))
                        }
                    )
                )
            }

            is ProfileDetailAction.EmptyStateActionTapped -> viewModelScope.launch {
                // Own-profile tabs are empty because the user has joined nothing
                // yet, so each CTA opens that activity's create flow.
                val route = when (action.segment) {
                    ProfileDetailViewState.SegmentTypes.CLUBS -> ClubsScreens.Create.route
                    ProfileDetailViewState.SegmentTypes.EVENTS -> EventsScreens.Create.route
                    ProfileDetailViewState.SegmentTypes.HANGOUTS -> HangoutsScreens.Create.route
                }
                navigator.navigateTo(route)
            }

            is ProfileDetailAction.UserCardCoverSaved -> applyUserCardCover(action.backgroundType)
        }
    }

    /** Fetches profile + clubs + events + activities in parallel (mirrors iOS ProfileDetailViewModel). */
    private fun fetchData() {
        clubsPage = 0
        eventsPage = 0
        hangoutsPage = 0
        viewModelScope.launch {
            postEffect(ProfileDetailSideEffect.Loading(true))
            val userId = inputData.userId

            val results = coroutineScope {
                val user = async { runCatching { dependencies.useCase.fetchProfileData(userId, inputData.communityId) } }
                val clubs = async {
                    runCatching { dependencies.useCase.getMyClubs(userId, 0, PAGE_SIZE) }
                }
                // events/my is self-only — fetch only for own profile, hide for another user.
                val events = async {
                    if (userId.isNullOrEmpty()) {
                        runCatching { dependencies.useCase.getMyEvents(0, PAGE_SIZE) }
                    } else {
                        Result.success(Page.empty())
                    }
                }
                val hangouts = async {
                    runCatching { dependencies.useCase.getMyHangouts(userId, 0, PAGE_SIZE) }
                }
                FetchResults(user.await(), clubs.await(), events.await(), hangouts.await())
            }

            val firstError = applyResults(results)
            postEffect(ProfileDetailSideEffect.Loading(false))
            firstError?.let {
                postEffect(ProfileDetailSideEffect.Error(it.userMessage()))
            }
        }
    }

    private data class FetchResults(
        val user: Result<com.bonjur.profile.presentation.detail.models.ProfileDetail.UIModel>,
        val clubs: Result<Page<com.bonjur.clubs.presentation.list.models.ClubCardModel>>,
        val events: Result<Page<com.bonjur.events.presentation.list.models.EventsCardModel>>,
        val hangouts: Result<Page<com.bonjur.hangouts.presentation.list.model.HangoutsCardModel>>
    )

    private suspend fun applyResults(results: FetchResults): Throwable? {
        val base = results.user.getOrNull()
        if (base != null) {
            val myId = dependencies.tokenManager.getUserId()
            val isOther = !inputData.userId.isNullOrEmpty() && inputData.userId != myId
            val clubsPageResult = results.clubs.getOrNull() ?: Page.empty()
            val eventsPageResult = results.events.getOrNull() ?: Page.empty()
            val hangoutsPageResult = results.hangouts.getOrNull() ?: Page.empty()
            clubsPage = clubsPageResult.page
            eventsPage = eventsPageResult.page
            hangoutsPage = hangoutsPageResult.page
            updateState(
                state.copy(
                    uiModel = base.copy(
                        clubs = clubsPageResult.items,
                        events = eventsPageResult.items,
                        hangouts = hangoutsPageResult.items
                    ),
                    clubsHasMore = clubsPageResult.hasMore,
                    eventsHasMore = eventsPageResult.hasMore,
                    hangoutsHasMore = hangoutsPageResult.hasMore,
                    isOwnProfile = !isOther,
                    navigationTitle = if (isOther) LanguageManager.string(R.string.profile_about_user) else LanguageManager.string(R.string.profile_title)
                )
            )
            // Only the signed-in user's own card belongs on the home screen; opening
            // someone else's profile must not repaint the widget with their details.
            if (!isOther) {
                dependencies.widgetPublisher.publish(base.userCardModel, myId)
            }
        }
        return listOf(results.user, results.clubs, results.events, results.hangouts)
            .firstNotNullOfOrNull { it.exceptionOrNull() }
    }

    // ── Paging ────────────────────────────────────────────────────────────────

    private fun loadMoreClubs() {
        if (isLoadingMoreClubs || !state.clubsHasMore) return
        isLoadingMoreClubs = true
        val nextPage = clubsPage + 1
        viewModelScope.launch {
            try {
                val result = dependencies.useCase.getMyClubs(inputData.userId, nextPage, PAGE_SIZE)
                clubsPage = result.page
                val ui = state.uiModel ?: return@launch
                updateState(
                    state.copy(
                        uiModel = ui.copy(clubs = appendPage(ui.clubs, result.items) { it.id }),
                        clubsHasMore = result.hasMore
                    )
                )
            } catch (e: Throwable) {
                // Stop paging rather than retry-looping the loader on every scroll.
                updateState(state.copy(clubsHasMore = false))
                postEffect(ProfileDetailSideEffect.Error(e.userMessage()))
            } finally {
                isLoadingMoreClubs = false
            }
        }
    }

    private fun loadMoreEvents() {
        if (isLoadingMoreEvents || !state.eventsHasMore) return
        isLoadingMoreEvents = true
        val nextPage = eventsPage + 1
        viewModelScope.launch {
            try {
                val result = dependencies.useCase.getMyEvents(nextPage, PAGE_SIZE)
                eventsPage = result.page
                val ui = state.uiModel ?: return@launch
                updateState(
                    state.copy(
                        uiModel = ui.copy(events = appendPage(ui.events, result.items) { it.id }),
                        eventsHasMore = result.hasMore
                    )
                )
            } catch (e: Throwable) {
                updateState(state.copy(eventsHasMore = false))
                postEffect(ProfileDetailSideEffect.Error(e.userMessage()))
            } finally {
                isLoadingMoreEvents = false
            }
        }
    }

    private fun loadMoreHangouts() {
        if (isLoadingMoreHangouts || !state.hangoutsHasMore) return
        isLoadingMoreHangouts = true
        val nextPage = hangoutsPage + 1
        viewModelScope.launch {
            try {
                val result = dependencies.useCase.getMyHangouts(inputData.userId, nextPage, PAGE_SIZE)
                hangoutsPage = result.page
                val ui = state.uiModel ?: return@launch
                updateState(
                    state.copy(
                        uiModel = ui.copy(hangouts = appendPage(ui.hangouts, result.items) { it.id }),
                        hangoutsHasMore = result.hasMore
                    )
                )
            } catch (e: Throwable) {
                updateState(state.copy(hangoutsHasMore = false))
                postEffect(ProfileDetailSideEffect.Error(e.userMessage()))
            } finally {
                isLoadingMoreHangouts = false
            }
        }
    }

    /**
     * Appends a page, dropping rows already on screen. The server re-sorts by
     * `modifiedAt`, so a row can shift across the page boundary and arrive twice —
     * and a duplicate key crashes LazyColumn.
     */
    private fun <T, ID> appendPage(
        existing: List<T>,
        newItems: List<T>,
        id: (T) -> ID
    ): List<T> {
        val seen = existing.mapTo(mutableSetOf(), id)
        return existing + newItems.filter { seen.add(id(it)) }
    }

    /** Optimistically updates the cover, then persists it (PUT /users) and confirms via snackbar. */
    private fun applyUserCardCover(backgroundType: AppUIEntities.BackgroundType?) {
        val ui = state.uiModel ?: return
        updateState(
            state.copy(
                uiModel = ui.copy(
                    userCardModel = ui.userCardModel.copy(backgroundCover = backgroundType)
                )
            )
        )

        viewModelScope.launch {
            // Resend the full profile (mirrors iOS) so other fields aren't wiped by the PUT.
            val request = ProfileUpdateRequest(
                birthDate = ui.birthday,
                gender = ui.gender?.let { Gender.from(it)?.name },
                about = ui.about,
                categoriesId = ui.tags.map { it.id },
                languagesId = ui.languages?.map { it.id } ?: emptyList(),
                backgroundColour = backgroundType?.toRequestString()
            )
            runCatching { dependencies.useCase.editProfile(request, null) }
                .onSuccess {
                    // Only now, not optimistically: a rejected PUT would otherwise leave
                    // the home screen wearing a cover the server never accepted, with no
                    // in-app screen showing it.
                    dependencies.widgetPublisher.publish(
                        ui.userCardModel.copy(backgroundCover = backgroundType),
                        dependencies.tokenManager.getUserId()
                    )
                    AppSnackBar.show(
                        title = LanguageManager.string(R.string.profile_cover_updated),
                        subtitle = LanguageManager.string(R.string.profile_changes_saved),
                        style = AppSnackBar.Style.SUCCESS
                    )
                }
                .onFailure {
                    AppSnackBar.show(
                        title = "Couldn't update cover",
                        subtitle = LanguageManager.string(DesignR.string.common_try_again),
                        style = AppSnackBar.Style.ERROR
                    )
                }
        }
    }

    // Backend enum = colour names (iOS BackgroundType raw values), not Primary/Secondary.
    private fun AppUIEntities.BackgroundType.toRequestString(): String = apiValue

    private companion object {
        const val PAGE_SIZE = 10
    }
}
