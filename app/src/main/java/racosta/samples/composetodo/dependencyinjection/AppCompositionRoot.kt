package racosta.samples.composetodo.dependencyinjection

import android.app.Application
import racosta.samples.composetodo.ui.screens.ByeScreen
import racosta.samples.composetodo.ui.screens.HomeScreen
import racosta.samples.composetodo.ui.screens.SettingsScreen

class AppCompositionRoot(override val app: Application) : AppScope {

    override val screens = listOf(
        HomeScreen,
        ByeScreen,
        SettingsScreen
    )
}