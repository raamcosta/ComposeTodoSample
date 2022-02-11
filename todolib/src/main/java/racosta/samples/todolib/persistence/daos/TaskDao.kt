package racosta.samples.todolib.persistence.daos

import kotlinx.coroutines.flow.Flow
import racosta.samples.todolib.persistence.entities.TaskEntity

interface TaskDao {

    fun makeTask(
        id: Long,
        title: String,
        description: String?,
        remindMe: Long?,
        dueDate: Long?,
        isDone: Boolean,
        groupId: Long?
    ): TaskEntity

    fun getAll(): Flow<List<TaskEntity>>

    fun getAllForNullGroup(): Flow<List<TaskEntity>>

    fun getAllForGroupId(groupId: Long): Flow<List<TaskEntity>>

    fun getAllForGroupId(groupId: Long?): Flow<List<TaskEntity>> {
        return if (groupId == null) {
            getAllForNullGroup()
        } else {
            getAllForGroupId(groupId)
        }
    }

    suspend fun insertOrUpdate(vararg tasks: TaskEntity)
}