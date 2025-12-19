#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
#parse("File Header.java")

import androidx.lifecycle.viewModelScope
import com.bonjur.navigation.Navigator
import com.bonjur.appfoundation.FeatureViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ${NAME}ViewModel @Inject constructor(
private val navigator: Navigator
) : FeatureViewModel<${NAME}ViewState, ${NAME}Action, ${NAME}SideEffect>(
${NAME}ViewState()
) {
    private lateinit var inputData: ${NAME}InputData

    fun init(inputData: ${NAME}InputData) {
        if (::inputData.isInitialized) return
        this.inputData = inputData
    }

    override fun handle(action: ${NAME}Action) {
        when (action) {
                ${NAME}Action.Example -> {
            // Handle action
        }
        }
    }

    private fun navigate(to: String) {
        viewModelScope.launch {
            navigator.navigateTo(to)
        }
    }
}