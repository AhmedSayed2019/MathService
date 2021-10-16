package math.question.task.data.pref

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PreferencesUtils(private val sharedPreferences: SharedPreferences) {
    private val gson: Gson = Gson()

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putFloat(key: String, value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getString(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }


    fun getInt(key: String, defValue: Int): Int {

        return sharedPreferences.getInt(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun putObject(key: String, model: Any?) {
        if (model != null) {
            val serializedObject = gson.toJson(model)
            sharedPreferences.edit().putString(key, serializedObject).apply()
        } else {
            sharedPreferences.edit().remove(key).apply()
        }
    }

    fun <T> getObject(
        key: String,
        classType: Class<T>
    ): T? {
        return if (sharedPreferences.contains(key)) {
            gson.fromJson(sharedPreferences.getString(key, ""), classType)
        } else null
    }

    fun <T> putList(key: String, value: List<T>) {
        val jsonArray = gson.toJsonTree(value).asJsonArray
        sharedPreferences.edit().putString(key, jsonArray.toString()).apply()
    }

    fun <T> getList(key: String, classType: Class<T>): List<T>? {
        val typeToken = TypeToken.getParameterized(ArrayList::class.java, classType).type
        return gson.fromJson<List<T>>(sharedPreferences.getString(key, "[]"), typeToken)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }



}