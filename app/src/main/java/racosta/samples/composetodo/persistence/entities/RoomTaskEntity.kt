package racosta.samples.composetodo.persistence.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.SET_NULL
import androidx.room.PrimaryKey
import racosta.samples.todolib.persistence.entities.TaskEntity

const val TASK_TABLE = "tasks"

@Entity(
    tableName = TASK_TABLE,
    foreignKeys = [
        ForeignKey(entity = RoomTaskGroupEntity::class, parentColumns = ["id"], childColumns = ["groupId"], onDelete = SET_NULL)
    ],
)
data class RoomTaskEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    override val title: String,
    override val description: String?,
    override val remindMe: Long?,
    override val dueDate: Long?,
    override val isDone: Boolean,
    override val groupId: Long?,
) : TaskEntity