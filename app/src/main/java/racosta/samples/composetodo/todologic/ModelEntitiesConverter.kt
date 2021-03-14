package racosta.samples.composetodo.todologic

import racosta.samples.composetodo.persistence.entities.TaskEntity
import racosta.samples.composetodo.persistence.entities.TaskGroupEntity
import racosta.samples.composetodo.todologic.entities.NewTask
import racosta.samples.composetodo.todologic.entities.NewTaskGroup
import racosta.samples.composetodo.todologic.entities.Task

fun TaskEntity.toTask() = Task(id, title, description, remindMe, dueDate, isDone, groupId)

fun Task.toEntity() = TaskEntity(id, title, description, remindMe, dueDate, isDone, groupId)

fun NewTask.toEntity() = TaskEntity(0, title, null, remindMe, dueDate, false, groupId)

fun NewTaskGroup.toEntity() = TaskGroupEntity(0, name)