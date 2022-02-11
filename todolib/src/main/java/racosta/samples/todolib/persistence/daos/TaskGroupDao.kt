package racosta.samples.todolib.persistence.daos

import kotlinx.coroutines.flow.Flow
import racosta.samples.todolib.persistence.entities.TaskGroupEntity

interface TaskGroupDao {

    fun makeTaskGroup(id: Long,
                      name: String) : TaskGroupEntity

    fun getAll(): Flow<List<TaskGroupEntity>>

    fun getForId(id: Long): Flow<TaskGroupEntity>

    suspend fun insertOrUpdate(taskGroups: TaskGroupEntity): Long
}