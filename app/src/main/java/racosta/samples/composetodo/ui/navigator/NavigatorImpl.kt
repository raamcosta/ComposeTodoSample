package racosta.samples.composetodo.ui.navigator

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import racosta.samples.composetodo.ui.screens.base.ScreenWithArgumentsDefinition
import racosta.samples.composetodo.ui.screens.base.ScreenWithArgumentsDefinition.Companion.fullRoute
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

class NavigatorImpl(private val navController: NavController) : Navigator {

    override fun <T> goTo(destination: ScreenWithArgumentsDefinition<T>, arguments: T) {
        navController.navigate(destination.fullRoute(arguments))
    }

    override fun goTo(destination: ScreenDefinition) {
        navController.navigate(destination.name)
    }
}