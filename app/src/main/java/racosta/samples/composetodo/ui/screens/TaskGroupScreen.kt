package racosta.samples.composetodo.ui.screens

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import kotlinx.coroutines.flow.StateFlow
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.todologic.entities.Task
import racosta.samples.composetodo.todologic.entities.TasksGroupDetailed
import racosta.samples.composetodo.ui.screens.base.ModeledScreenDefinition
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.viewmodels.TaskGroupViewModel

class TaskGroupScreen(private val uiState: State, private val userEvents: UserEvents) : Screen {

    @Composable
    override fun Compose() {
        Column {
            val groupName = uiState.groupName.collectAsState()
            val tasksGroup = uiState.taskGroup.collectAsState()

            Button(onClick = { userEvents.onAddSomethingClick() }) {
                Text("Add Something")
            }
            Text(text = groupName.value)

            val tasks = tasksGroup.value?.tasks
            if (tasks != null) {
                TasksList(tasks)
            }
        }
    }

    @Composable
    private fun TasksList(tasks: List<Task>) {
        LazyColumn {
            items(items = tasks) {
                TaskRow(it)
            }
        }
    }

    @Composable
    private fun TaskRow(task: Task) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.padding(all = 6.dp),
                checked = task.isDone,
                onCheckedChange = { userEvents.onTaskCheckClick(task) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(task.title)
        }
    }

    companion object : ModeledScreenDefinition<TaskGroupViewModel>() {

        private const val idArg = "id"

        override val rawRoute = "taskGroup"
        override val route = "$rawRoute?$idArg={$idArg}"

        override val navArguments = listOf(
            navArgument(idArg) {
                nullable = true
                type = NavType.StringType
            }
        )

        fun createRoute(taskGroupId: Long?): String {
            return rawRoute + if (taskGroupId != null) "?$idArg=$taskGroupId" else ""
        }

        @Composable
        override fun viewModel(
            arguments: Bundle?,
            compositionRoot: ScreenCompositionRoot
        ): TaskGroupViewModel {
            return viewModel(
                factory = TaskGroupViewModel.Factory(
                    arguments?.getString(idArg)?.toLong(),
                    compositionRoot.getTaskGroupDetailedForIdUseCase,
                    compositionRoot.addNewTasksUseCase,
                    compositionRoot.updateTaskUseCase
                )
            )
        }

        @Composable
        override fun prepareScreen(viewModel: TaskGroupViewModel): Screen {
            return TaskGroupScreen(viewModel, viewModel)
        }
    }

    interface UserEvents {
        fun onAddSomethingClick()
        fun onTaskCheckClick(task: Task)
    }

    interface State {
        val groupName: StateFlow<String>
        val taskGroup: StateFlow<TasksGroupDetailed?>
    }
}