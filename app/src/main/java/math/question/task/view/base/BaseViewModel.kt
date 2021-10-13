package math.question.task.view.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel : ViewModel(){

    protected val disposables = CompositeDisposable()
    val loading = ObservableBoolean(false)
    val message = MutableLiveData<String>()

    override fun onCleared() {
        disposables.clear()
    }
}