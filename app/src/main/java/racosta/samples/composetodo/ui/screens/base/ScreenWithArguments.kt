package racosta.samples.composetodo.ui.screens.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.google.gson.Gson
import racosta.samples.composetodo.TodoApp
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.navigator.Navigator
import java.lang.reflect.Type

abstract class ScreenWithArguments<Arguments> : Screen {

    abstract val argType: Type

    @Composable
    abstract fun makeComposable(arguments: Arguments, compositionRoot: ScreenCompositionRoot): ScreenComposable

    final override fun addComposable(navGraphBuilder: NavGraphBuilder, navigator: Navigator) {
        navGraphBuilder.composable(
            fullRoute(),
            navArguments,
            navDeepLinks
        ) {
            val arguments: Arguments = parseArguments(it.arguments?.getString(NAV_ARG), argType)
            val app = LocalContext.current.applicationContext as TodoApp

            val compositionRoot = remember {
                ScreenCompositionRoot(app.appCompositionRoot, navigator)
            }

            makeComposable(arguments, compositionRoot).Compose()
        }
    }

    @Composable
    final override fun makeComposable(compositionRoot: ScreenCompositionRoot): ScreenComposable {
        throw RuntimeException("ArgScreenDefinition must use prepareScreen with arguments instead!")
    }

    companion object {
        private const val NAV_ARG = "navArg"
        private val gson = Gson()

        private val navArguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(NAV_ARG) {
                    nullable = true
                    type = NavType.StringType
                }
            )

        fun ScreenWithArguments<*>.fullRoute(): String {
            return "$name?$NAV_ARG={$NAV_ARG}"
        }

        fun <T> ScreenWithArguments<T>.fullRoute(arguments: T): String {
            return "$name?$NAV_ARG=${gson.toJson(arguments)}"
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> parseArguments(navArg: String?, argType: Type?): T =
            if (navArg != null) gson.fromJson(navArg, argType)
            else null as T
    }
}