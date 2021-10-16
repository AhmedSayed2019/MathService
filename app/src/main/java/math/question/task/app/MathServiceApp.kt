package math.question.task.app

import android.app.Application
import math.question.task.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MathServiceApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree());

        startKoin {
            androidLogger()
            androidContext(this@MathServiceApp)
            modules(listOf(appModule, prefModule, networkModule, repositoryModule, viewModelModule))
        }
    }

}
//class   MathServiceApp : Application() {
//
//    override fun onCreate() {
//        super.onCreate()
//
//        Timber.plant(Timber.DebugTree());
//
//
//        startKoin {
//            androidLogger()
//            androidContext(this@MathServiceApp)
//            modules(listOf(appModule, prefModule, repositoryModule, viewModelModule))
//        }
//    }
//
//}