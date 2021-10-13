package math.question.task.view.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import math.question.task.MyApplication
import math.question.task.R
import math.question.task.databinding.FragmentHomeBinding
import math.question.task.util.LOCAL_PRODCAST_RECIEVER_UpdateQuestions
import math.question.task.view.viewModel.HomeViewModel
import kotlin.properties.Delegates


class HomeFragment : Fragment(), HomeViewModel.HomeViewModelObserver {


    lateinit var binding: FragmentHomeBinding
    var application: MyApplication by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = activity?.application as MyApplication
        application.context = context!!

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)



//        binding.viewModel = homeViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel =
            ViewModelProvider(
                this,
                HomeViewModelFactory(application)
            ).get(HomeViewModel::class.java)
        binding.viewModel!!.observer = this
        binding.viewModel?.getLocalQuestionModels(binding)

    }

    var updateQuestionsBroadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            binding.viewModel?.getLocalQuestionModels(binding)
        }
    }

    fun registerReceivers() {
        LocalBroadcastManager
            .getInstance(context!!)
            .registerReceiver(
                updateQuestionsBroadCastReceiver,
                IntentFilter(
                    LOCAL_PRODCAST_RECIEVER_UpdateQuestions
                )
            )
    }

    fun unRegisterReceivers() {
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
//        Intent(context, AddNewTaskActivity::class.java).also {
//            startActivity(it)
//            activity?.overridePendingTransition(R.anim.slide_from_right_to_left, R.anim.slide_in_left)
//        }
    }


}