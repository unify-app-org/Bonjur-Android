package com.bonjur.profile.presentation.detail.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.clubs.presentation.list.models.ClubCardMocks
import com.bonjur.clubs.presentation.list.models.ClubCardModel

// MARK: - Input Data
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.components.segmentView.SegmentedPickerOption
import com.bonjur.events.presentation.list.models.EventsCardMocks
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardMocks
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel

// MARK: - Input Data
data class ProfileDetailInputData(
    val dummy: Unit = Unit
)

// MARK: - Side Effects
sealed class ProfileDetailSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ProfileDetailSideEffect()
}

// MARK: - View State
data class ProfileDetailViewState(
    val uiModel: ProfileDetail.UIModel? = null,
    val selectedSegment: SegmentTypes = SegmentTypes.CLUBS
) : FeatureState {

    enum class SegmentTypes(
        override val title: String
    ): SegmentedPickerOption {
        CLUBS("Clubs"),
        EVENTS("Events"),
        HANGOUTS("Hangouts");

        override val id: String get() = name

        companion object {
            fun fromIndex(index: Int): SegmentTypes {
                return when (index) {
                    0 -> SegmentTypes.CLUBS
                    1 -> SegmentTypes.EVENTS
                    2 -> SegmentTypes.HANGOUTS
                    else -> SegmentTypes.CLUBS
                }
            }
        }

        fun toIndex(): Int {
            return when (this) {
                SegmentTypes.CLUBS -> 0
                SegmentTypes.EVENTS -> 1
                SegmentTypes.HANGOUTS -> 2
            }
        }
    }
}

object ProfileDetail {
    data class UIModel(
        val userCardModel: UserCardModel = UserCardModel(),
        val about: String? = null,
        val gender: String? = null,
        val birthday: String? = null,
        val languages: List<String>? = null,
        val tags: List<AppUIEntities.Tags> = emptyList(),
        val cardCover: AppUIEntities.BackgroundType? = null,
        val clubs: List<ClubCardModel> = emptyList(),
        val events: List<EventsCardModel> = emptyList(),
        val hangouts: List<HangoutsCardModel> = emptyList()
    ) {
        companion object {
        }
    }
}


fun ProfileDetail.UIModel.Companion.mock(): ProfileDetail.UIModel = ProfileDetail.UIModel(
    userCardModel = UserCardModel.mock.first(),
    about = "I want to have a coffee and then go to the film I have one free ticket to the concert for the Sunday evening if someone want just contact.",
    gender = "Male",
    birthday = null,
    languages = null,
    tags = listOf(
        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Messi"),
        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Ronaldo"),
        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Ronaldinho"),
        AppUIEntities.Tags(id = 1, type = "SPORT", title = "Basketball")
    ),
    cardCover = AppUIEntities.BackgroundType.Tertiary,
    clubs = ClubCardMocks.previewData,
    events = EventsCardMocks.previewMock,
    hangouts = HangoutsCardMocks.previewMock
)

// MARK: - Actions
sealed class ProfileDetailAction : FeatureAction {
    object FetchData : ProfileDetailAction()
    data class ClubsItemTapped(val id: Int) : ProfileDetailAction()
    data class SegmentTapped(val segment: ProfileDetailViewState.SegmentTypes) : ProfileDetailAction()
    data class EventsItemTapped(val id: String) : ProfileDetailAction()
    data class HangoutsItemTapped(val id: String) : ProfileDetailAction()
    object SettingsTapped : ProfileDetailAction()
    object UserCardTapped : ProfileDetailAction()
    data class UserCardCoverSaved(val backgroundType: AppUIEntities.BackgroundType?) : ProfileDetailAction()
}