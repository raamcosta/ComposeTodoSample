package racosta.samples.composetodo.ui.navigator

import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

interface Navigator {

    fun goTo(destination: ScreenDefinition, args: List<String> = emptyList())
}