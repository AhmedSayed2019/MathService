package math.question.task.ui.viewModel

import android.content.Context
import android.location.Location
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import math.question.task.R
import math.question.task.data.model.QuestionModel
import math.question.task.common.util.AddOperator
import math.question.task.common.util.DivideOperator
import math.question.task.common.util.MultiplyOperator
import math.question.task.common.util.SubOperator
import math.question.task.ui.base.BaseViewModel

class AddTaskViewModel(
    context: Context
) : BaseViewModel() {
    var  mContext :Context = context

    lateinit var observer: Observer
    var firstNumber = MutableLiveData<String>()
    var secondNumber = MutableLiveData<String>()
    var delayTime = MutableLiveData<String>()
    var isShowFirstNumberError = MutableLiveData<Boolean>()
    var isShowSecondNumberError = MutableLiveData<Boolean>()
    var isShowDelayTimeError = MutableLiveData<Boolean>()

    var methodOperationList: ArrayList<String> = ArrayList()
    var isShowOperatorError = MutableLiveData<Boolean>()
    var operator = MutableLiveData<String>()
    var selectedOperatorPosition = -1

    var isGetMyLocation = MutableLiveData<Boolean>()
    var mLastKnownLocation: Location? = null
    var latitude = 0.0
    var longitude = 0.0

    init {
        isGetMyLocation.value = false
        firstNumber.value = ""
        secondNumber.value = ""
        delayTime.value = ""
        operator.value = ""
        isShowFirstNumberError.value = false
        isShowSecondNumberError.value = false
        isShowDelayTimeError.value = false

        initMethodOperationList()
    }

    val firstNumberTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            isShowFirstNumberError.value =
                firstNumber.value!!.isEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    val secondNumberTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            isShowSecondNumberError.value =
                secondNumber.value!!.isEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    val delayTimeTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            isShowDelayTimeError.value =
                delayTime.value!!.isEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private fun initMethodOperationList() {
        methodOperationList = ArrayList()
        methodOperationList.add(AddOperator)
        methodOperationList.add(SubOperator)
        methodOperationList.add(MultiplyOperator)
        methodOperationList.add(DivideOperator)
    }

    private fun validation(): Boolean {
        var isValid = true
        if (firstNumber.value?.isEmpty()!!) {
            isShowFirstNumberError.value = true
            isValid = false
        }
        if (secondNumber.value?.isEmpty()!!) {
            isShowSecondNumberError.value = true
            isValid = false
        }
        if (selectedOperatorPosition == -1) {
            isShowOperatorError.value = true
            isValid = false
        }
        if (selectedOperatorPosition == 3 && secondNumber.value?.toInt() == 0) {
            isShowSecondNumberError.value = true
            isValid = false
        }
        if (delayTime.value?.isEmpty()!!) {
            isShowDelayTimeError.value = true
            isValid = false
        }
        if (isGetMyLocation.value!! && latitude == 0.0) {
            observer.onShowHideMessageDialog(
                mContext.getString(R.string.please_open_location_or_wait_to_get_your_location),
            )
            isValid = false
        }
        return isValid
    }

    fun onButtonCalculateClicked() {
        if (validation()) {
            val questionModel = QuestionModel()
            questionModel.firstNumber = firstNumber.value
            questionModel.secondNumber = secondNumber.value
            questionModel.operatorText = operator.value
            questionModel.delayTime = delayTime.value?.toInt()!!
            observer.setQuestionAlarm(questionModel)
        }


    }



    interface Observer {
        fun onButtonBackClick()
        fun onShowHideMessageDialog( message: String)
        fun selectOperator()
        fun setQuestionAlarm(questionModel: QuestionModel)
    }

}