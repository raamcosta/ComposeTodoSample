package racosta.samples.composetodo.ui.screens.taskgroup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
import racosta.samples.composetodo.commons.CircleDialogWithTextFieldAndButton
import racosta.samples.composetodo.commons.ExplodingFab
import racosta.samples.composetodo.commons.Logger
import racosta.samples.composetodo.todologic.entities.Task
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

class TaskGroupScreen(private val state: StateFlow<TaskGroupScreenState>, private val userEvents: TaskGroupScreenUserEvents) :
    Screen, Logger {

    @ExperimentalAnimationApi
    @Composable
    override fun Compose() {
        BoxWithConstraints(contentAlignment = Alignment.BottomEnd) {
            val state by state.collectAsState()
            var explodeState by remember { mutableStateOf(false) }
            var showFabContent by remember { mutableStateOf(true) }

            TasksList(state)

            if (state.newTaskDialogVisible) {
                NewTaskDialog(
                    editTextValue = state.newTaskDialogTextFieldText,
                    onEditTextValueChanged = userEvents::onNewTaskDialogTextChanged,
                    onDismissRequest = {
                        explodeState = false
                        userEvents.onNewTaskDialogDismissed()
                    },
                    onButtonClick = {
                        explodeState = false
                        userEvents.onAddNewTaskClick()
                    }
                )
            }

            NewTaskFab(
                onFabClick = {
                    showFabContent = false
                    explodeState = true
                },
                onExplodingFinished = {
                    userEvents.onNewTaskDialogOpened()
                },
                onShrinkingFinished = { showFabContent = true },
                explodeState = explodeState,
                showFab = !state.newTaskDialogVisible,
                showFabContent = showFabContent
            )
        }
    }

    @ExperimentalAnimationApi
    @Composable
    private fun BoxWithConstraintsScope.NewTaskFab(
        showFabContent: Boolean,
        showFab: Boolean,
        explodeState: Boolean,
        onFabClick: () -> Unit,
        onExplodingFinished: () -> Unit,
        onShrinkingFinished: () -> Unit,
    ) {
        ExplodingFab(
            availableWidth = maxWidth,
            availableHeight = maxHeight,
            showFab = showFab,
            explodeState = explodeState,
            onFabClick = onFabClick,
            onExplodingFinished = onExplodingFinished,
            onShrinkingFinished = onShrinkingFinished,
        ) {
            AnimatedVisibility(visible = showFabContent) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
            }
        }
    }

    @Composable
    private fun NewTaskDialog(
        editTextValue: String,
        onEditTextValueChanged: (String) -> Unit,
        onDismissRequest: () -> Unit,
        onButtonClick: () -> Unit
    ) {
        CircleDialogWithTextFieldAndButton(
            editTextValue = editTextValue,
            onEditTextValueChanged = onEditTextValueChanged,
            onDismissRequest = onDismissRequest,
            onButtonClick = onButtonClick,
            modifier = Modifier.size(300.dp),
            editTextLabel = { Text("Add a new task", color = Color.White) },
            buttonContent = { Text("Add") },
        )
    }

    @Composable
    private fun TasksList(state: TaskGroupScreenState) {
        Column(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = state.groupName,
                fontSize = 24.sp
            )

            val tasks = state.taskGroup?.tasks
            if (tasks != null) {
                TasksList(tasks)
            }
        }
    }

    @Composable
    private fun TasksList(tasks: List<Task>) {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(items = tasks) {
                TaskRow(it)
            }
        }
    }

    @Composable
    private fun TaskRow(task: Task) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { userEvents.onTaskClick(task) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                modifier = Modifier.padding(all = 6.dp),
                checked = task.isDone,
                onCheckedChange = { userEvents.onTaskCheckClick(task) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(task.title)
        }
    }

    companion object : ScreenDefinition<TaskGroupArguments> by TaskGroupScreenDefinition
}