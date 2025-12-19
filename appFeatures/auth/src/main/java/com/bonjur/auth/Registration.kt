package com.bonjur.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonjur.auth.navigation.AuthScreens
import com.bonjur.designSystem.ui.theme.BonjurTheme
import com.bonjur.navigation.Navigator
import com.bonjur.navigation.route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Composable
fun RegistrationGreeting(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: RegistrationVM = hiltViewModel()
) {
    Column() {
        Text(
            text = "Hello $name!",
            modifier = modifier
                .clickable {
                    viewModel.forgetClicked(
                        "husein10.hasanov@gmail.com"
                    )
                },
        )

        Text(
            text = stringResource(R.string.common_save),
            modifier = modifier
                .clickable {
                    viewModel.backClicked()
                },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BonjurTheme {
        RegistrationGreeting("Android")
    }
}


@HiltViewModel
class RegistrationVM @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    fun forgetClicked(email: String) {
        viewModelScope.launch {
            navigator.navigateTo(AuthScreens.Test.route)
        }
    }

    fun backClicked() {
        viewModelScope.launch {
            navigator.popToRoute(AuthScreens.Test.route)
        }
    }
}

@Serializable
data class ForgotPass(
    val email: String
)