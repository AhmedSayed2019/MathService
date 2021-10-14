package math.question.task.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import math.question.task.data.database.AppDatabase
import math.question.task.data.database.QuestionDAO
import math.question.task.data.model.QuestionModel
import math.question.task.view.viewModel.HomeViewState
import java.lang.Exception

class QuestionRepo(val context: Context) {

    var questionDAO: QuestionDAO = AppDatabase.getDatabase(context).questionDao()


    private val _mutableListQuestion = MutableStateFlow<HomeViewState>(HomeViewState.Idle)
    val listQuestion: StateFlow<HomeViewState> = _mutableListQuestion



    fun getQuestionLocalDataSource(): Flowable<List<QuestionModel>> {
        try {
            Log.d("getData", "getLocalQuestionModels: try")
            _mutableListQuestion.value = HomeViewState.QuestionList( questionDAO.getQuestionModels().blockingFirst())

        }catch (e:Exception){
            Log.d("getData", "getLocalQuestionModels: catch")
            _mutableListQuestion.value = HomeViewState.Error(e.message!!)

        }
//        listQuestion =  HomeViewState.QuestionList(questionDAO.getQuestionModels())
//        _mutableListQuestion.value = HomeViewState.QuestionList()
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