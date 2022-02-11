package racosta.samples.composetodo

import racosta.samples.composetodo.persistence.entities.RoomTaskEntity
import racosta.samples.composetodo.persistence.entities.RoomTaskGroupEntity
import racosta.samples.todolib.entities.NewTask
import racosta.samples.todolib.entities.NewTaskGroup
import racosta.samples.todolib.entities.Task
import racosta.samples.todolib.entities.TasksGroupSummary


fun tasksGroup(
    id: Long? = null,
    groupName: String? = "",
    totalTasks: Int = 0,
    tasksDone: Int = 0,
) = TasksGroupSummary(
    id, groupName, totalTasks, tasksDone
)

fun defaultTaskGroup(
    totalTasks: Int = 0,
    tasksDone: Int = 0,
) = TasksGroupSummary(null, null, totalTasks, tasksDone)

fun task(
    id: Long = 0,
    title: String = "",
    description: String? = null,
    remindMe: Long? = null,
    dueDate: Long? = null,
    isDone: Boolean = false,
    groupId: Long? = null
) = Task(
    id,
    title,
    description,
    remindMe,
    dueDate,
    isDone,
    groupId
)

fun newTask(
    title: String,
    remindMe: Long? = null,
    dueDate: Long? = null,
    groupId: Long? = null
) = NewTask(
    title,
    remindMe,
    dueDate,
    groupId
)

fun newTaskGroup(name: String) = NewTaskGroup(name)

fun taskEntity(
    id: Long = 0,
    title: String = "",
    description: String? = null,
    remindMe: Long? = null,
    dueDate: Long? = null,
    isDone: Boolean = false,
    groupId: Long? = null
) = RoomTaskEntity(
    id,
    title,
    description,
    remindMe,
    dueDate,
    isDone,
    groupId,
)

fun taskGroupEntity(
    id: Long = 0,
    name: String = ""
) = RoomTaskGroupEntity(
    id,
    name
)