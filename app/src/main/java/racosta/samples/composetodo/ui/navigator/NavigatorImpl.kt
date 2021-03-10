package racosta.samples.composetodo.ui.navigator

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

class NavigatorImpl(private val navController: NavController) : Navigator {

    override fun goTo(destination: ScreenDefinition, args: List<String>) {
        if (destination.isBottomNavScreen) {
            popAllFromBackStack()
        }

        val route = destination.rawRoute + args.joinToString(prefix = "/", separator = "/")
        navController.navigate(route)
    }

    private fun popAllFromBackStack() {
        @Suppress("ControlFlowWithEmptyBody")
        while (navController.popBackStack()) { }
    }
}