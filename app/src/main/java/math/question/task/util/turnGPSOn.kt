package math.question.task.view.activity.addNewTask

import androidx.appcompat.app.AppCompatActivity
import math.question.task.R
import math.question.task.observer.IOnAskUserAction
import math.question.task.util.showMessage

fun turnGPSOn(activity: AppCompatActivity, IOnAskUserAction: IOnAskUserAction) {
    showMessage(
        activity,
        activity.getString(R.string.location_required),
        activity.getString(R.string.you_must_enable_device_location_to_get_data_based_on_your_location),
        object : IOnAskUserAction {
            override fun onPositiveAction() {
                IOnAskUserAction.onPositiveAction()
            }

            override fun onNegativeAction() {
            }
        },
        false,
        activity.getString(R.string.cancel),
        activity.getString(R.string.ok),
        false
    )
}