package racosta.samples.composetodo.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import racosta.samples.composetodo.persistence.entities.TASK_GROUPS_TABLE
import racosta.samples.composetodo.persistence.entities.TaskGroupEntity

@Dao
interface TaskGroupDao {

    @Query("SELECT * FROM $TASK_GROUPS_TABLE")
    fun getAll() : Flow<List<TaskGroupEntity>>

    @Query("SELECT * FROM $TASK_GROUPS_TABLE WHERE id = :id")
    fun getForId(id: Long): Flow<TaskGroupEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertOrUpdate(taskGroups: TaskGroupEntity) : Long
}