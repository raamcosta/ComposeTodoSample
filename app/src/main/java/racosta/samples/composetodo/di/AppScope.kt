package racosta.samples.composetodo.di

import android.app.Application
import racosta.samples.composetodo.persistence.TodoDatabase
import racosta.samples.composetodo.ui.screens.base.Screen

interface AppScope {

    val app: Application

    val screens: Array<Screen>

    val db: TodoDatabase
}