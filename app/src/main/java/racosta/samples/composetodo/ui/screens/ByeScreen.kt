package racosta.samples.composetodo.ui.screens

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import racosta.samples.composetodo.dependencyinjection.ScreenCompositionRoot
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition
import racosta.samples.composetodo.ui.navigator.Navigator
import racosta.samples.composetodo.ui.screens.base.Screen

class ByeScreen(private val name: String, private val navigator: Navigator) : Screen {

    @Composable
    override fun Compose() {
        Column {
            Text(text = "Bye bye $name!")
            Button(onClick = { navigator.goTo(HomeScreen, listOf(name)) }) {
                Text("Go Home")
            }
        }
    }

    companion object : ScreenDefinition {

        private const val nameArg = "name"

        override val rawRoute = "bye"
        override val route = "$rawRoute/{$nameArg}"
        override val navArguments = listOf(navArgument(nameArg) { NavType.StringType })

        @Composable
        override fun prepareScreen(
            arguments: Bundle?,
            compositionRoot: ScreenCompositionRoot
        ): Screen {
            val name = arguments?.getString(nameArg)!!
            return ByeScreen(name, compositionRoot.navigator)
        }
    }
}