package racosta.samples.composetodo

import android.app.Application
import racosta.samples.composetodo.dependencyinjection.AppCompositionRoot

class TodoApp : Application() {

    val appCompositionRoot = AppCompositionRoot(this)
}