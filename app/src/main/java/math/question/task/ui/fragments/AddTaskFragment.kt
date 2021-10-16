package math.question.task.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import math.question.task.BR
import math.question.task.R
import math.question.task.common.util.LOCAL_PRODCAST_RECIEVER_UpdateQuestions
import math.question.task.common.util.LocationHelper
import math.question.task.common.util.MathUtils
import math.question.task.common.util.services.AlarmReceiver
import math.question.task.data.model.QuestionModel
import math.question.task.databinding.FragmentAddTaskBinding
import math.question.task.observer.IOnBottomSheetItemClickListener
import math.question.task.ui.base.BaseFragment
import math.question.task.ui.sub.BottomSheetStringsFragment
import math.question.task.ui.viewModel.AddTaskViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class AddTaskFragment : BaseFragment<FragmentAddTaskBinding , AddTaskViewModel>() , AddTaskViewModel.Observer {
    override fun getLayoutId(): Int =R.layout.fragment_add_task

    override fun getMyViewModel(): AddTaskViewModel =getViewModel()

    override fun getBindingVariable(): Int = BR.viewModel



//    lateinit var binding: FragmentAddTaskBinding
//    var application: MyApplication by Delegates.notNull()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
//    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        application = activity?.application as MyApplication
//        application.context = context!!
//
////        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
////        binding.activity = this
//
//
////        binding.viewModel = homeViewModel
//
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //navController = Navigation.findNavController(view)
        mViewModel.observer = this
        mViewDataBinding.viewModel = mViewModel
        LocationHelper.init(context!!)
        setListener()
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.viewModel =
//            ViewModelProvider(
//                this,
//                AddTaskViewModelFactory(application)
//            ).get(AddTaskViewModel::class.java)
//
//        binding.viewModel!!.observer = this
//        LocationHelper.init(context!!)
//        setListener()
////        binding.viewModel?.getLocalQuestionModels(binding)
//
//    }

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

    override fun onShowHideMessageDialog( message: String) {
        Toast.makeText(
             context,
            message,
        Toast.LENGTH_LONG
        ).show()
    }
     fun setListener() {
        mViewDataBinding.viewModel!!.isGetMyLocation.observe(viewLifecycleOwner , Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                updateLocationUI()
            }
        })
    }
    fun updateLocationUI(){

      val location =   LocationHelper.getLocation()

        Log.d("getLocation", "updateLocationUI: get Location latitude = "+location.latitude+" longitude = "+location.longitude)
        if (mViewDataBinding.viewModel?.mLastKnownLocation == null) {
            mViewDataBinding.viewModel?.mLastKnownLocation = location
            mViewDataBinding.viewModel!!.latitude = location.latitude
            mViewDataBinding.viewModel!!.longitude = location.longitude
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
            mViewDataBinding.viewModel!!.methodOperationList
        )
        bundle.putSerializable("title", getString(R.string.select_math_operator))
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.setOnBottomSheetItemClickObserver(object :
            IOnBottomSheetItemClickListener {
            override fun onBottomSheetItemClickListener(position: Int) {
                mViewDataBinding.viewModel!!.selectedOperatorPosition = position
                mViewDataBinding.viewModel!!.operator.value =
                    mViewDataBinding.viewModel!!.methodOperationList[position]
                mViewDataBinding.viewModel!!.isShowOperatorError.value = false
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
        bundle.putBoolean("isShowLocation", mViewDataBinding.viewModel?.isGetMyLocation?.value!!)
        if (mViewDataBinding.viewModel?.isGetMyLocation?.value!!) {
            bundle.putString("latitude", mViewDataBinding.viewModel?.latitude?.toString() ?: "0")
            bundle.putString("longitude", mViewDataBinding.viewModel?.longitude?.toString() ?: "0")
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
            System.currentTimeMillis() + (mViewDataBinding.viewModel!!.delayTime.value?.toInt()!! * 1000)
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


}