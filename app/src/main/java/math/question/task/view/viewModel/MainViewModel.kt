package math.question.task.view.viewModel

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import math.question.task.MyApplication
import math.question.task.adapter.RecyclerQuestionAdapter
import math.question.task.data.model.QuestionModel
import math.question.task.data.database.QuestionDAO
import math.question.task.observer.IOnRecyclerItemClickListener

class MainViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var compositeDisposable = CompositeDisposable()
    val questionDAO: QuestionDAO = db.questionDao()

    var isShowLoader = MutableLiveData<Boolean>()
    var isShowNoData = MutableLiveData<Boolean>()

    var questionModels: ArrayList<QuestionModel>? = ArrayList()
    var recyclerQuestionAdapter: RecyclerQuestionAdapter

    init {
        isShowLoader.value = true
        isShowNoData.value = false

        recyclerQuestionAdapter =
            RecyclerQuestionAdapter(questionModels!!, object : IOnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {

                }

            })
        getLocalQuestionModels()
    }

    fun getLocalQuestionModels() {
        compositeDisposable.add(questionDAO.getQuestionModels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }
            .subscribe(
                { dbQuestionList ->
                    isShowLoader.value = false
                    if (dbQuestionList.isEmpty()) {
                        isShowNoData.value = true
                    } else {
                        questionModels = dbQuestionList.toCollection(ArrayList())
                        recyclerQuestionAdapter.setList(questionModels!!)
                        isShowNoData.value = false
                    }
                },
                { }
            ))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    interface Observer {
    }

}