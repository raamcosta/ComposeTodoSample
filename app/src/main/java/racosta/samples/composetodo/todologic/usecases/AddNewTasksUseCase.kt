package racosta.samples.composetodo.todologic.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import racosta.samples.composetodo.persistence.daos.TaskDao
import racosta.samples.composetodo.persistence.daos.TaskGroupDao
import racosta.samples.composetodo.todologic.entities.NewTask
import racosta.samples.composetodo.todologic.entities.NewTaskGroup
import racosta.samples.composetodo.todologic.toEntity

class AddNewTasksUseCase(private val taskDao: TaskDao, private val taskGroupDao: TaskGroupDao) {

    suspend fun addNewTask(task: NewTask) = withContext(Dispatchers.IO) {
        taskDao.insertOrUpdate(task.toEntity())
    }

    suspend fun addNewTaskGroup(taskGroup: NewTaskGroup): Long = withContext(Dispatchers.IO) {
        taskGroupDao.insertOrUpdate(taskGroup.toEntity())
    }
}