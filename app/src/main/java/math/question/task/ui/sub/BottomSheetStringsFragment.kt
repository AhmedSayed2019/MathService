package math.question.task.ui.sub

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import math.question.task.R
import math.question.task.adapter.RecyclerStringsBottomSheetAdapter
import math.question.task.databinding.BottomSheetRecyclerBinding
import math.question.task.observer.IOnBottomSheetItemClickListener
import math.question.task.ui.activity.HomeActivity


class BottomSheetStringsFragment : BottomSheetDialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is BaseActivity)
//            activity = context
    }

//    lateinit var activity: BaseActivity
    lateinit var binding: BottomSheetRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_recycler, container, false)
        var view = binding.root
        return view
    }

    lateinit var IOnBottomSheetItemClickListener: IOnBottomSheetItemClickListener

    fun setOnBottomSheetItemClickObserver(listenerI: IOnBottomSheetItemClickListener) {
        this.IOnBottomSheetItemClickListener = listenerI
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var arrayList = requireArguments()["list"] as ArrayList<String>
        binding.tvLabelBottomSheet.text = requireArguments()["title"] as String

        binding.rcBottomSheet.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        var myAdapter = RecyclerStringsBottomSheetAdapter(
            activity as HomeActivity ,
            arrayList,
            object : IOnBottomSheetItemClickListener {
                override fun onBottomSheetItemClickListener(position: Int) {
                    IOnBottomSheetItemClickListener.onBottomSheetItemClickListener(position)
                    dismissAllowingStateLoss()
                }

            })
        binding.rcBottomSheet.adapter = myAdapter
        binding.ivCloseBottomSheet.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }
}