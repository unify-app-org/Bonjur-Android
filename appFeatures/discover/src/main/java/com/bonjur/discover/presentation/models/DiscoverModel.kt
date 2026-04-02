//
//  DiscoverModels.kt
//  Discover
//
//  Created by Huseyn Hasanov on 11.01.26
//

package com.bonjur.discover.presentation.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.clubs.presentation.list.models.ClubCardModel
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.communities.CommunityCardModel
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.discover.domain.models.UserModel
import com.bonjur.events.presentation.list.models.EventsCardModel
import com.bonjur.hangouts.presentation.list.model.HangoutsCardModel
import com.bonjur.network.model.ApiException

// MARK: - Input Data
data class DiscoverInputData(
    val initialValue: String = "",
    val onTabChange: (() -> Unit)? = null
)

// MARK: - Side Effects
sealed class DiscoverSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : DiscoverSideEffect()
    data class Error(val error: ApiException) : DiscoverSideEffect()
}

// MARK: - View State
data class DiscoverViewState(
    val uiModel: UIModel = UIModel()
) : FeatureState {
    data class UIModel(
        val user: UserModel = UserModel(
            id = 1,
            name = "",
            profileImage = null,
            greeting = ""
        ),
        val filters: List<FilterView.Model> = emptyList(),
        val communities: List<CommunityCardModel> = emptyList(),
        val clubs: List<ClubCardModel> = emptyList(),
        val events: List<EventsCardModel> = emptyList(),
        val hangouts: List<HangoutsCardModel> = emptyList()
    )
}

// MARK: - Actions
sealed class DiscoverAction : FeatureAction {
    object FetchData : DiscoverAction()
    data class ViewAllTapped(val type: AppUIEntities.ActivityType) : DiscoverAction()
    data class CLubItemTapped(val clubId: Int) : DiscoverAction()
    data class EventItemTapped(val eventId: String) : DiscoverAction()

}