package math.question.task.di

import math.question.task.app.MathServiceApp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import math.question.task.common.view.ResourceProvider

val appModule = module {

    koinApplication { MathServiceApp() }

    single { ResourceProvider(androidApplication()) }
}