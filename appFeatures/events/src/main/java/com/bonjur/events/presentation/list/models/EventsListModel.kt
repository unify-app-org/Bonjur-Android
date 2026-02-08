//
//  EventsListModels.kt
//  Events
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.events.presentation.list.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.network.model.ApiException

// MARK: - Input Data
data class EventsListInputData(
    val initialValue: String = ""
)

// MARK: - Side Effects
sealed class EventsListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : EventsListSideEffect()
    data class Error(val error: ApiException) : EventsListSideEffect()
}

// MARK: - View State
data class EventsListViewState(
    val uiModel: UIModel = UIModel()
) : FeatureState {
    data class UIModel(
        val events: List<EventsCardModel> = emptyList(),
        val filters: List<FilterView.Model> = emptyList(),
        val searchText: String = ""
    )
}

// MARK: - Actions
sealed class EventsListAction : FeatureAction {
    object FetchData : EventsListAction()
    object Dismiss: EventsListAction()
    data class SearchTextChanged(val text: String) : EventsListAction()
    data class FilterSelected(val items: List<FilterView.Items>) : EventsListAction()
    data class EventItemTapped(val id: String): EventsListAction()
}