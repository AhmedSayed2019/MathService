package math.question.task.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import math.question.task.BR
import math.question.task.R
import math.question.task.common.util.LOCAL_PRODCAST_RECIEVER_UpdateQuestions
import math.question.task.common.util.LocationHelper
import math.question.task.databinding.FragmentHomeBinding
import math.question.task.ui.base.BaseFragment
import math.question.task.ui.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),

    HomeViewModel.HomeViewModelObserver {

    override fun getLayoutId(): Int =R.layout.fragment_home

    override fun getMyViewModel(): HomeViewModel =getViewModel()

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.observer = this
        mViewDataBinding.viewModel = mViewModel
        mViewModel?.getLocalQuestionModels(mViewDataBinding)

    }

    var updateQuestionsBroadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mViewDataBinding.viewModel?.getLocalQuestionModels(mViewDataBinding)
        }
    }

    private fun registerReceivers() {
        LocalBroadcastManager
            .getInstance(context!!)
            .registerReceiver(
                updateQuestionsBroadCastReceiver,
                IntentFilter(
                    LOCAL_PRODCAST_RECIEVER_UpdateQuestions
                )
            )
    }

    private fun unRegisterReceivers() {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(
            updateQuestionsBroadCastReceiver
        )
    }

    override fun onResume() {
        super.onResume()
        registerReceivers()
    }

    override fun onPause() {
        super.onPause()
        unRegisterReceivers()
    }

    override fun onHomeButtonClicked() {
        findNavController().navigate(R.id.nav_createOperation)
    }


}