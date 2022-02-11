package racosta.samples.todolib.usecases

import racosta.samples.todolib.persistence.daos.TaskDao
import racosta.samples.todolib.entities.Task
import racosta.samples.todolib.toEntity

class UpdateTaskUseCase(private val taskDao: TaskDao) {

    suspend fun updateTask(task: Task) {
        taskDao.insertOrUpdate(task.toEntity(taskDao))
    }
}