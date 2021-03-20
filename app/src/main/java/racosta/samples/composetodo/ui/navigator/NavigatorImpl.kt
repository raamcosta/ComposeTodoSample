package racosta.samples.composetodo.ui.navigator

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition.Companion.fullRoute

//createExercise/{exerciseId}/{workoutId}
//createExercise/{exerciseId}/{workoutId}?setNumber={setNumber}&repNumber={repNumber}

class NavigatorImpl(private val navController: NavController) : Navigator {

    override fun <T> goTo(destination: ScreenDefinition<T>, arguments: T) {
        navController.navigate(destination.fullRoute(arguments))
    }

//    override fun goTo(destination: ScreenDefinition, args: List<String?>) {
//        if (destination.isBottomNavScreen) {
//            popAllFromBackStack()
//        }
//
//        if (args.all { it == null }) {
//            navController.navigate(destination.rawRoute)
//        } else {
//            val route = destination.rawRoute + args.joinToString(prefix = "/", separator = "/")
//            navController.navigate(route)
//        }
//    }
//
//    private fun popAllFromBackStack() {
//        @Suppress("ControlFlowWithEmptyBody")
//        while (navController.popBackStack()) {
//        }
//    }
}