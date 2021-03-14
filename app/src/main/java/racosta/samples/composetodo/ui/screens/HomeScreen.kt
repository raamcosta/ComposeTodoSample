package racosta.samples.composetodo.ui.screens

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.todologic.entities.TasksGroupSummary
import racosta.samples.composetodo.ui.screens.base.ModeledScreenDefinition
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.viewmodels.HomeViewModel

class HomeScreen(private val viewState: State, private val userEvents: UserEvents) : Screen {

    @Composable
    override fun Compose() {
        val taskGroups by viewState.taskGroups.collectAsState()
        val newTaskGroup by viewState.newTaskGroupName.collectAsState()
        val newTaskGroupButtonEnabled by viewState.newTaskGroupButtonEnabled.collectAsState()

        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            LazyColumn(Modifier.weight(1f)) {
                items(taskGroups) { taskGroup ->
                    TaskGroup(taskGroup)
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = newTaskGroup,
                    onValueChange = { userEvents.onNewTaskGroupNameChanged(it) }
                )

                Button(
                    enabled = newTaskGroupButtonEnabled,
                    onClick = { userEvents.onAddNewTasksGroupClick() }) {
                    Text("Add")
                }
            }
        }
    }

    @Composable
    private fun TaskGroup(taskGroupSummary: TasksGroupSummary) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { userEvents.onTaskGroupClick(taskGroupSummary.id) }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(Icons.Filled.List, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (taskGroupSummary.name == null) "Tasks" else "${taskGroupSummary.name}")
            }

            if (taskGroupSummary.totalTasks != 0) {
                Text("${taskGroupSummary.tasksDone} / ${taskGroupSummary.totalTasks}")
            }
        }
    }

    companion object : ModeledScreenDefinition<HomeViewModel>() {

        override val rawRoute = "home"
        override val route = rawRoute

        override val icon: ImageVector
            get() = Icons.Filled.Home

        override val iconContentDescription: Int?
            get() = null

        @Composable
        override fun viewModel(
            arguments: Bundle?,
            compositionRoot: ScreenCompositionRoot
        ): HomeViewModel {
            return viewModel(
                factory = HomeViewModel.Factory(
                    compositionRoot.getAllTasksGroupsUseCase,
                    compositionRoot.addNewTasksUseCase
                )
            )
        }

        @Composable
        override fun prepareScreen(viewModel: HomeViewModel): Screen {
            return HomeScreen(viewModel, viewModel)
        }
    }

    interface UserEvents {
        fun onAddNewTasksGroupClick()
        fun onTaskGroupClick(taskGroupId: Long?)
        fun onNewTaskGroupNameChanged(newGroupName: String)
    }

    interface State {
        val taskGroups: StateFlow<List<TasksGroupSummary>>
        val newTaskGroupName: StateFlow<String>
        val newTaskGroupButtonEnabled: MutableStateFlow<Boolean>
    }
}
