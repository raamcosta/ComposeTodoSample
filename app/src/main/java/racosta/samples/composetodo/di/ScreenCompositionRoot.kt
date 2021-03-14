package racosta.samples.composetodo.di

import racosta.samples.composetodo.todologic.usecases.AddNewTasksUseCase
import racosta.samples.composetodo.todologic.usecases.GetAllTaskGroupsUseCase
import racosta.samples.composetodo.todologic.usecases.GetTaskGroupDetailedForIdUseCase
import racosta.samples.composetodo.todologic.usecases.UpdateTaskUseCase
import racosta.samples.composetodo.ui.navigator.Navigator

class ScreenCompositionRoot(
    appCompositionRoot: AppCompositionRoot,
    val navigator: Navigator
) : AppScope by appCompositionRoot {

    val updateTaskUseCase: UpdateTaskUseCase get() = UpdateTaskUseCase(db.taskDao())

    val getAllTasksGroupsUseCase get() = GetAllTaskGroupsUseCase(db.taskDao(), db.taskGroupDao())

    val addNewTasksUseCase get() = AddNewTasksUseCase(db.taskDao(), db.taskGroupDao())

    val getTaskGroupDetailedForIdUseCase get() = GetTaskGroupDetailedForIdUseCase(db.taskDao(), db.taskGroupDao())
}