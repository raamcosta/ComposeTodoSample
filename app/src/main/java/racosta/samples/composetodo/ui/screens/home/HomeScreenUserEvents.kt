package racosta.samples.composetodo.ui.screens.home

interface HomeScreenUserEvents {
    fun onAddNewTasksGroupClick()
    fun onTaskGroupClick(taskGroupId: Long?)
    fun onNewTaskGroupNameChanged(newGroupName: String)
    fun onNewTasksGroupDialogOpened()
    fun onNewTasksGroupDialogDismissed()
}