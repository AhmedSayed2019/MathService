package math.question.task.view.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import math.question.task.MyApplication
import math.question.task.R
import math.question.task.data.model.QuestionModel
import math.question.task.databinding.FragmentAddTaskBinding
import math.question.task.observer.IOnAskUserAction
import math.question.task.observer.IOnBottomSheetItemClickListener
import math.question.task.util.LOCAL_PRODCAST_RECIEVER_UpdateQuestions
import math.question.task.util.LocationHelper
import math.question.task.util.MathUtils
import math.question.task.util.ProgressDialogLoading
import math.question.task.util.services.AlarmReceiver

import math.question.task.view.sub.BottomSheetStringsFragment
import math.question.task.view.viewModel.AddTaskViewModel
import kotlin.properties.Delegates


open class AddTaskFragment : Fragment() , AddTaskViewModel.Observer {

    lateinit var binding: FragmentAddTaskBinding
    var application: MyApplication by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        application = activity?.application as MyApplication
        application.context = context!!

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
        binding.activity = this


//        binding.viewModel = homeViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel =
            ViewModelProvider(
                this,
                AddTaskViewModelFactory(application)
            ).get(AddTaskViewModel::class.java)

        binding.viewModel!!.observer = this
        LocationHelper.init(context!!)
        setListener()
//        binding.viewModel?.getLocalQuestionModels(binding)

    }

    var updateQuestionsBroadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            binding.viewModel?.getLocalQuestionModels(binding)
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

    override fun onButtonBackClick() {
        findNavController().popBackStack()
    }

    override fun onShowHideMessageDialog(title: String, message: String, isShow: Boolean) {
        showHideMessageDialog(isShow, title, message)
    }
     fun setListener() {
        binding.viewModel!!.isGetMyLocation.observe(viewLifecycleOwner , Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                updateLocationUI()
            }
        })
    }
    fun updateLocationUI(){

      val location =   LocationHelper.getLocation()

        Log.d("getLocation", "updateLocationUI: get Location latitude = "+location.latitude+" longitude = "+location.longitude)
        if (binding.viewModel?.mLastKnownLocation == null) {
            binding.viewModel?.mLastKnownLocation = location
            binding.viewModel!!.latitude = location.latitude
            binding.viewModel!!.longitude = location.longitude
        }

    }


//    fun open(){
//        val dropDownDialog = BottomSheetStringsFragment.newInstance(resources.getString(title))
//        dropDownDialog.setParam(this, lsit)
//        dropDownDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogSheet)
//        dropDownDialog.show(childFragmentManager, null)
//    }
    override fun selectOperator() {

        val bottomSheetFragment = BottomSheetStringsFragment()
        val bundle = Bundle()

        bundle.putSerializable(
            "list",
            binding.viewModel!!.methodOperationList
        )
        bundle.putSerializable("title", getString(R.string.select_math_operator))
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.setOnBottomSheetItemClickObserver(object :
            IOnBottomSheetItemClickListener {
            override fun onBottomSheetItemClickListener(position: Int) {
                binding.viewModel!!.selectedOperatorPosition = position
                binding.viewModel!!.operator.value =
                    binding.viewModel!!.methodOperationList[position]
                binding.viewModel!!.isShowOperatorError.value = false
            }
        })
        bottomSheetFragment.show(childFragmentManager, null)
    }

    override fun setQuestionAlarm(questionModel: QuestionModel) {
        val intent1 = Intent(context, AlarmReceiver::class.java)
        intent1.action = "Calculate"
        val bundle = Bundle()
        bundle.putString("firstNumber", questionModel.firstNumber)
        bundle.putString("secondNumber", questionModel.secondNumber)
        bundle.putString("operatorText", questionModel.operatorText)
        bundle.putString("delayTime", questionModel.delayTime.toString())
        bundle.putBoolean("isShowLocation", binding.viewModel?.isGetMyLocation?.value!!)
        if (binding.viewModel?.isGetMyLocation?.value!!) {
            bundle.putString("latitude", binding.viewModel?.latitude?.toString() ?: "0")
            bundle.putString("longitude", binding.viewModel?.longitude?.toString() ?: "0")
        }
        intent1.putExtras(bundle)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MathUtils.getRandomNumber(),
            intent1,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarm =
            context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //start service only after given period
        val triggerTime =
            System.currentTimeMillis() + (binding.viewModel!!.delayTime.value?.toInt()!! * 1000)
        alarm.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        Toast.makeText(
            context,
            getString(R.string.question_added_successfully),
            Toast.LENGTH_LONG
        ).show()
        findNavController().popBackStack()
    }


    fun showHideMessageDialog(isShow: Boolean, title: String, message: String) {
//        if (isShow)
//            showMessage(
//               AppCompatActivity(), title,
//                message,
//                object : IOnAskUserAction {
//                    override fun onPositiveAction() {
//                    }
//
//                    override fun onNegativeAction() {
//                    }
//
//                },
//                false,
//                getString(R.string.cancel),
//                getString(R.string.ok),
//                true
//            )
//        else
//            ProgressDialogLoading.dismiss(AppCompatActivity())
    }
}