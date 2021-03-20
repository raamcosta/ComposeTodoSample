package racosta.samples.composetodo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import racosta.samples.composetodo.ui.navigator.NavigatorImpl
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition.Companion.fullRoute
import racosta.samples.composetodo.ui.screens.home.HomeScreen
import racosta.samples.composetodo.ui.theme.ComposeTODOTheme

class MainActivity : AppCompatActivity() {

    private val appCompositionRoot get() = (application as TodoApp).appCompositionRoot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeTODOTheme {
                Navigation()
            }
        }
    }

    @Composable
    private fun Navigation() {
        val navController = rememberNavController()
        val navigator = NavigatorImpl(navController)

        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomBar(navController) }
        ) { paddingValues ->
            
            Box(Modifier.padding(paddingValues)) {
                NavHost(
                    navController = navController,
                    startDestination = HomeScreen.fullRoute()
                ) {
                    appCompositionRoot.screens.forEach {
                        it.addComposable(this, navigator)
                    }
                }
            }
        }
    }

    @Composable
    private fun TopBar() {
        TopAppBar {
            Text("Top app bar", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }

    @Composable
    private fun BottomBar(navController: NavHostController) {
        BottomNavigation {
            appCompositionRoot.screens.filter { it.isBottomNavScreen }.forEach { screen ->
                BottomBarItem(screen, navController)
            }
        }
    }

    @Composable
    private fun RowScope.BottomBarItem(
        screen: ScreenDefinition<*>,
        navController: NavHostController
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

        BottomNavigationItem(
            icon = {
                Icon(
                    screen.icon ?: throw RuntimeException("Trying to set screen with no set icon as part of bottom bar nav!"),
                    screen.iconContentDescription?.let { stringResource(it) } ?: ""
                )
            },
            label = { Text(screen.route) },
            selected = currentRoute == screen.route,
            onClick = {
                navController.navigate(screen.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo = navController.graph.startDestination
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                }
            }
        )
    }
}