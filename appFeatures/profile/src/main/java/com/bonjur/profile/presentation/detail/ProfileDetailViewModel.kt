package com.bonjur.profile.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bonjur.appfoundation.FeatureViewModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import com.bonjur.profile.domain.usecase.ProfileUseCase
import com.bonjur.profile.navigation.ProfileScreens
import com.bonjur.profile.presentation.detail.models.ProfileDetailAction
import com.bonjur.profile.presentation.detail.models.ProfileDetailInputData
import com.bonjur.profile.presentation.detail.models.ProfileDetailSideEffect
import com.bonjur.profile.presentation.detail.models.ProfileDetailViewState
import com.bonjur.profile.presentation.studentCard.models.StudentCardInputData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val dependencies: Dependencies
) : FeatureViewModel<ProfileDetailViewState, ProfileDetailAction, ProfileDetailSideEffect>(
    ProfileDetailViewState()
) {

    data class Dependencies @Inject constructor(
        val useCase: ProfileUseCase
    )

    private lateinit var inputData: ProfileDetailInputData
    private lateinit var navigator: Navigator

    fun init(inputData: ProfileDetailInputData, navigator: Navigator) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
        this.navigator = navigator
        fetchData()
    }

    override fun handle(action: ProfileDetailAction) {
        when (action) {
            ProfileDetailAction.FetchData -> fetchData()

            is ProfileDetailAction.ClubsItemTapped -> viewModelScope.launch {
//                navigator.navigateTo(ProfileScreens.ClubsDetails(action.id).route)
            }

            is ProfileDetailAction.SegmentTapped -> viewModelScope.launch {
                handleSegmentChanged(action.segment)
            }

            is ProfileDetailAction.EventsItemTapped -> viewModelScope.launch {
//                navigator.navigateTo(ProfileScreens.EventsDetails(action.id).route)
            }

            is ProfileDetailAction.HangoutsItemTapped -> viewModelScope.launch {
//                navigator.navigateTo(ProfileScreens.HangoutsDetails(action.id).route)
            }

            ProfileDetailAction.SettingsTapped -> viewModelScope.launch {
//                navigator.navigateTo(ProfileScreens.Settings.route)
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

    private fun handleSegmentChanged(segment: ProfileDetailViewState.SegmentTypes) {
        updateState(state.copy(selectedSegment = segment))
    }

    private fun fetchData() {
        viewModelScope.launch {
            fetchUserData()
        }
    }

    private suspend fun fetchUserData() {
        try {
            val uiModel = dependencies.useCase.fetchProfileData()
            updateState(state.copy(uiModel = uiModel))
        } catch (e: Exception) {
            // Handle error
        }
    }

    private fun applyUserCardCover(backgroundType: AppUIEntities.BackgroundType?) {
        val currentUIModel = state.uiModel ?: return

        val updatedUserCardModel = currentUIModel.userCardModel.copy(
            backgroundCover = backgroundType
        )

        val updatedUIModel = currentUIModel.copy(
            userCardModel = updatedUserCardModel,
            cardCover = backgroundType
        )

        updateState(state.copy(uiModel = updatedUIModel))
    }
}