package com.bonjur.auth.presentation.welcome.model

import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect
import com.bonjur.auth.domain.models.OnboardingUIModel
import com.bonjur.designSystem.ui.theme.image.Images
import kotlinx.serialization.Serializable

@Serializable
data class AuthWelcomeInputData(
    val name: String = ""
)

sealed class AuthWelcomeSideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : AuthWelcomeSideEffect()
}

data class AuthWelcomeViewState(
    val uiModel: OnboardingUIModel = OnboardingUIModel(
        id = "",
        title = "",
        subtitle = "",
        image = { Images.Icons.bigGraduationHat() }
    )
) : FeatureState

sealed class AuthWelcomeAction: FeatureAction {
    object FetchData : AuthWelcomeAction()
    object Dismiss : AuthWelcomeAction()
    object ContinueTapped : AuthWelcomeAction()
}
