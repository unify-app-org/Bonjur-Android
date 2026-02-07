//
//  ClubsListModels.kt
//  Clubs
//
//  Created by Huseyn Hasanov on 17.01.26
//

package com.bonjur.clubs.presentation.list.models

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.network.model.ApiException

// MARK: - Input Data
data class ClubsListInputData(
    val initialValue: String = ""
)

// MARK: - Side Effects
sealed class ClubsListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ClubsListSideEffect()
    data class Error(val error: ApiException) : ClubsListSideEffect()
}

// MARK: - View State
data class ClubsListViewState(
    val uiModel: UIModel = UIModel()
) : FeatureState {
    data class UIModel(
        val clubs: List<ClubCardModel> = emptyList(),
        val filters: List<FilterView.Model> = emptyList(),
        val searchText: String = ""
    )
}

// MARK: - Actions
sealed class ClubsListAction : FeatureAction {
    data class clubItemTapped(val id: Int) : ClubsListAction()

    object FetchData : ClubsListAction()
    data class SearchTextChanged(val text: String) : ClubsListAction()
    data class FilterSelected(val items: List<FilterView.Items>) : ClubsListAction()
}