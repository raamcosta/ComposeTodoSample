package racosta.samples.composetodo.ui.screens.taskgroup

import androidx.compose.runtime.Composable
import com.google.gson.reflect.TypeToken
import racosta.samples.composetodo.commons.navigatorViewModel
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.screens.base.ScreenWithArgumentsDefinition
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.viewmodels.TaskGroupViewModel
import java.lang.reflect.Type

object TaskGroupScreenDefinition : ScreenWithArgumentsDefinition<TaskGroupArguments> {

    override val name = "taskGroup"

    override val argType: Type?
        get() = object: TypeToken<TaskGroupArguments>() {}.type

    @Composable
    override fun prepareScreen(arguments: TaskGroupArguments, compositionRoot: ScreenCompositionRoot): Screen {
        val viewModel: TaskGroupViewModel = navigatorViewModel(
            navigator = compositionRoot.navigator,
            factory = TaskGroupViewModel.Factory(
                arguments.taskGroupId,
                compositionRoot.getTaskGroupDetailedForIdUseCase,
                compositionRoot.addNewTasksUseCase,
                compositionRoot.updateTaskUseCase
            )
        )

        return TaskGroupScreen(viewModel.uiState, viewModel)
    }
}