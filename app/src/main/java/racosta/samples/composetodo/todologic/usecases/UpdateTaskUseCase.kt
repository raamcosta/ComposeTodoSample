package racosta.samples.composetodo.todologic.usecases

import racosta.samples.composetodo.persistence.daos.TaskDao
import racosta.samples.composetodo.todologic.entities.Task
import racosta.samples.composetodo.todologic.toEntity

class UpdateTaskUseCase(private val taskDao: TaskDao) {

    suspend fun updateTask(task: Task) {
        taskDao.insertOrUpdate(task.toEntity())
    }
}