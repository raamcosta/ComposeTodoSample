package racosta.samples.composetodo.ui.screens.base

import androidx.annotation.CallSuper
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
import java.lang.RuntimeException
import java.lang.reflect.Type

interface ScreenWithArgumentsDefinition<Arguments> : ScreenDefinition {

    val argType: Type? get() = null

    override fun addComposable(navGraphBuilder: NavGraphBuilder, navigator: Navigator) {
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

            prepareScreen(arguments, compositionRoot).Compose()
        }
    }

    @Composable
    fun prepareScreen(arguments: Arguments, compositionRoot: ScreenCompositionRoot): Screen

    @CallSuper
    @Composable
    override fun prepareScreen(compositionRoot: ScreenCompositionRoot): Screen {
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

        fun ScreenWithArgumentsDefinition<*>.fullRoute(): String {
            return "$name?$NAV_ARG={$NAV_ARG}"
        }

        fun <T> ScreenWithArgumentsDefinition<T>.fullRoute(arguments: T): String {
            return "$name?$NAV_ARG=${gson.toJson(arguments)}"
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> parseArguments(navArg: String?, argType: Type?): T =
            if (navArg != null) gson.fromJson(navArg, argType)
            else null as T
    }
}