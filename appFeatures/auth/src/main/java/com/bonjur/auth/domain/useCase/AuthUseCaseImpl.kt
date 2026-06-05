package com.bonjur.auth.domain.useCase

import android.os.Build
import com.bonjur.auth.data.DTOs.LoginRequest
import com.bonjur.auth.data.dataSource.AuthDataSource
import com.bonjur.auth.domain.models.AuthInterestsModel
import com.bonjur.auth.domain.models.OnboardingUIModel
import com.bonjur.designSystem.components.categorieChips.CategoriesChipModel
import com.bonjur.designSystem.components.selectableList.SelectableListItemModel
import com.bonjur.designSystem.ui.theme.image.Images
import com.bonjur.network.manager.TokenManager
import com.bonjur.storage.defaultPreference.DefaultStorage
import com.bonjur.storage.defaultPreference.DefaultStorageKey
import java.util.UUID
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    val dataSource: AuthDataSource,
    val tokenManager: TokenManager,
    val defaultStorage: DefaultStorage
) : AuthUseCase {

    override suspend fun login(communityId: Int, email: String, password: String?): Boolean {
        val response = dataSource.login(
            LoginRequest(
                mail = email,
                password = password,
                communityId = communityId,
                deviceId = UUID.randomUUID().toString(),
                devicePlatform = "ANDROID",
                deviceOs = "Android ${Build.VERSION.RELEASE}",
                deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
                appVersion = "1.0.0"
            )
        )
        tokenManager.saveAccessToken(response.accessToken)
        tokenManager.saveRefreshToken(response.refreshToken)
        tokenManager.saveUserId(response.userId)
        defaultStorage.saveInt(DefaultStorageKey.COMMUNITY_ID, communityId)
        defaultStorage.saveBoolean(DefaultStorageKey.IS_AUTHENTICATED, true)
        return response.isFirstLogin
    }

    override fun onboarding(): List<OnboardingUIModel> = listOf(
        OnboardingUIModel(
            id = "1",
            title = "Find Your \nPeople",
            subtitle = "Join your university community and start connecting with like-minded friends.",
            image = { Images.Icons.bigGraduationHat() }
        ),
        OnboardingUIModel(
            id = "2",
            title = "Chat Your \nWay",
            subtitle = "Send messages and share ideas instantly, all through your favorite apps.",
            image = { Images.Icons.bigLamps() }
        ),
        OnboardingUIModel(
            id = "3",
            title = "Make It \nYours",
            subtitle = "Customize your app style and enjoy conversations your way.",
            image = { Images.Icons.bigPeopleGroups() }
        )
    )

    override fun welcome(name: String): OnboardingUIModel = OnboardingUIModel(
        id = "1",
        title = "Welcome $name",
        subtitle = "Complete your profile for better interaction. It will take only 5 minutes.",
        image = { Images.Icons.resume() }
    )

    override suspend fun chooseUniversity(): List<SelectableListItemModel> {
        return try {
            dataSource.getCommunities().map { community ->
                SelectableListItemModel(
                    id = community.id,
                    title = community.name,
                    selected = false
                )
            }
        } catch (e: Exception) {
            print(e)
            listOf(
                SelectableListItemModel(id = 1, title = "UFAZ", selected = false),
                SelectableListItemModel(id = 2, title = "BHOS", selected = false),
                SelectableListItemModel(id = 3, title = "ADNSU", selected = false)
            )
        }
    }

    override suspend fun genders(): List<SelectableListItemModel> = listOf(
        SelectableListItemModel(id = 1, title = "Male", selected = false),
        SelectableListItemModel(id = 2, title = "Female", selected = false)
    )

    override suspend fun languages(): List<SelectableListItemModel> {
        return try {
            dataSource.getLanguages().map { lang ->
                SelectableListItemModel(
                    id = lang.id,
                    title = lang.name,
                    selected = false,
                    style = SelectableListItemModel.Style.MultiSelect
                )
            }
        } catch (e: Exception) {
            listOf(
                SelectableListItemModel(id = 1, title = "Azerbaijani", selected = false, style = SelectableListItemModel.Style.MultiSelect),
                SelectableListItemModel(id = 2, title = "English", selected = false, style = SelectableListItemModel.Style.MultiSelect),
                SelectableListItemModel(id = 3, title = "Russian", selected = false, style = SelectableListItemModel.Style.MultiSelect),
                SelectableListItemModel(id = 4, title = "French", selected = false, style = SelectableListItemModel.Style.MultiSelect)
            )
        }
    }

    override suspend fun interests(): List<AuthInterestsModel> {
        return try {
            val categories = dataSource.getCategories()
            listOf(
                AuthInterestsModel(
                    title = "Categories",
                    interests = categories.map { cat ->
                        CategoriesChipModel(id = cat.id, title = cat.title, selected = false)
                    }
                )
            )
        } catch (e: Exception) {
            listOf(
                AuthInterestsModel(
                    title = "Categories",
                    interests = listOf(
                        CategoriesChipModel(id = 1, title = "sport", selected = false),
                        CategoriesChipModel(id = 2, title = "fashion", selected = false),
                        CategoriesChipModel(id = 3, title = "music", selected = false),
                        CategoriesChipModel(id = 4, title = "art", selected = false),
                        CategoriesChipModel(id = 5, title = "food", selected = false)
                    )
                )
            )
        }
    }
}
