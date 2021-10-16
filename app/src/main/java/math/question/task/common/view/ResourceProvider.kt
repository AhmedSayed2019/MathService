package math.question.task.common.view

import android.content.Context

class ResourceProvider(val context: Context) {

    fun getString(resourceId: Int): String{
        return context.getString(resourceId)
    }

    fun getString(resourceId: Int, value: String): String{
        return context.getString(resourceId, value)
    }

    fun getString(resourceId: Int, value: String, value2: String): String{
        return context.getString(resourceId, value, value2)
    }


}