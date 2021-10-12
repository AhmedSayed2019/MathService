package math.question.task.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import math.question.task.observer.IOnAskUserAction
import math.question.task.view.sub.PopupDialogAskUserAction

fun showMessage(
    activity: AppCompatActivity,
    title: String,
    msg: String,
    IOnAskUserAction: IOnAskUserAction?,
    isShowNegativeButton: Boolean,
    negativeText: String,
    positiveText: String,
    isCancelable: Boolean
) {
    if (activity.isFinishing)
        return
    var popupDialogAskUserAction = PopupDialogAskUserAction()
    popupDialogAskUserAction.setOnAskUserActionObserver(object : IOnAskUserAction {
        override fun onPositiveAction() {
            popupDialogAskUserAction.dismissAllowingStateLoss()
            IOnAskUserAction?.onPositiveAction()
        }

        override fun onNegativeAction() {
            IOnAskUserAction?.onNegativeAction()
        }
    })
    var bundle = Bundle()
    bundle.putString("title", title)
    bundle.putString("body", msg)
    bundle.putString("negativeButtonText", negativeText)
    bundle.putString("positiveButtonText", positiveText)
    bundle.putBoolean("isShowTitle", true)
    bundle.putBoolean("isShowNegativeButton", isShowNegativeButton)
    bundle.putBoolean("isShowPositiveButton", true)
    popupDialogAskUserAction.arguments = bundle
    popupDialogAskUserAction.isCancelable = isCancelable
    popupDialogAskUserAction.show(activity.supportFragmentManager, "PopupDialogAskUserAction")
}