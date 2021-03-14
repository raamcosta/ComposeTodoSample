package racosta.samples.composetodo.ui.screens.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable
import racosta.samples.composetodo.TodoApp
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.navigator.Navigator

interface ScreenDefinition {

    val route: String
    val rawRoute: String

    val isBottomNavScreen get() = icon != null

    val icon: ImageVector? get() = null
    @get:StringRes val iconContentDescription: Int? get() = null

    val navArguments: List<NamedNavArgument> get() = emptyList()
    val navDeepLinks: List<NavDeepLink> get() = emptyList()

    fun addComposable(
        navGraphBuilder: NavGraphBuilder,
        navigator: Navigator
    ) {
        navGraphBuilder.composable(
            route,
            navArguments,
            navDeepLinks
        ) {
            val app = LocalContext.current.applicationContext as TodoApp

            val compositionRoot = ScreenCompositionRoot(app.appCompositionRoot, navigator)

            prepareScreen(it.arguments, compositionRoot).Compose()
        }
    }

    @Composable
    fun prepareScreen(arguments: Bundle?, compositionRoot: ScreenCompositionRoot) : Screen
}