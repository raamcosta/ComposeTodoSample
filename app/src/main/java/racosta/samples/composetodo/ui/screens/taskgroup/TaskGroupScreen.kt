package racosta.samples.composetodo.ui.screens.taskgroup

import androidx.compose.runtime.Composable
import com.google.gson.reflect.TypeToken
import racosta.samples.composetodo.commons.navigatorViewModel
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.screens.base.ScreenWithArguments
import racosta.samples.composetodo.ui.screens.base.ScreenComposable
import java.lang.reflect.Type

object TaskGroupScreen : ScreenWithArguments<TaskGroupArguments>() {

    override val name = "taskGroup"

    override val argType: Type = object: TypeToken<TaskGroupArguments>() {}.type

    @Composable
    override fun makeComposable(arguments: TaskGroupArguments, compositionRoot: ScreenCompositionRoot): ScreenComposable {
        val viewModel: TaskGroupViewModel = navigatorViewModel(
            navigator = compositionRoot.navigator,
            factory = TaskGroupViewModel.Factory(
                arguments.taskGroupId,
                compositionRoot.getTaskGroupDetailedForIdUseCase,
                compositionRoot.addNewTasksUseCase,
                compositionRoot.updateTaskUseCase
            )
        )

        return TaskGroupComposable(viewModel.uiState, viewModel)
    }
}