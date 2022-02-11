package racosta.samples.composetodo.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import racosta.samples.composetodo.persistence.entities.TASK_TABLE
import racosta.samples.composetodo.persistence.entities.RoomTaskEntity
import racosta.samples.todolib.persistence.daos.TaskDao
import racosta.samples.todolib.persistence.entities.TaskEntity

class AndroidTaskDao(private val roomTaskDao: RoomTaskDao) : TaskDao {

    override fun makeTask(
        id: Long,
        title: String,
        description: String?,
        remindMe: Long?,
        dueDate: Long?,
        isDone: Boolean,
        groupId: Long?
    ) = RoomTaskEntity(
        id,
        title,
        description,
        remindMe,
        dueDate,
        isDone,
        groupId
    )

    override fun getAll(): Flow<List<TaskEntity>> {
        return roomTaskDao.getAll()
    }

    override fun getAllForNullGroup(): Flow<List<TaskEntity>> {
        return roomTaskDao.getAllForNullGroup()
    }

    override fun getAllForGroupId(groupId: Long): Flow<List<TaskEntity>> {
        return roomTaskDao.getAllForGroupId(groupId)
    }

    override suspend fun insertOrUpdate(vararg tasks: TaskEntity) {
        roomTaskDao.insertOrUpdate(*(tasks.filterIsInstance<RoomTaskEntity>().toTypedArray()))
    }
}


@Dao
interface RoomTaskDao {

    @Query("SELECT * FROM $TASK_TABLE")
    fun getAll(): Flow<List<RoomTaskEntity>>

    @Query("SELECT * FROM $TASK_TABLE WHERE groupId IS NULL")
    fun getAllForNullGroup(): Flow<List<RoomTaskEntity>>

    @Query("SELECT * FROM $TASK_TABLE WHERE groupId = :groupId")
    fun getAllForGroupId(groupId: Long): Flow<List<RoomTaskEntity>>

    fun getAllForGroupId(groupId: Long?): Flow<List<RoomTaskEntity>> {
        return if (groupId == null) {
            getAllForNullGroup()
        } else {
            getAllForGroupId(groupId)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(vararg tasks: RoomTaskEntity)
}
