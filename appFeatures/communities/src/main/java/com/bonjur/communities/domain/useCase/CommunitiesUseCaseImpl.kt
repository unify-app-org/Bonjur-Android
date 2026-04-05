package com.bonjur.communities.domain.useCase

import com.bonjur.communities.presentation.list.model.CommunityCardMocks
import com.bonjur.communities.presentation.list.model.CommunityCardModel
import com.bonjur.communities.data.dataSource.CommunitiesDataSource
import com.bonjur.communities.domain.model.CommunityDetails
import com.bonjur.designSystem.components.filter.FilterView
import com.bonjur.designSystem.components.filter.FilterViewMocks
import javax.inject.Inject

class CommunitiesUseCaseImpl @Inject constructor(
    val dataSource: CommunitiesDataSource
) : CommunitiesUseCase {

    override suspend fun fetchCommunitiesData(): List<CommunityCardModel> {
        return CommunityCardMocks.mock
    }

    override suspend fun fetchFilterData(): List<FilterView.Model> {
        return FilterViewMocks.mockData
    }

    override suspend fun fetchCommunityDetails(communityId: Int): CommunityDetails.UIModel {
        return CommunityDetails.UIModel.mock
    }
}
