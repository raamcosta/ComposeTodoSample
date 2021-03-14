package racosta.samples.composetodo

import racosta.samples.composetodo.persistence.entities.TaskEntity
import racosta.samples.composetodo.persistence.entities.TaskGroupEntity
import racosta.samples.composetodo.todologic.entities.NewTask
import racosta.samples.composetodo.todologic.entities.NewTaskGroup
import racosta.samples.composetodo.todologic.entities.Task
import racosta.samples.composetodo.todologic.entities.TasksGroupSummary


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
) = TaskEntity(
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
) = TaskGroupEntity(
    id,
    name
)