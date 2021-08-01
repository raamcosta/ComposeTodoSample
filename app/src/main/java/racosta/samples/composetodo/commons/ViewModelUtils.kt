@file:JvmName("Utils")

package racosta.samples.composetodo.commons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import racosta.samples.composetodo.ui.navigator.Navigator

@Composable
inline fun <reified T : ViewModel> navigatorViewModel(
    navigator: Navigator,
    key: String? = null,
    factory: ViewModelProvider.Factory? = null
): T {
    return viewModel<T>(key = key, factory = factory).apply {
        this as NavigatorViewModel

        DisposableEffect(Unit) {
            this@apply.navigator = navigator

            onDispose {
                this@apply.navigator = null
            }
        }
    }
}
