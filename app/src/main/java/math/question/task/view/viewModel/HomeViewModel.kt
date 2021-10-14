package math.question.task.view.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import math.question.task.MyApplication
import math.question.task.adapter.RecyclerQuestionAdapter
import math.question.task.data.model.QuestionModel
import math.question.task.databinding.FragmentHomeBinding
import math.question.task.observer.IOnRecyclerItemClickListener
import math.question.task.repo.QuestionRepo
import math.question.task.view.base.BaseViewModel


class HomeViewModel(
    application: MyApplication
) : BaseViewModel() {
    private var questionRepo: QuestionRepo = QuestionRepo(application)


    private val _viewState = MutableStateFlow<HomeViewState>(HomeViewState.Idle)
    val state: StateFlow<HomeViewState> get() = _viewState
    lateinit var observer: HomeViewModelObserver
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowNoData = MutableLiveData<Boolean>()

    var questionModels: ArrayList<QuestionModel>? = ArrayList()
    var recyclerQuestionAdapter: RecyclerQuestionAdapter
    var context: HomeViewModel

    init {
        isShowLoader.value = true
        isShowNoData.value = false
        context = this
        recyclerQuestionAdapter =
            RecyclerQuestionAdapter(questionModels!!, object : IOnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {

                }

            })
    }

    //        fun getAlarmLocalDataSource(): Flowable<List<QuestionModel>> {
//
//            GlobalScope.launch {
//                Dispatchers.IO
//                questionRepo.getQuestionLocalDataSource().collect{
//
//                }
//            }
//            return questionRepo.getQuestionLocalDataSource()
//
//
//        }
//    }
//    //process
//    private  fun processIntent(){
//        viewModelScope.launch {
//            intentChannel.consumeAsFlow().collect {
//                while (it){
//                    is HomeIntent.AddTask ->
//                }
//            }
//        }
//    }
    fun getLocalQuestionModels(binding: FragmentHomeBinding) {
        questionRepo.getQuestionLocalDataSource()
        Log.d("getData", "getLocalQuestionModels: start")
        viewModelScope.launch {
//            Dispatchers.IO
            questionRepo.listQuestion.collect {
                when (it) {
                    is HomeViewState.Idle -> {
                        Log.d("getData", "getLocalQuestionModels: Idle")
                        isShowLoader.value = true
                    }
                    is HomeViewState.Error -> {
                        Log.d("getData", "getLocalQuestionModels: Error")
                    }
                    is HomeViewState.QuestionList -> {

                        if (it.questionList.isEmpty()) {
                            isShowNoData.value = true
                        } else {

                            questionModels = it.questionList.toCollection(ArrayList())
                            recyclerQuestionAdapter.setList(questionModels!!)
                            isShowNoData.value = false
                            isShowLoader.value = false
                            binding.viewModel = context
                        }
                    }
                }
            }
        }

    }

    fun getLocalQuestionModelss(binding: FragmentHomeBinding) {
        disposables.add(questionRepo.getQuestionLocalDataSource()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowLoader.value = true
            }.subscribe({ data ->
                if (data.isEmpty()) {
                    isShowNoData.value = true
                    Log.d("getData", "getLocalQuestionModels: isShowNoData")
                } else {
                    Log.d("getData", "getLocalQuestionModels: get data")
                    isShowNoData.value = false
                    isShowLoader.value = false
                    binding.viewModel = this
                    questionModels = data.toCollection(ArrayList())
                    recyclerQuestionAdapter.setList(questionModels!!)

                }

            }, { throwable ->

            })
        )
    }


    fun onHomeButtonClicked() {
        observer.onHomeButtonClicked()
    }

    interface HomeViewModelObserver {
        fun onHomeButtonClicked()
    }


}