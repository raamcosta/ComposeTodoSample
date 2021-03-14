package racosta.samples.composetodo.ui.screens.base

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.viewmodels.base.NavigatorViewModel

abstract class ModeledScreenDefinition<VM: ViewModel> : ScreenDefinition {

    @Composable
    final override fun prepareScreen(
        arguments: Bundle?,
        compositionRoot: ScreenCompositionRoot
    ): Screen {
        // Additional step for screens with view model:
        // create the view model attaching the navigator if needed
        val viewModel = viewModel(arguments, compositionRoot)

        if (viewModel is NavigatorViewModel) {
            DisposableEffect(Unit) {
                viewModel.navigator = compositionRoot.navigator

                onDispose {
                    viewModel.navigator = null
                }
            }
        }

        return prepareScreen(viewModel)
    }

    @Composable
    abstract fun viewModel(arguments: Bundle?, compositionRoot: ScreenCompositionRoot) : VM

    @Composable
    abstract fun prepareScreen(viewModel: VM) : Screen
}