package racosta.samples.composetodo.ui.screens.taskgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import racosta.samples.composetodo.todologic.entities.NewTask
import racosta.samples.composetodo.todologic.entities.Task
import racosta.samples.composetodo.todologic.usecases.AddNewTasksUseCase
import racosta.samples.composetodo.todologic.usecases.GetTaskGroupDetailedForIdUseCase
import racosta.samples.composetodo.todologic.usecases.UpdateTaskUseCase
import racosta.samples.composetodo.commons.NavigatorViewModel

class TaskGroupViewModel(
    private val tasksGroupId: Long?,
    private val getTaskGroupDetailedForIdUseCase: GetTaskGroupDetailedForIdUseCase,
    private val addNewTasksUseCase: AddNewTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : NavigatorViewModel(), TaskGroupScreenUserEvents {

    val uiState = MutableStateFlow(TaskGroupScreenState("", null, false, ""))

    init {
        viewModelScope.launch {
            if (tasksGroupId == null) {
                updateState { copy(groupName = "Tasks") }
            }

            getTaskGroupDetailedForIdUseCase.taskGroupDetailed(tasksGroupId).collect {
                updateState { copy(taskGroup = it, groupName = it.name ?: "Tasks") }
            }
        }
    }

    override fun onAddNewTaskClick() {
        viewModelScope.launch {
            addNewTasksUseCase.addNewTask(NewTask(uiState.value.newTaskDialogTextFieldText, groupId = tasksGroupId))
            updateState { copy(newTaskDialogVisible = false, newTaskDialogTextFieldText = "") }
        }
    }

    override fun onTaskCheckClick(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase.updateTask(task.copy(isDone = !task.isDone))
        }
    }

    override fun onTaskClick(task: Task) {
        //TODO("Not yet implemented")
    }

    override fun onNewTaskDialogOpened() {
        updateState { copy(newTaskDialogVisible = true) }
    }

    override fun onNewTaskDialogDismissed() {
        updateState { copy(newTaskDialogVisible = false) }
    }

    override fun onNewTaskDialogTextChanged(newText: String) {
        updateState { copy(newTaskDialogTextFieldText = newText) }
    }

    private inline fun updateState(newStateMaker: TaskGroupScreenState.() -> TaskGroupScreenState) {
        uiState.value = uiState.value.newStateMaker()
    }

    class Factory(
        private val tasksGroupId: Long?,
        private val getTaskGroupDetailedForIdUseCase: GetTaskGroupDetailedForIdUseCase,
        private val addNewTasksUseCase: AddNewTasksUseCase,
        private val updateTaskUseCase: UpdateTaskUseCase,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TaskGroupViewModel(
                tasksGroupId,
                getTaskGroupDetailedForIdUseCase,
                addNewTasksUseCase,
                updateTaskUseCase
            ) as T
        }
    }
}