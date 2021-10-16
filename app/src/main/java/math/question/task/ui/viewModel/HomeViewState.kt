package math.question.task.ui.viewModel

import math.question.task.data.model.QuestionModel

sealed class HomeViewState {
    //idle
    object  Idle: HomeViewState()

    //list
    data class  QuestionList(val questionList: List<QuestionModel>): HomeViewState()

    //list
    data class  Error(val error: String): HomeViewState()

}