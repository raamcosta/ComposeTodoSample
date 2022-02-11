package racosta.samples.todolib.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import racosta.samples.todolib.entities.TasksGroupDetailed
import racosta.samples.todolib.persistence.daos.TaskDao
import racosta.samples.todolib.persistence.daos.TaskGroupDao
import racosta.samples.todolib.persistence.entities.TaskEntity
import racosta.samples.todolib.toTask

class GetTaskGroupDetailedForIdUseCase(
    private val taskDao: TaskDao,
    private val taskGroupDao: TaskGroupDao
) {

    fun taskGroupDetailed(id: Long?): Flow<TasksGroupDetailed> {
        return if (id == null) {
            taskDao.getAllForGroupId(id).map {
                it.toTasksGroupDetailed(id)
            }
        } else {
            taskDao.getAllForGroupId(id).combine(taskGroupDao.getForId(id)) { tasks, taskGroup ->
                tasks.toTasksGroupDetailed(id, taskGroup.name)
            }
        }
    }

    private fun List<TaskEntity>.toTasksGroupDetailed(
        id: Long?,
        groupName: String? = null
    ): TasksGroupDetailed {
        return TasksGroupDetailed(id, groupName, map { it.toTask() })
    }
}
