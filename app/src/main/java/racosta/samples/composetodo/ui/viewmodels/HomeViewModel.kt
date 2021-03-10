package racosta.samples.composetodo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import racosta.samples.composetodo.ui.screens.ByeScreen
import racosta.samples.composetodo.ui.screens.HomeScreen
import racosta.samples.composetodo.ui.viewmodels.base.NavigatorViewModel

class HomeViewModel(initName: String) : NavigatorViewModel(),
    HomeScreen.UserEvents, HomeScreen.State {

    //region screen state

    override val name = MutableStateFlow(initName)

    //endregion

    init {
        viewModelScope.launch {
            for (i in 1..10) {
                delay(1000)
                name.value = name.value + "!"
            }
            goTo(ByeScreen, listOf(name.value))
        }
    }

    //region user events handling

    override fun onByeButtonClick() {
        goTo(ByeScreen, listOf(name.value))
    }

    //endregion

    //region factory

    class Factory(private val initName: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(initName) as T
        }
    }

    //endregion
}