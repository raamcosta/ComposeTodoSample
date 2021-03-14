package racosta.samples.composetodo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import racosta.samples.composetodo.commons.launchInScope
import racosta.samples.composetodo.todologic.entities.NewTaskGroup
import racosta.samples.composetodo.todologic.entities.TasksGroupSummary
import racosta.samples.composetodo.todologic.usecases.AddNewTasksUseCase
import racosta.samples.composetodo.todologic.usecases.GetAllTaskGroupsUseCase
import racosta.samples.composetodo.ui.screens.TaskGroupScreen
import racosta.samples.composetodo.ui.screens.HomeScreen
import racosta.samples.composetodo.ui.viewmodels.base.NavigatorViewModel

class HomeViewModel(
    private val getAllTaskGroupsUseCase: GetAllTaskGroupsUseCase,
    private val addNewTasksUseCase: AddNewTasksUseCase
) : NavigatorViewModel(), HomeScreen.UserEvents, HomeScreen.State {

    //region screen state

    override val taskGroups = MutableStateFlow<List<TasksGroupSummary>>(emptyList())

    override val newTaskGroupName = MutableStateFlow("")

    override val newTaskGroupButtonEnabled = MutableStateFlow(isAddNewGroupButtonEnabled())

    //endregion

    init {
        launchInScope {
            getAllTaskGroupsUseCase.allTaskGroups.collect {
                taskGroups.value = it
            }
        }
    }

    //region user events handling

    override fun onTaskGroupClick(taskGroupId: Long?) {
        goTo(TaskGroupScreen.createRoute(taskGroupId))
    }

    override fun onAddNewTasksGroupClick() {
        if (!isAddNewGroupButtonEnabled()) {
            throw IllegalStateException("onAddNewTasksGroupClick called but button should be disabled")
        }

        launchInScope {
            addNewTasksUseCase.addNewTaskGroup(NewTaskGroup(newTaskGroupName.value))
            newTaskGroupName.value = ""
        }
    }

    override fun onNewTaskGroupNameChanged(newGroupName: String) {
        newTaskGroupName.value = newGroupName

        val shouldButtonBeEnabled = isAddNewGroupButtonEnabled()
        if (newTaskGroupButtonEnabled.value != shouldButtonBeEnabled) {
            newTaskGroupButtonEnabled.value = shouldButtonBeEnabled
        }
    }

    //endregion

    //region internal logic

    private fun isAddNewGroupButtonEnabled() = newTaskGroupName.value.isNotBlank()

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