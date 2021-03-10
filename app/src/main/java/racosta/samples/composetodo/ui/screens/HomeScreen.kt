package racosta.samples.composetodo.ui.screens

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import kotlinx.coroutines.flow.StateFlow
import racosta.samples.composetodo.dependencyinjection.ScreenCompositionRoot
import racosta.samples.composetodo.ui.screens.base.ModeledScreenDefinition
import racosta.samples.composetodo.ui.screens.base.Screen
import racosta.samples.composetodo.ui.viewmodels.HomeViewModel

class HomeScreen(private val viewState: State, private val userEvents: UserEvents) : Screen {

    @Composable
    override fun Compose() {
        val name by viewState.name.collectAsState()

        Column {
            Text(text = "Hello $name!")
            Button(onClick = { userEvents.onByeButtonClick() }) {
                Text("Bye!")
            }
        }
    }

    companion object : ModeledScreenDefinition<HomeViewModel>() {

        private const val greetNameArg = "greetName"

        override val rawRoute = "home"
        override val route = "$rawRoute/{$greetNameArg}"
        override val navArguments = listOf(navArgument(greetNameArg) { NavType.StringType })

        override val icon: ImageVector
            get() = Icons.Filled.Home

        override val iconContentDescription: Int?
            get() = null

        @Composable
        override fun viewModel(
            arguments: Bundle?,
            compositionRoot: ScreenCompositionRoot
        ): HomeViewModel {
            val initName = arguments?.getString(greetNameArg) ?: "lol"

            return viewModel(
                factory = HomeViewModel.Factory(initName)
            )
        }

        @Composable
        override fun prepareScreen(viewModel: HomeViewModel): Screen {
            return HomeScreen(viewModel, viewModel)
        }
    }

    interface UserEvents {

        fun onByeButtonClick()
    }

    interface State {

        val name: StateFlow<String>
    }
}
