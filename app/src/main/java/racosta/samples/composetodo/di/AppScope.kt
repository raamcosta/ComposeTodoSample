package racosta.samples.composetodo.di

import android.app.Application
import racosta.samples.composetodo.persistence.TodoDatabase
import racosta.samples.composetodo.ui.screens.base.ScreenDefinition

interface AppScope {

    val app: Application

    val screens: Array<ScreenDefinition>

    val db: TodoDatabase
}