package com.bonjur.profile.presentation.detail

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
import com.bonjur.profile.presentation.editProfile.models.EditProfileInputData
import com.bonjur.profile.presentation.editProfile.models.Gender
import com.bonjur.profile.presentation.studentCard.models.StudentCardInputData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ProfileDetailViewState, ProfileDetailAction, ProfileDetailSideEffect>(
    ProfileDetailViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ProfileUseCase,
        val tokenManager: TokenManager
    )

    private lateinit var inputData: ProfileDetailInputData
    private lateinit var navigator: Navigator

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

            is ProfileDetailAction.UserCardCoverSaved -> applyUserCardCover(action.backgroundType)
        }
    }

    /** Fetches profile + clubs + events + activities in parallel (mirrors iOS ProfileDetailViewModel). */
    private fun fetchData() {
        viewModelScope.launch {
            postEffect(ProfileDetailSideEffect.Loading(true))
            val userId = inputData.userId

            val results = coroutineScope {
                val user = async { runCatching { dependencies.useCase.fetchProfileData(userId) } }
                val clubs = async { runCatching { dependencies.useCase.getMyClubs(userId) } }
                // events/my is self-only — fetch only for own profile, hide for another user.
                val events = async {
                    if (userId.isNullOrEmpty()) runCatching { dependencies.useCase.getMyEvents() }
                    else Result.success(emptyList())
                }
                val hangouts = async { runCatching { dependencies.useCase.getMyHangouts(userId) } }
                FetchResults(user.await(), clubs.await(), events.await(), hangouts.await())
            }

            val firstError = applyResults(results)
            postEffect(ProfileDetailSideEffect.Loading(false))
            firstError?.let {
                postEffect(ProfileDetailSideEffect.Error(it.message ?: "Something went wrong", null))
            }
        }
    }

    private data class FetchResults(
        val user: Result<com.bonjur.profile.presentation.detail.models.ProfileDetail.UIModel>,
        val clubs: Result<List<com.bonjur.clubs.presentation.list.models.ClubCardModel>>,
        val events: Result<List<com.bonjur.events.presentation.list.models.EventsCardModel>>,
        val hangouts: Result<List<com.bonjur.hangouts.presentation.list.model.HangoutsCardModel>>
    )

    private suspend fun applyResults(results: FetchResults): Throwable? {
        val base = results.user.getOrNull()
        if (base != null) {
            val myId = dependencies.tokenManager.getUserId()
            val isOther = !inputData.userId.isNullOrEmpty() && inputData.userId != myId
            updateState(
                state.copy(
                    uiModel = base.copy(
                        clubs = results.clubs.getOrDefault(emptyList()),
                        events = results.events.getOrDefault(emptyList()),
                        hangouts = results.hangouts.getOrDefault(emptyList())
                    ),
                    isOwnProfile = !isOther,
                    navigationTitle = if (isOther) "About user" else "Profile"
                )
            )
        }
        return listOf(results.user, results.clubs, results.events, results.hangouts)
            .firstNotNullOfOrNull { it.exceptionOrNull() }
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
                    AppSnackBar.show(
                        title = "Cover updated successfully",
                        subtitle = "Your changes are saved",
                        style = AppSnackBar.Style.SUCCESS
                    )
                }
        }
    }

    // Backend enum = colour names (iOS BackgroundType raw values), not Primary/Secondary.
    private fun AppUIEntities.BackgroundType.toRequestString(): String = when (this) {
        is AppUIEntities.BackgroundType.Primary -> "GREEN"
        is AppUIEntities.BackgroundType.Secondary -> "BLUE"
        is AppUIEntities.BackgroundType.Tertiary -> "PURPLE"
        is AppUIEntities.BackgroundType.CustomColor -> when (colorType) {
            is AppUIEntities.ColorType.Orange -> "ORANGE"
            is AppUIEntities.ColorType.Red -> "RED"
            is AppUIEntities.ColorType.Pink -> "PINK"
            is AppUIEntities.ColorType.Custom -> "GREEN"
        }
    }
}
