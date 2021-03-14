package racosta.samples.composetodo.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import racosta.samples.composetodo.persistence.entities.TASK_TABLE
import racosta.samples.composetodo.persistence.entities.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM $TASK_TABLE")
    fun getAll() : Flow<List<TaskEntity>>

    @Query("SELECT * FROM $TASK_TABLE WHERE groupId IS NULL")
    fun getAllForNullGroup(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM $TASK_TABLE WHERE groupId = :groupId")
    fun getAllForGroupId(groupId: Long): Flow<List<TaskEntity>>

    fun getAllForGroupId(groupId: Long?): Flow<List<TaskEntity>> {
        return if (groupId == null) {
            getAllForNullGroup()
        } else {
            getAllForGroupId(groupId)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(vararg tasks: TaskEntity)
}
