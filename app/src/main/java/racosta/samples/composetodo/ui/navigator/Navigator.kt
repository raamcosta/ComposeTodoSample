package racosta.samples.composetodo.ui.navigator

import racosta.samples.composetodo.ui.screens.base.ScreenWithArguments
import racosta.samples.composetodo.ui.screens.base.Screen

interface Navigator {

    fun <T> goTo(destination: ScreenWithArguments<T>, arguments: T)

    fun goTo(destination: Screen)
}