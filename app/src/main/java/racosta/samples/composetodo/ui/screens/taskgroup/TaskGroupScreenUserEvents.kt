package racosta.samples.composetodo.ui.screens.taskgroup

import racosta.samples.composetodo.todologic.entities.Task

interface TaskGroupScreenUserEvents {
    fun onAddNewTaskClick()
    fun onTaskCheckClick(task: Task)
    fun onTaskClick(task: Task)
    fun onNewTaskDialogOpened()
    fun onNewTaskDialogDismissed()
    fun onNewTaskDialogTextChanged(newText: String)
}