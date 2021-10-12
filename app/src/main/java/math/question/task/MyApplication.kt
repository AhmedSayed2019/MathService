package math.question.task

import android.app.Application
import android.content.Context
import math.question.task.data.Preferences

class MyApplication : Application() {

    lateinit var context:Context
    override fun onCreate() {
        super.onCreate()
        Preferences.initializePreferences(this)
    }

}