package racosta.samples.composetodo.ui.navigator

import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

interface Navigator {

    fun <T> goTo(destination: ScreenDefinition<T>, arguments: T)
}