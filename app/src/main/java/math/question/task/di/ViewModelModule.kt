package math.question.task.di



import math.question.task.ui.base.GeneralViewModel
import math.question.task.ui.viewModel.AddTaskViewModel
import math.question.task.ui.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { HomeViewModel(get()) }
    viewModel { AddTaskViewModel(get()) }
    viewModel { GeneralViewModel(get()) }


}