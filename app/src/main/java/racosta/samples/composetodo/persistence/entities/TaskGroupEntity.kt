package racosta.samples.composetodo.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TASK_GROUPS_TABLE = "task_groups"

@Entity(tableName = TASK_GROUPS_TABLE)
data class TaskGroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
)
