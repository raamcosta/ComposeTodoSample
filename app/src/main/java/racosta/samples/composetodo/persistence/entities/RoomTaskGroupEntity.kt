package racosta.samples.composetodo.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import racosta.samples.todolib.persistence.entities.TaskGroupEntity

const val TASK_GROUPS_TABLE = "task_groups"

@Entity(tableName = TASK_GROUPS_TABLE)
data class RoomTaskGroupEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    override val name: String,
): TaskGroupEntity
