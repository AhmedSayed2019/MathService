package math.question.task.ui.activity

import android.os.Bundle

import math.question.task.BR
import math.question.task.R
import math.question.task.databinding.ActivityHomeBinding
import math.question.task.ui.base.BaseActivity
import math.question.task.ui.base.GeneralViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel



class HomeActivity : BaseActivity<ActivityHomeBinding, GeneralViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.viewModel = mViewModel
    }



    override fun getLayoutId() = R.layout.activity_home

    override fun getMyViewModel(): GeneralViewModel {
        return getViewModel()
    }

    override fun getBindingVariable() = BR.viewModel
}