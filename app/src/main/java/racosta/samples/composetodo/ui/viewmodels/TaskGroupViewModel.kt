package racosta.samples.composetodo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import racosta.samples.composetodo.commons.launchInScope
import racosta.samples.composetodo.todologic.entities.NewTask
import racosta.samples.composetodo.todologic.entities.Task
import racosta.samples.composetodo.todologic.entities.TasksGroupDetailed
import racosta.samples.composetodo.todologic.usecases.AddNewTasksUseCase
import racosta.samples.composetodo.todologic.usecases.GetTaskGroupDetailedForIdUseCase
import racosta.samples.composetodo.todologic.usecases.UpdateTaskUseCase
import racosta.samples.composetodo.ui.screens.TaskGroupScreen
import racosta.samples.composetodo.ui.viewmodels.base.NavigatorViewModel

class TaskGroupViewModel(
    private val tasksGroupId: Long?,
    private val getTaskGroupDetailedForIdUseCase: GetTaskGroupDetailedForIdUseCase,
    private val addNewTasksUseCase: AddNewTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : NavigatorViewModel(), TaskGroupScreen.State, TaskGroupScreen.UserEvents {

    override val groupName = MutableStateFlow("")
    override val taskGroup = MutableStateFlow<TasksGroupDetailed?>(null)

    init {
        launchInScope {
            if (tasksGroupId == null) {
                groupName.value = "Tasks"
            }

            getTaskGroupDetailedForIdUseCase.taskGroupDetailed(tasksGroupId).collect {
                taskGroup.value = it
                groupName.value = it.name ?: "Tasks"
            }
        }
    }

    override fun onAddSomethingClick() {
        launchInScope {
            addNewTasksUseCase.addNewTask(NewTask("TITLE", groupId = tasksGroupId))
        }
    }

    override fun onTaskCheckClick(task: Task) {
        launchInScope {
            updateTaskUseCase.updateTask(task.copy(isDone = !task.isDone))
        }
    }

    class Factory(
        private val tasksGroupId: Long?,
        private val getTaskGroupDetailedForIdUseCase: GetTaskGroupDetailedForIdUseCase,
        private val addNewTasksUseCase: AddNewTasksUseCase,
        private val updateTaskUseCase: UpdateTaskUseCase,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TaskGroupViewModel(tasksGroupId, getTaskGroupDetailedForIdUseCase, addNewTasksUseCase, updateTaskUseCase) as T
        }
    }
}