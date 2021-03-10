package racosta.samples.composetodo.dependencyinjection

import kotlinx.coroutines.CoroutineScope
import racosta.samples.composetodo.TodoApp
import racosta.samples.composetodo.ui.navigator.Navigator

class ScreenCompositionRoot(
    app: TodoApp,
    val navigator: Navigator,
    val composeScope: CoroutineScope
) : AppScope by app.appCompositionRoot