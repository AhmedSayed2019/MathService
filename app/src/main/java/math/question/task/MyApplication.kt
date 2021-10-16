package math.question.task

import android.app.Application
import android.content.Context
import math.question.task.data.PreferencesUtils

class MyApplication : Application() {

    lateinit var context:Context
    override fun onCreate() {
        super.onCreate()
        PreferencesUtils.initializePreferences(this)
    }

}