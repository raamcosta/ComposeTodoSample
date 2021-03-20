package racosta.samples.composetodo.ui.navigator

import racosta.samples.composetodo.ui.screens.base.ScreenWithArgumentsDefinition
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

interface Navigator {

    fun <T> goTo(destination: ScreenWithArgumentsDefinition<T>, arguments: T)

    fun goTo(destination: ScreenDefinition)
}