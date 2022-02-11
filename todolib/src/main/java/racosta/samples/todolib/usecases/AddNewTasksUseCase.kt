package racosta.samples.todolib.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import racosta.samples.todolib.persistence.daos.TaskDao
import racosta.samples.todolib.persistence.daos.TaskGroupDao
import racosta.samples.todolib.entities.NewTask
import racosta.samples.todolib.entities.NewTaskGroup
import racosta.samples.todolib.toEntity

class AddNewTasksUseCase(private val taskDao: TaskDao, private val taskGroupDao: TaskGroupDao) {

    suspend fun addNewTask(task: NewTask) = withContext(Dispatchers.IO) {
        taskDao.insertOrUpdate(task.toEntity(taskDao))
    }

    suspend fun addNewTaskGroup(taskGroup: NewTaskGroup): Long = withContext(Dispatchers.IO) {
        taskGroupDao.insertOrUpdate(taskGroup.toEntity(taskGroupDao))
    }
}