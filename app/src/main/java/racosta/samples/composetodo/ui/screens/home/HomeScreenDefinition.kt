package racosta.samples.composetodo.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import racosta.samples.composetodo.commons.navigatorViewModel
import racosta.samples.composetodo.di.ScreenCompositionRoot
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition
import racosta.samples.composetodo.ui.viewmodels.HomeViewModel

object HomeScreenDefinition : ScreenDefinition<Nothing?> {

    override val route = "home"

    override val icon: ImageVector
        get() = Icons.Filled.Home

    override val iconContentDescription: Int?
        get() = null

    @Composable
    override fun prepareScreen(arguments: Nothing?, compositionRoot: ScreenCompositionRoot): Screen {
        val viewModel: HomeViewModel = navigatorViewModel(
            navigator = compositionRoot.navigator,
            factory = HomeViewModel.Factory(
                compositionRoot.getAllTasksGroupsUseCase,
                compositionRoot.addNewTasksUseCase
            )
        )

        return HomeScreen(viewModel, viewModel)
    }
}