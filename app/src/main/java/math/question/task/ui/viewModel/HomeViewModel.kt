package math.question.task.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import math.question.task.adapter.RecyclerQuestionAdapter
import math.question.task.data.model.QuestionModel
import math.question.task.databinding.FragmentHomeBinding
import math.question.task.observer.IOnRecyclerItemClickListener
import math.question.task.repository.QuestionRepo
import math.question.task.ui.base.BaseViewModel


class HomeViewModel(
    context: Context
) : BaseViewModel() {
    private var questionRepo: QuestionRepo = QuestionRepo(context)

    private val _viewState = MutableStateFlow<HomeViewState>(HomeViewState.Idle)
    val state: StateFlow<HomeViewState> get() = _viewState
    lateinit var observer: HomeViewModelObserver
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowNoData = MutableLiveData<Boolean>()

    var questionModels: ArrayList<QuestionModel>? = ArrayList()
    var recyclerQuestionAdapter: RecyclerQuestionAdapter
    var mContext: HomeViewModel

    init {
        isShowLoader.value = true
        isShowNoData.value = false
        mContext = this
        recyclerQuestionAdapter =
            RecyclerQuestionAdapter(questionModels!!, object : IOnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {

                }

            })
    }

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
                            binding.viewModel = mContext
                        }
                    }
                }
            }
        }

    }

    fun onHomeButtonClicked() {
        observer.onHomeButtonClicked()
    }

    interface HomeViewModelObserver {
        fun onHomeButtonClicked()
    }


}