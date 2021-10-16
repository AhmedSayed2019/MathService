package com.ershad.ui.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import math.question.task.ui.base.BaseActivity
import math.question.task.ui.base.BaseViewModel


abstract class BaseDialogFragment<T : ViewDataBinding, V : BaseViewModel> : DialogFragment() {

    protected var activity: BaseActivity<*, *>? = null
    lateinit var mViewDataBinding: T
    lateinit var mViewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.activity = context
        }
    }

    override fun onDetach() {
        activity = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mViewModel = getMyViewModel()
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        return mViewDataBinding.root
    }


    fun openActivityAndClearAll(destinationActivity: Class<*>) {
        activity?.openActivityAndClearAll(destinationActivity)
    }



    @SuppressLint("NewApi")
    fun showProgressDialog() {
        activity?.showProgressDialog()
    }

    fun hideProgressDialog() {
        activity?.hideProgressDialog()
    }

    fun provideItemDecoration(): DividerItemDecoration {
        return activity?.provideItemDecoration()!!
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean? {
        return activity?.hasPermission(permission)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        activity?.requestPermissionsSafely(permissions, requestCode)
    }

    fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    fun showLongToast(message: String) {
        activity?.showLongToast(message)
    }

    fun showShortToast(message: String) {
        activity?.showShortToast(message)
    }

    fun showLongToast(resourceId: Int) {
        activity?.showLongToast(resourceId)
    }

    fun showShortToast(resourceId: Int) {
        activity?.showShortToast(resourceId)
    }

    fun backClicked(view: View) {
        activity?.backClicked(view)
    }

    fun homeClicked(view: View) {
        activity?.homeClicked(view)
    }

    fun openActivity(destinationActivity: Class<*>, bundle: Bundle? = null) {
        activity?.openActivity(destinationActivity, bundle)
    }

    fun openActivityAndFinish(destinationActivity: Class<*>) {
        activity?.openActivityAndFinish(destinationActivity)
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getMyViewModel(): V

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

}
