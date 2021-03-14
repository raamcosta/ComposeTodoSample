package racosta.samples.composetodo

import android.app.Application
import racosta.samples.composetodo.di.AppCompositionRoot

class TodoApp : Application() {

    val appCompositionRoot by lazy { AppCompositionRoot(this) }
}