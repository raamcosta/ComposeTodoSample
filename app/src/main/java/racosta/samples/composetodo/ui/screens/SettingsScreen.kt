package racosta.samples.composetodo.ui.screens

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

class SettingsScreen : Screen {

    @Composable
    override fun Compose() {
        Text("Settings")
    }

    companion object : ScreenDefinition {
        override val name = "settings"

        override val icon: ImageVector
            get() = Icons.Filled.Settings

        override val iconContentDescription: Int?
            get() = null

        @Composable
        override fun prepareScreen(compositionRoot: ScreenCompositionRoot): Screen {
            return SettingsScreen()
        }
    }
}