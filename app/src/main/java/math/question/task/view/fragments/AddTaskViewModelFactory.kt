package math.question.task.view.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import math.question.task.MyApplication
import math.question.task.view.viewModel.AddTaskViewModel

class AddTaskViewModelFactory(
    var application: MyApplication
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddTaskViewModel(application) as T
    }
}