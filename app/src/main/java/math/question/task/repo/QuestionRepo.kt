package math.question.task.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import math.question.task.data.database.AppDatabase
import math.question.task.data.database.QuestionDAO
import math.question.task.data.model.QuestionModel

class QuestionRepo(val context: Context) {

    var questionDAO: QuestionDAO = AppDatabase.getDatabase(context).questionDao()




    fun getQuestionLocalDataSource():  Flowable<List<QuestionModel>> {
        return questionDAO.getQuestionModels()
    }


    fun insertQuestion(questionModel: QuestionModel) {
        GlobalScope.launch {
            Dispatchers.IO
            questionDAO.insertQuestionModel(questionModel)
        }
    }




    companion object {
        private const val TAG = "QuestionRepo"
    }

}