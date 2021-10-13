package math.question.task.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import math.question.task.data.model.QuestionModel

@Database(entities = [QuestionModel::class], version = 1,exportSchema = false)
@TypeConverters(Converters::class)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun questionDao(): QuestionDAO
//}

abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDAO

    companion object {
        var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            instance ?: synchronized(this)
            {
                val roomInstance =
                    Room.databaseBuilder(context, AppDatabase::class.java, "MathQuestionsDB").build()
                instance = roomInstance
            }
            return instance!!
        }
    }

}