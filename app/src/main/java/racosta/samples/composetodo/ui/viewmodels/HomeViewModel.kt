package racosta.samples.composetodo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import racosta.samples.composetodo.commons.launchInScope
import racosta.samples.composetodo.todologic.entities.NewTaskGroup
import racosta.samples.composetodo.todologic.usecases.AddNewTasksUseCase
import racosta.samples.composetodo.todologic.usecases.GetAllTaskGroupsUseCase
import racosta.samples.composetodo.ui.screens.home.HomeScreenState
import racosta.samples.composetodo.ui.screens.home.HomeScreenUserEvents
import racosta.samples.composetodo.ui.screens.taskgroup.TaskGroupArguments
import racosta.samples.composetodo.ui.screens.taskgroup.TaskGroupScreen
import racosta.samples.composetodo.ui.viewmodels.base.NavigatorViewModel

class HomeViewModel(
    private val getAllTaskGroupsUseCase: GetAllTaskGroupsUseCase,
    private val addNewTasksUseCase: AddNewTasksUseCase
) : NavigatorViewModel(), HomeScreenUserEvents, HomeScreenState {

    //region screen state

    override val taskGroups = getAllTaskGroupsUseCase.allTaskGroups.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    override val newTaskGroupName: StateFlow<String> = MutableStateFlow("")

    override val newTasksGroupButtonEnabled = MutableStateFlow(isAddNewGroupButtonEnabled())

    override val newTasksGroupDialogVisible = MutableStateFlow(false)

    //endregion

    //region user events handling

    override fun onTaskGroupClick(taskGroupId: Long?) {
        goTo(TaskGroupScreen, TaskGroupArguments(taskGroupId))
    }

    override fun onAddNewTasksGroupClick() {
        if (!isAddNewGroupButtonEnabled()) {
            throw IllegalStateException("onAddNewTasksGroupClick called but button should be disabled")
        }

        launchInScope {
            addNewTasksUseCase.addNewTaskGroup(NewTaskGroup(newTaskGroupName.value))
            setNewTaskGroupName("")
            newTasksGroupDialogVisible.value = false
        }
    }

    override fun onNewTaskGroupNameChanged(newGroupName: String) {
        setNewTaskGroupName(newGroupName)
    }

    override fun onNewTasksGroupDialogOpened() {
        newTasksGroupDialogVisible.value = true
    }

    override fun onNewTasksGroupDialogDismissed() {
        newTasksGroupDialogVisible.value = false
    }

    //endregion

    //region internal logic

    private fun isAddNewGroupButtonEnabled() = newTaskGroupName.value.isNotBlank()

    private fun setNewTaskGroupName(newGroupName: String) = with(newTaskGroupName as MutableStateFlow) {
        newTaskGroupName.value = newGroupName

        val shouldButtonBeEnabled = isAddNewGroupButtonEnabled()
        if (newTasksGroupButtonEnabled.value != shouldButtonBeEnabled) {
            newTasksGroupButtonEnabled.value = shouldButtonBeEnabled
        }
    }

    //endregion

    //region factory

    class Factory(
        private val getAllTaskGroupsUseCase: GetAllTaskGroupsUseCase,
        private val addNewTasksUseCase: AddNewTasksUseCase
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(getAllTaskGroupsUseCase, addNewTasksUseCase) as T
        }
    }

    //endregion
}