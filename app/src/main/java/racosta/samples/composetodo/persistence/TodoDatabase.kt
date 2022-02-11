package racosta.samples.composetodo.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import racosta.samples.composetodo.persistence.daos.AndroidTaskDao
import racosta.samples.composetodo.persistence.daos.AndroidTaskGroupDao
import racosta.samples.composetodo.persistence.daos.RoomTaskDao
import racosta.samples.composetodo.persistence.daos.RoomTaskGroupDao
import racosta.samples.composetodo.persistence.entities.RoomTaskEntity
import racosta.samples.composetodo.persistence.entities.RoomTaskGroupEntity
import racosta.samples.todolib.persistence.daos.TaskDao
import racosta.samples.todolib.persistence.daos.TaskGroupDao

const val DB_NAME = "todo-db"
private const val DB_VERSION = 1

@Database(entities = [RoomTaskEntity::class, RoomTaskGroupEntity::class], version = DB_VERSION, exportSchema = false)
abstract class TodoDatabase: RoomDatabase() {

    fun taskDao(): TaskDao = AndroidTaskDao(roomTaskDao())

    fun taskGroupDao(): TaskGroupDao = AndroidTaskGroupDao(roomTaskGroupDao())

    protected abstract fun roomTaskDao(): RoomTaskDao

    protected abstract fun roomTaskGroupDao(): RoomTaskGroupDao
}