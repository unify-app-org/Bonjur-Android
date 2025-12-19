#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
#parse("File Header.java")
import com.bonjur.appfoundation.FeatureAction
import com.bonjur.appfoundation.FeatureState
import com.bonjur.appfoundation.SideEffect

// MARK: - Input Data
data class ${NAME}InputData(
val initialValue: String = ""
)

// MARK: - Side Effects
sealed class ${NAME}SideEffect : SideEffect {
    data class Loading(val isLoading: Boolean) : ${NAME}SideEffect()
}

// MARK: - View State
data class ${NAME}ViewState(
val title: String = "${NAME}"
) : FeatureState

// MARK: - Actions
sealed class ${NAME}Action : FeatureAction {
    object Example : ${NAME}Action()
}