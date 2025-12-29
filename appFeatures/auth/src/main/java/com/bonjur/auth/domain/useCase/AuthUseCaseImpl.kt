package com.bonjur.auth.domain.useCase

import androidx.compose.ui.input.key.Key.Companion.I
import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.data.dataSource.AuthDataSource
import com.bonjur.auth.domain.models.ChooseUniversityUIModel
import com.bonjur.auth.domain.models.OnboardingUIModel
import com.bonjur.auth.domain.models.RegisterModel
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.designSystem.ui.theme.image.Images
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    val dataSource: AuthDataSource
): AuthUseCase {
    override suspend fun register(
        request: RegisterRequest
    ): RegisterModel {
        val response = dataSource.register(
            request
        )
        return RegisterModel(
            response.token,
            response.refreshToken
        )
    }

    override fun onboarding(): List<OnboardingUIModel> {
        return listOf(
            OnboardingUIModel(
                id = "1",
                title = "Find Your \nPeople",
                subtitle = "Join your university community and start connecting with like-minded friends.",
                image = { Images.Icons.bigGraduationHat() }
            ),
            OnboardingUIModel(
                id = "2",
                title = "Chat Your \nWay",
                subtitle = "Send messages and share idea instantly, all through your favorite apps.",
                image = { Images.Icons.bigLamps() }
            ),
            OnboardingUIModel(
                id = "3",
                title = "Make It \nYours",
                subtitle = "Customize your app style and enjoy conversations your way.",
                image = { Images.Icons.bigPeopleGroups() }
            )
        )
    }

    override suspend fun chooseUniversity(): List<ChooseUniversityUIModel> {
        return listOf(
            ChooseUniversityUIModel(
                id = "1",
                title = "UFAZ",
                selected = false
            ),
            ChooseUniversityUIModel(
                id = "1",
                title = "BHOS",
                selected = false
            ),
            ChooseUniversityUIModel(
                id = "1",
                title = "ADNSU",
                selected = false
            )
        )
    }

    override fun welcome(name: String): OnboardingUIModel {
        return OnboardingUIModel(
            id = "1",
            title = "Welcome $name",
            subtitle = "Complete your profile for better interaction.It will take only 5 minutes.",
            image = { Images.Icons.resume() },
        )
    }

    override fun genders(): List<SelectableListItemModel> {
        return listOf(
            SelectableListItemModel(
                title = "Male",
                selected = false
            ),
            SelectableListItemModel(
                title = "Female",
                selected = false
            )
        )
    }

    override fun languages(): List<SelectableListItemModel> {
        return listOf(
            SelectableListItemModel(
                title = "Azerbaijan",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                title = "English",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                title = "Female",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                title = "Russian",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                title = "French",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                title = "Turkish",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            )
        )
    }
}