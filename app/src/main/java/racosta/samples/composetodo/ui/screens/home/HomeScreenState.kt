package racosta.samples.composetodo.ui.screens.home

import kotlinx.coroutines.flow.StateFlow
import racosta.samples.todolib.entities.TasksGroupSummary

interface HomeScreenState {
    val taskGroups: StateFlow<List<TasksGroupSummary>>
    val newTaskGroupName: StateFlow<String>
    val newTasksGroupButtonEnabled: StateFlow<Boolean>
    val newTasksGroupDialogVisible: StateFlow<Boolean>
}