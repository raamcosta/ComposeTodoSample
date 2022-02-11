package racosta.samples.composetodo.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import racosta.samples.composetodo.persistence.entities.TASK_GROUPS_TABLE
import racosta.samples.composetodo.persistence.entities.RoomTaskGroupEntity
import racosta.samples.todolib.persistence.daos.TaskGroupDao
import racosta.samples.todolib.persistence.entities.TaskGroupEntity

class AndroidTaskGroupDao(private val roomTaskGroupDao: RoomTaskGroupDao) : TaskGroupDao {

    override fun makeTaskGroup(
        id: Long,
        name: String
    ) = RoomTaskGroupEntity(
        id,
        name
    )

    override fun getAll(): Flow<List<TaskGroupEntity>> {
        return roomTaskGroupDao.getAll()
    }

    override fun getForId(id: Long): Flow<TaskGroupEntity> {
        return roomTaskGroupDao.getForId(id)
    }

    override suspend fun insertOrUpdate(taskGroups: TaskGroupEntity): Long {
        return roomTaskGroupDao.insertOrUpdate(taskGroups as  RoomTaskGroupEntity)
    }

}

@Dao
interface RoomTaskGroupDao {

    @Query("SELECT * FROM $TASK_GROUPS_TABLE")
    fun getAll(): Flow<List<RoomTaskGroupEntity>>

    @Query("SELECT * FROM $TASK_GROUPS_TABLE WHERE id = :id")
    fun getForId(id: Long): Flow<RoomTaskGroupEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertOrUpdate(taskGroups: RoomTaskGroupEntity): Long
}