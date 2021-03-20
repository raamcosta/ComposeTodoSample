package racosta.samples.composetodo.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import racosta.samples.composetodo.commons.CircleDialogWithTextFieldAndButton
import racosta.samples.composetodo.commons.ExplodingFab
import racosta.samples.composetodo.todologic.entities.TasksGroupSummary
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

class HomeScreen(private val viewState: HomeScreenState, private val userEvents: HomeScreenUserEvents) :
    Screen {

    @ExperimentalAnimationApi
    @Composable
    override fun Compose() {
        val taskGroups by viewState.taskGroups.collectAsState()
        val newTaskGroupName by viewState.newTaskGroupName.collectAsState()
        val newTaskGroupButtonEnabled by viewState.newTasksGroupButtonEnabled.collectAsState()
        val newTaskGroupDialogVisible by viewState.newTasksGroupDialogVisible.collectAsState()

        var explodeState by remember { mutableStateOf(false) }
        var showFabContent by remember { mutableStateOf(true) }

        BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {

            TasksGroupList(taskGroups)

            if (newTaskGroupDialogVisible) {
                NewTaskGroupDialog(
                    newTaskGroupName = newTaskGroupName,
                    newTaskGroupButtonEnabled = newTaskGroupButtonEnabled,
                    onDismissRequest = {
                        explodeState = false
                        userEvents.onNewTasksGroupDialogDismissed()
                    },
                    onButtonClick = {
                        explodeState = false
                        userEvents.onAddNewTasksGroupClick()
                    }
                )
            }

            NewTaskGroupFab(
                explodeState = explodeState,
                newTaskGroupDialogVisible = newTaskGroupDialogVisible,
                showFabContent = showFabContent,
                onFabClick = {
                    showFabContent = false
                    explodeState = true
                },
                onExplodingFinished = {
                    userEvents.onNewTasksGroupDialogOpened()
                },
                onShrinkingFinished = { showFabContent = true }
            )
        }
    }

    @ExperimentalAnimationApi
    @Composable
    private fun BoxWithConstraintsScope.NewTaskGroupFab(
        explodeState: Boolean,
        newTaskGroupDialogVisible: Boolean,
        showFabContent: Boolean,
        onFabClick: () -> Unit,
        onExplodingFinished: () -> Unit,
        onShrinkingFinished: () -> Unit
    ) {
        ExplodingFab(
            availableWidth = maxWidth,
            availableHeight = maxHeight,
            explodeState = explodeState,
            showFab = !newTaskGroupDialogVisible,
            onFabClick = onFabClick,
            onExplodingFinished = onExplodingFinished,
            onShrinkingFinished = onShrinkingFinished
        ) {
            AnimatedVisibility(visible = showFabContent) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
            }
        }
    }

    @Composable
    private fun NewTaskGroupDialog(
        newTaskGroupName: String,
        newTaskGroupButtonEnabled: Boolean,
        onDismissRequest: () -> Unit,
        onButtonClick: () -> Unit,
    ) {
        CircleDialogWithTextFieldAndButton(
            modifier = Modifier.size(300.dp),
            editTextValue = newTaskGroupName,
            onEditTextValueChanged = { userEvents.onNewTaskGroupNameChanged(it) },
            editTextLabel = { Text("Add a task list", color = Color.White) },
            buttonContent = { Text("Add") },
            buttonEnabled = newTaskGroupButtonEnabled,
            onDismissRequest = onDismissRequest,
            onButtonClick = onButtonClick,
        )
    }

    @Composable
    private fun TasksGroupList(taskGroups: List<TasksGroupSummary>) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(taskGroups) { taskGroup ->
                TaskGroup(taskGroup)
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

    companion object : ScreenDefinition<Nothing?> by HomeScreenDefinition
}