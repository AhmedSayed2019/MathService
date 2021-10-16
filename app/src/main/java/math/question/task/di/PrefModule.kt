package math.question.task.di

import android.app.Application
import android.content.SharedPreferences
import math.question.task.data.pref.PreferencesUtils
import org.koin.android.ext.koin.androidApplication


//import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val prefModule = module {

    single {
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }

    single {
       PreferencesUtils(get())
    }


}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("MATH_SERVICE", android.content.Context.MODE_PRIVATE)
}