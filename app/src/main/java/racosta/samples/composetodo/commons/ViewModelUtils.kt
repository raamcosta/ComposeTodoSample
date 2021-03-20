@file:JvmName("Utils")

package racosta.samples.composetodo.commons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import racosta.samples.composetodo.ui.navigator.Navigator
import racosta.samples.composetodo.ui.viewmodels.base.NavigatorViewModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun ViewModel.launchInScope(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(context, start, block)

@Composable
inline fun <reified T : ViewModel> navigatorViewModel(
    navigator: Navigator,
    key: String? = null,
    factory: ViewModelProvider.Factory? = null
): T {
    return viewModel(T::class.java, key, factory).apply {
        if (this is NavigatorViewModel) {
            DisposableEffect(Unit) {
                this@apply.navigator = navigator

                onDispose {
                    this@apply.navigator = null
                }
            }
        }
    }
}
