package math.question.task.view.viewModel

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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



    lateinit var observer: HomeViewModelObserver
    var isShowLoader = true
    var isShowNoData = false

    var questionModels: ArrayList<QuestionModel>? = ArrayList()
    var recyclerQuestionAdapter: RecyclerQuestionAdapter


    init {


        recyclerQuestionAdapter =
            RecyclerQuestionAdapter(questionModels!!, object : IOnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {

                }

            })

    }
    fun getLocalQuestionModels( binding: FragmentHomeBinding) {
        disposables.add(questionRepo.getQuestionLocalDataSource()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isShowLoader = true
            }.subscribe({ data ->
                if (data.isEmpty()) {
                    isShowNoData = true
                    Log.d("getData", "getLocalQuestionModels: isShowNoData")
                } else {
                    Log.d("getData", "getLocalQuestionModels: get data")
                    isShowNoData = false
                    isShowLoader = false

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