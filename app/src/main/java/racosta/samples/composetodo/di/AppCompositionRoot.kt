package racosta.samples.composetodo.di

import android.app.Application
import androidx.room.Room
import racosta.samples.composetodo.persistence.DB_NAME
import racosta.samples.composetodo.persistence.TodoDatabase
import racosta.samples.composetodo.ui.screens.home.HomeScreen
import racosta.samples.composetodo.ui.screens.settings.SettingsScreen
import racosta.samples.composetodo.ui.screens.taskgroup.TaskGroupScreen

class AppCompositionRoot(override val app: Application) : AppScope {

    override val screens = arrayOf(
        HomeScreen,
        TaskGroupScreen,
        SettingsScreen,
    )

    override val db = Room.databaseBuilder(app, TodoDatabase::class.java, DB_NAME).build()
}