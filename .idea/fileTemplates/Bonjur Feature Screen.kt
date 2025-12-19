#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
#parse("File Header.java")
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bonjur.appfoundation.FeatureScreen

@Composable
fun ${NAME}Screen(
inputData: ${NAME}InputData,
viewModel: ${NAME}ViewModel = hiltViewModel()
) {
    LaunchedEffect(inputData) {
        viewModel.init(inputData)
    }

    FeatureScreen(
        viewModel = viewModel,
        handleEffect = { effect ->
            when (effect) {
                is ${NAME}SideEffect.Loading -> {
                // Handle loading effect
            }
            }
        }
    ) { store ->
        ${NAME}View(store = store)
    }
}