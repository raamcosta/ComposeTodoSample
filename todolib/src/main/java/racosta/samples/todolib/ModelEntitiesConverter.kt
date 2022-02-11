package racosta.samples.todolib

import racosta.samples.todolib.entities.NewTask
import racosta.samples.todolib.entities.NewTaskGroup
import racosta.samples.todolib.entities.Task
import racosta.samples.todolib.persistence.daos.TaskDao
import racosta.samples.todolib.persistence.daos.TaskGroupDao
import racosta.samples.todolib.persistence.entities.TaskEntity

//region Entities to Models

fun TaskEntity.toTask() = Task(id, title, description, remindMe, dueDate, isDone, groupId)

//endregion

//region Models to Entities

fun Task.toEntity(taskDao: TaskDao) = taskDao.makeTask(id, title, description, remindMe, dueDate, isDone, groupId)

fun NewTask.toEntity(taskDao: TaskDao) = taskDao.makeTask(0, title, null, remindMe, dueDate, false, groupId)

fun NewTaskGroup.toEntity(taskGroupDao: TaskGroupDao) = taskGroupDao.makeTaskGroup(0, name)

//endregion