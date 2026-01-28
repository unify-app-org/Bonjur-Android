//
//  HangoutsListModels.kt
//  Hangouts
//
//  Created by Huseyn Hasanov on 22.01.26
//

package com.bonjur.hangouts.presentation.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.network.model.ApiException

// MARK: - Input Data
data class HangoutsListInputData(
    val initialValue: String = ""
)

// MARK: - Side Effects
sealed class HangoutsListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : HangoutsListSideEffect()
    data class Error(val error: ApiException) : HangoutsListSideEffect()
}

// MARK: - View State
data class HangoutsListViewState(
    val uiModel: UIModel = UIModel()
) : FeatureState {
    data class UIModel(
        val hangouts: List<HangoutsCardModel> = emptyList(),
        val filters: List<FilterView.Model> = emptyList(),
        val searchText: String = ""
    )
}

// MARK: - Actions
sealed class HangoutsListAction : FeatureAction {
    object FetchData : HangoutsListAction()
    object Dismiss : HangoutsListAction()
    data class SearchTextChanged(val text: String) : HangoutsListAction()
    data class FilterSelected(val items: List<FilterView.Items>) : HangoutsListAction()
}