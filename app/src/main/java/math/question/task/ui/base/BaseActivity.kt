package math.question.task.ui.base

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import math.question.task.R
import math.question.task.data.model.DropDownModel
import math.question.task.ui.activity.HomeActivity
import org.koin.core.KoinComponent


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
    KoinComponent {


    protected lateinit var activity: AppCompatActivity
    protected lateinit var progressDialog: Dialog

    lateinit var mViewDataBinding: T
    lateinit var mViewModel: V
    private val TAG: String = BaseActivity::class.java.simpleName

    // Used in checking for runtime permissions.
    val REQUEST_PERMISSIONS_REQUEST_CODE = 34


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        performDataBinding()
    }

    fun setupActionBar() {
//        setSupportActionBar(toolBar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun openActivity(destinationActivity: Class<*>, bundle: Bundle? = null) {
        val mainIntent = Intent(activity, destinationActivity)
        bundle?.let { mainIntent.putExtras(it) }
        startActivity(mainIntent)
    }

    fun openActivityAndFinish(destinationActivity: Class<*>, bundle: Bundle? = null) {
        val mainIntent = Intent(activity, destinationActivity)
        bundle?.let { mainIntent.putExtras(it) }
        startActivity(mainIntent)
        finish()
    }
    fun openActivityAndFinishNoBack(destinationActivity: Class<*>, bundle: Bundle? = null) {
        val mainIntent = Intent(activity, destinationActivity)
        bundle?.let { mainIntent.putExtras(it) }
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainIntent)
        finish()
    }

    fun openActivityAndClearAll(destinationActivity: Class<*>) {
        val i = Intent(activity, destinationActivity)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun provideItemDecoration(): DividerItemDecoration {
        return DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewModel = getMyViewModel()
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showProgressDialog() {
        hideProgressDialog()
        val alertDialogBuilder = AlertDialog.Builder(this).setCancelable(false)
        alertDialogBuilder.setView(R.layout.progress_dialog_loader)
        progressDialog = alertDialogBuilder.create()
        progressDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing)
            progressDialog.dismiss()
    }


    fun showLongToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(resourceId: Int) {
        Toast.makeText(activity, resourceId, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(resourceId: Int) {
        Toast.makeText(activity, resourceId, Toast.LENGTH_SHORT).show()
    }

    fun backClicked(view: View) {
        super.onBackPressed()
    }

    fun homeClicked(view: View) {
        openActivity(HomeActivity::class.java)
    }


    fun openFragment(fragment: Fragment, bundle: Bundle? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        bundle?.let { fragment.arguments = it }
        transaction.replace(R.id.container, fragment, fragment.tag)
//        transaction.addToBackStack("tag").commit()
        transaction.commit()
    }

//    fun openFragmentBack(fragment: Fragment, bundle: Bundle? = null) {
//        val transaction = supportFragmentManager.beginTransaction()
//        bundle?.let { fragment.arguments = it }
//        transaction.replace(R.id.container, fragment, fragment.tag)
//        transaction.addToBackStack("tag").commit()
//
//        if (fragment == AddAttachmentFragment()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
//            super.onBackPressed()
//        }
//    }

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
     *c
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}
