package com.bonjur.communities.presentation.list.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.network.model.ApiException

data class CommunitiesListInputData(
    val initialValue: String = ""
)

sealed class CommunitiesListSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : CommunitiesListSideEffect()
    data class Error(val error: ApiException) : CommunitiesListSideEffect()
}

data class CommunitiesListViewState(
    val uiModel: UIModel = UIModel()
) : FeatureState {
    data class UIModel(
        val communities: List<CommunityCardModel> = emptyList(),
        val filters: List<FilterView.Model> = emptyList(),
        val searchText: String = ""
    )
}

sealed class CommunitiesListAction : FeatureAction {
    data class CommunityItemTapped(val id: Int) : CommunitiesListAction()
    object FetchData : CommunitiesListAction()
    data class SearchTextChanged(val text: String) : CommunitiesListAction()
    data class FilterSelected(val items: List<FilterView.Items>) : CommunitiesListAction()
}
