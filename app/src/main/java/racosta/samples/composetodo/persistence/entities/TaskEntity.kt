package racosta.samples.composetodo.persistence.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.SET_NULL
import androidx.room.PrimaryKey

const val TASK_TABLE = "tasks"

@Entity(
    tableName = TASK_TABLE,
    foreignKeys = [
        ForeignKey(entity = TaskGroupEntity::class, parentColumns = ["id"], childColumns = ["groupId"], onDelete = SET_NULL)
    ],
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val description: String?,
    val remindMe: Long?,
    val dueDate: Long?,
    val isDone: Boolean,
    val groupId: Long?,
)