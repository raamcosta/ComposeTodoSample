package racosta.samples.composetodo.ui.screens.taskgroup

import racosta.samples.composetodo.todologic.entities.TasksGroupDetailed

data class TaskGroupScreenState(
    val groupName: String,
    val taskGroup: TasksGroupDetailed?,
    val newTaskDialogVisible: Boolean,
    val newTaskDialogTextFieldText: String,
)