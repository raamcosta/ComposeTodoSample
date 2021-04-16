package racosta.samples.composetodo.ui.screens.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import racosta.samples.composetodo.commons.TodoAppTopBar
import racosta.samples.composetodo.ui.screens.base.ScreenComposable

class SettingsComposable : ScreenComposable {

    @ExperimentalAnimationApi
    @Composable
    override fun Compose() {
        TodoAppTopBar(title = "Settings")
    }
}