package racosta.samples.composetodo.ui.viewmodels.base

import androidx.lifecycle.ViewModel
import racosta.samples.composetodo.commons.Logger
import racosta.samples.composetodo.commons.TAG
import racosta.samples.composetodo.ui.navigator.Navigator
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

abstract class NavigatorViewModel: ViewModel(), Logger, Navigator {

    private var pendingNavigatorAction: (() -> Unit)? = null

    var navigator: Navigator? = null
        set(value) = synchronized(this::javaClass) {
            field = value
            if (value != null) checkForPendingNavigation()
        }

    private fun checkForPendingNavigation() {
        if (pendingNavigatorAction == null) {
            return
        }

        debug("We had a pending navigation, navigating now")

        pendingNavigatorAction?.let { action ->
            pendingNavigatorAction = null
            action()
        }
    }

    override fun goTo(destinationRoute: String): Unit = synchronized(this::javaClass) {
        if (pendingNavigatorAction != null) {
            throw IllegalStateException("$this ViewModel as already set a new destination!")
        }

        val navigator = navigator

        if (navigator != null) {
            val result = kotlin.runCatching {
                debug("Navigating to $destinationRoute")
                navigator.goTo(destinationRoute)
//                debug("Navigating to ${destination.TAG}, args = $args")
//                navigator.goTo(destination, args)
            }

            if (result.isSuccess) {
                return
            }

            val exceptionOrNull = result.exceptionOrNull()
            if (exceptionOrNull is IllegalArgumentException) {
                throw exceptionOrNull
            }
            warn("Error while trying to navigate at this point: ${exceptionOrNull?.message}")
        }

        info("We tried to navigate while no navigator was available, we'll save the navigation action..")

        pendingNavigatorAction = {
            goTo(destinationRoute)
//            goTo(destination, args)
        }
    }
}