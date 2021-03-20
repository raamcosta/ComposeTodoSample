package racosta.samples.composetodo.ui.screens.base

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import racosta.samples.composetodo.TodoApp
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.navigator.Navigator

interface Screen {

    val name: String

    val isBottomNavScreen get() = icon != null

    val icon: ImageVector? get() = null
    @get:StringRes
    val iconContentDescription: Int?
        get() = null

    val navDeepLinks: List<NavDeepLink> get() = emptyList()

    fun addComposable(
        navGraphBuilder: NavGraphBuilder,
        navigator: Navigator
    ) {
        navGraphBuilder.composable(
            name,
            emptyList(),
            navDeepLinks
        ) {
            val app = LocalContext.current.applicationContext as TodoApp

            val compositionRoot = remember {
                ScreenCompositionRoot(app.appCompositionRoot, navigator)
            }

            makeComposable(compositionRoot).Compose()
        }
    }

    @Composable
    fun makeComposable(compositionRoot: ScreenCompositionRoot): ScreenComposable
}