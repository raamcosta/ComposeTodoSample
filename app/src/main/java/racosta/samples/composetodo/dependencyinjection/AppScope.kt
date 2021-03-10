package racosta.samples.composetodo.dependencyinjection

import android.app.Application
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

interface AppScope {
    val app: Application

    val screens: List<ScreenDefinition>
}