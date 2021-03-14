package racosta.samples.composetodo.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import racosta.samples.composetodo.persistence.daos.TaskDao
import racosta.samples.composetodo.persistence.daos.TaskGroupDao
import racosta.samples.composetodo.persistence.entities.TaskEntity
import racosta.samples.composetodo.persistence.entities.TaskGroupEntity

const val DB_NAME = "todo-db"
private const val DB_VERSION = 1

@Database(entities = [TaskEntity::class, TaskGroupEntity::class], version = DB_VERSION)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun taskGroupDao(): TaskGroupDao
}