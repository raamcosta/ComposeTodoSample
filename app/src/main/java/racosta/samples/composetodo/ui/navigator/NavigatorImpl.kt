package racosta.samples.composetodo.ui.navigator

import androidx.navigation.NavController
import racosta.samples.composetodo.ui.screens.base.ScreenWithArguments
import racosta.samples.composetodo.ui.screens.base.ScreenWithArguments.Companion.fullRoute
import racosta.samples.composetodo.ui.screens.base.Screen

class NavigatorImpl(private val navController: NavController) : Navigator {

    override fun <T> goTo(destination: ScreenWithArguments<T>, arguments: T) {
        navController.navigate(destination.fullRoute(arguments))
    }

    override fun goTo(destination: Screen) {
        navController.navigate(destination.name)
    }
}