package com.bonjur.auth.domain.useCase

import androidx.compose.ui.input.key.Key.Companion.I
import com.bonjur.auth.data.DTOs.RegisterRequest
import com.bonjur.auth.data.dataSource.AuthDataSource
import com.bonjur.auth.domain.models.AuthInterestsModel
import com.bonjur.auth.domain.models.OnboardingUIModel
import com.bonjur.auth.domain.models.RegisterModel
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
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

    override suspend fun chooseUniversity(): List<SelectableListItemModel> {
        return listOf(
            SelectableListItemModel(
                id = 1,
                title = "UFAZ",
                selected = false
            ),
            SelectableListItemModel(
                id = 2,
                title = "BHOS",
                selected = false
            ),
            SelectableListItemModel(
                id = 3,
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

    override suspend fun genders(): List<SelectableListItemModel> {
        return listOf(
            SelectableListItemModel(
                id = 1,
                title = "Male",
                selected = false
            ),
            SelectableListItemModel(
                id = 2,
                title = "Female",
                selected = false
            )
        )
    }

    override suspend fun languages(): List<SelectableListItemModel> {
        return listOf(
            SelectableListItemModel(
                id = 1,
                title = "Azerbaijan",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                id = 2,
                title = "English",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                id = 3,
                title = "Russian",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                id = 4,
                title = "French",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            ),
            SelectableListItemModel(
                id = 5,
                title = "Turkish",
                selected = false,
                style = SelectableListItemModel.Style.MultiSelect
            )
        )
    }

    override suspend fun interests(): List<AuthInterestsModel> {
        return listOf(
            AuthInterestsModel(
                title = "Example",
                interests = listOf(
                    CategoriesChipModel(
                        id = 1,
                        title = "love",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 2,
                        title = "beautiful",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 3,
                        title = "fashion",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 4,
                        title = "dog",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 5,
                        title = "art",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 6,
                        title = "cat",
                        selected = false
                    )
                )
            ),
            AuthInterestsModel(
                title = "Example",
                interests = listOf(
                    CategoriesChipModel(
                        id = 7,
                        title = "love",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 8,
                        title = "beautiful",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 9,
                        title = "fashion",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 10,
                        title = "dog",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 11,
                        title = "art",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 12,
                        title = "cat",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 13,
                        title = "beautiful",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 14,
                        title = "fashion",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 15,
                        title = "dog",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 16,
                        title = "art",
                        selected = false
                    ),
                    CategoriesChipModel(
                        id = 17,
                        title = "cat",
                        selected = false
                    )
                )
            )
        )
    }
}